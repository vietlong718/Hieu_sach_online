package com.ute.bookstoreonlinebe.services.user;

import com.ute.bookstoreonlinebe.dtos.PasswordDto;
import com.ute.bookstoreonlinebe.dtos.book.BookInCart;
import com.ute.bookstoreonlinebe.dtos.user.UserDto;
import com.ute.bookstoreonlinebe.entities.User;
import com.ute.bookstoreonlinebe.models.EmailDetails;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface UserService {

//    Page<User> getUserPaging(String search, int page, int size, String sort, String column);

    User getUser(Principal principal);

    Page<User> getUserPaging(String search, int page, int size, String sort, String column);

    List<User> getAllUser();

    User getUserByID(String id);

    User getUserCoreByEmail(String email);

    User getUserCoreByPhone(String phone);

    User addNewUserCore(String fullnam, String email, String password);

    User createAdmin(UserDto dto);

    User createNewUser(UserDto dto);

    User convertDto(UserDto dto);

    User updateUser(String id, Principal principal, UserDto dto);

    User changeStatus(String id, Principal principal);

    User changePassword(String id, Principal principal, PasswordDto passwordDto);

    User checkUserWithIDAndPrincipal(String id, Principal principal);

    List<String> getRoles();

    User updateName(UserDto dto, Principal principal);


    User updateAvatar(String id, Principal principal, MultipartFile file);

    User save(User user);


}
