package com.ute.bookstoreonlinebe.controllers;

import com.ute.bookstoreonlinebe.dtos.PasswordDto;
import com.ute.bookstoreonlinebe.dtos.book.BookDto;
import com.ute.bookstoreonlinebe.dtos.user.UserDto;
import com.ute.bookstoreonlinebe.entities.Book;
import com.ute.bookstoreonlinebe.entities.User;
import com.ute.bookstoreonlinebe.models.MyConstants;
import com.ute.bookstoreonlinebe.services.file.FilesStorageService;
import com.ute.bookstoreonlinebe.services.user.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/rest/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private FilesStorageService storageService;

    @ApiOperation(value = "Create a new User")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<User> createNewUser(@RequestBody UserDto dto) {
        return new ResponseEntity<>(userService.createNewUser(dto), HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new Admin")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createNewAdmin")
    public ResponseEntity<User> createNewAdmin(@RequestBody UserDto dto) {
        return new ResponseEntity<>(userService.createAdmin(dto), HttpStatus.OK);
    }

    @ApiOperation(value = "Get thông tin tài khoản by token")
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping
    public ResponseEntity<User> getUser(Principal principal) {
        return new ResponseEntity<>(userService.getUser(principal), HttpStatus.OK);
    }

    @ApiOperation(value = "Get thông tin của 1 user bằng id")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<User> getUserByID(@PathVariable String id) {
        return new ResponseEntity<>(userService.getUserByID(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get tất cả user có phân trang cho admin")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paging")
    public ResponseEntity<Page<User>> getUserPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "id") String column) {
        return new ResponseEntity<>(userService.getUserPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    @ApiOperation(value = "User update thông tin")
    @PreAuthorize("hasRole('MEMBER')")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable String id, Principal principal, @RequestBody UserDto dto) {
        return new ResponseEntity<>(userService.updateUser(id, principal, dto), HttpStatus.OK);
    }

    @ApiOperation(value = "Update avatar")
    @PreAuthorize("hasRole('MEMBER')")
    @PutMapping("/avatar/{id}")
    public ResponseEntity<User> updateAvatar(
            HttpServletRequest request, @PathVariable String id,
            Principal principal, @RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(userService.updateAvatar(id, principal, file), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin thay đổi trạng thái kích hoạt tài khoản user")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<User> changeStatus(@PathVariable String id, Principal principal) {
        return new ResponseEntity<>(userService.changeStatus(id, principal), HttpStatus.OK);
    }

    @ApiOperation(value = "Get avatar user")
    @GetMapping(value = "/avatar/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getFileAvatar(
            @PathVariable(value = "filename") String fileName){
        String filePath = "avatar/" + fileName;
        Resource file = storageService.loadFile(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(file, headers , HttpStatus.OK);
    }
    @ApiOperation(value = "User change password")
    @PutMapping(value = "/password/{id}")
    @ResponseBody
    public ResponseEntity<User> changePassword(
            @PathVariable String id, Principal principal,
            @RequestBody PasswordDto dto){
        return new ResponseEntity<>(userService.changePassword(id, principal, dto) , HttpStatus.OK);
    }
}
