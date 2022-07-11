package com.ute.bookstoreonlinebe.services.user;

import com.ute.bookstoreonlinebe.dtos.PasswordDto;
import com.ute.bookstoreonlinebe.dtos.book.BookInCart;
import com.ute.bookstoreonlinebe.dtos.card.CartDetail;
import com.ute.bookstoreonlinebe.dtos.user.UserDto;
import com.ute.bookstoreonlinebe.entities.Book;
import com.ute.bookstoreonlinebe.entities.User;
import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedCardListBook;
import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedCart;
import com.ute.bookstoreonlinebe.exceptions.InvalidException;
import com.ute.bookstoreonlinebe.exceptions.NotFoundException;
import com.ute.bookstoreonlinebe.models.EmailDetails;
import com.ute.bookstoreonlinebe.repositories.UserRepository;
import com.ute.bookstoreonlinebe.services.book.BookService;
import com.ute.bookstoreonlinebe.services.file.FilesStorageService;
import com.ute.bookstoreonlinebe.services.mailsender.MailSenderService;
import com.ute.bookstoreonlinebe.utils.enums.EnumRole;
import com.ute.bookstoreonlinebe.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    private FilesStorageService storageService;

    @Autowired
    private BookService bookService;

    @Value("${default.password}")
    private String defaultPassword;

    @Autowired
    private MailSenderService mailSenderService;

    @Value("${upload.url}")
    private String url;

    @Value("${default.avatar}")
    private String defaultAvatart;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException(String.format("User có email %s không tồn tại", principal.getName())));
    }

    @Override
    public Page<User> getUserPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return userRepository.getUserPaging(search, pageable);
    }

    @Override
    public List<User> getAllUser() {
        List<User> userList = userRepository.findAll();
        return userList;
    }

    @Override
    public User getUserByID(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User có id %s không tồn tại", id)));
    }

    @Override
    public User getUserCoreByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User getUserCoreByPhone(String phone) {
        return userRepository.getUserCoreByPhone(phone).orElse(null);
    }

    @Override
    public User addNewUserCore(String fullname, String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setFullname(fullname);
        user.setPassword(password);
        user.setRoles(Collections.singletonList(EnumRole.ROLE_MEMBER.name()));
        user.setEnable(true);
        userRepository.save(user);
        return user;
    }

    @Override
    public User createAdmin(UserDto dto) {
        User user = convertDto(dto);
        user.setRoles(Arrays.asList(EnumRole.ROLE_ADMIN.name(), EnumRole.ROLE_MEMBER.name()));
        userRepository.save(user);
        return user;
    }

    @Override
    public User createNewUser(UserDto dto) {
        User user = convertDto(dto);

        user.setRoles(Collections.singletonList(EnumRole.ROLE_MEMBER.name()));
        userRepository.save(user);

        mailSenderService.sendMailSignup(user);
        return user;
    }

    @Override
    public User convertDto(UserDto dto) {
        if (ObjectUtils.isEmpty(dto.getEmail())) {
            throw new InvalidException("Email không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getPhone())) {
            throw new InvalidException("Phone đã không được bỏ trống");
        }
        if (ObjectUtils.isEmpty(dto.getPassword())) {
            throw new InvalidException("Password không được bỏ trống");
        }

        User userCoreByEmail = getUserCoreByEmail(dto.getEmail());
        if (!ObjectUtils.isEmpty(userCoreByEmail)) {
            throw new InvalidException(String.format("Email %s đã được sử dụng", dto.getEmail()));
        }

        User userCoreByPhone = getUserCoreByPhone(dto.getPhone());
        if (!ObjectUtils.isEmpty(userCoreByPhone)){
            throw new InvalidException(String.format("Phone %s đã được sử dụng", dto.getPhone()));
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(dto.getPassword());

        user.setFullname(dto.getFullname());
        user.setBirthday(dto.getBirthday());
        user.setGender(dto.getGender());
        user.setAddress(dto.getAddress());
        user.setAvatar(url + "rest/user/" + defaultAvatart);

        user.setEnable(true);
        return user;
    }

    @Override
    public User updateUser(String id, Principal principal, UserDto dto) {
        User user = getUserByID(id);
        if (!user.getEmail().equals(principal.getName())){
            throw new InvalidException("Token không đến từ đúng người dùng.");
        }
        User userCoreByEmail = getUserCoreByEmail(dto.getEmail());
        if (!ObjectUtils.isEmpty(userCoreByEmail)) {
            throw new InvalidException(String.format("Email %s đã được sử dụng", dto.getEmail()));
        }

        User userCoreByPhone = getUserCoreByPhone(dto.getPhone());
        if (!ObjectUtils.isEmpty(userCoreByPhone)){
            throw new InvalidException(String.format("Phone %s đã được sử dụng", dto.getPhone()));
        }
        user.setFullname(dto.getFullname());
//        user.setPassword(dto.getPassword());
//        if (!dto.getEmail().toLowerCase().trim().equals(user.getEmail()) &&
//                getUserCoreByEmail(dto.getEmail().toLowerCase().trim()) == null) {
//            user.setEmail(dto.getEmail().toLowerCase().trim());
//        }
        user.setBirthday(dto.getBirthday());
        user.setPhone(dto.getPhone());
        user.setGender(dto.getGender());
        user.setAddress(dto.getAddress());

        userRepository.save(user);
        return user;
    }

    @Override
    public User changeStatus(String id, Principal principal) {
        User user = getUserByID(id);
        if (user.getEmail().equals(principal.getName())) {
            throw new InvalidException("Cannot change status for main account");
        }
        user.setEnable(!user.isEnable());
        userRepository.save(user);
        return user;
    }

    @Override
    public User changePassword(String id, Principal principal, PasswordDto passwordDto) {
        User user = checkUserWithIDAndPrincipal(id, principal);
        if (!user.getPassword().equals(passwordDto.getOldPass())){
            throw new InvalidException("Old pass does not correct");
        }
        user.setPassword(passwordDto.getNewPass());
        userRepository.save(user);
        return user;
    }

    @Override
    public User checkUserWithIDAndPrincipal(String id, Principal principal) {
        User user = getUserByID(id);
        if (!user.getEmail().equals(principal.getName())){
            throw new InvalidException("Token không đến từ đúng người dùng.");
        }
        return user;
    }

    @Override
    public List<String> getRoles() {
        return Arrays.stream(EnumRole.values()).map(Enum::name).collect(Collectors.toList());
    }

    @Override
    public User updateName(UserDto dto, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException(String.format("Account have email %s does not exist", principal.getName())));
        user.setFullname(dto.getFullname());
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateAvatar(String id, Principal principal, MultipartFile file) {
        User user = getUserByID(id);
        if (!user.getEmail().equals(principal.getName())){
            throw new InvalidException("Token không đến từ đúng người dùng.");
        }
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String ext = fileName.substring(fileName.lastIndexOf("."));
            String newName = id + "_" + new Date().getTime() + ext;
            storageService.save("avatar", newName, file);
            String nameAvatarCurrent = user.getAvatar();
            if(!nameAvatarCurrent.equals("avatar_default.png")){
                storageService.deleteAvatar("avatar", nameAvatarCurrent);
            }
            user.setAvatar(url + "rest/user/avatar/" + newName);
            return save(user);


        }catch (Exception e){
            throw new InvalidException(String.format("Không thể upload file: %s", file.getOriginalFilename()));
        }

    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

}
