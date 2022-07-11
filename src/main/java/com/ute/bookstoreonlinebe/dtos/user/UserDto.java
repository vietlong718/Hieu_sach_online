package com.ute.bookstoreonlinebe.dtos.user;

import com.ute.bookstoreonlinebe.utils.enums.EnumGender;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String email;
    private String password;
    private String fullname;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date birthday;
    private EnumGender gender;
    private String address;
    private String phone;
}
