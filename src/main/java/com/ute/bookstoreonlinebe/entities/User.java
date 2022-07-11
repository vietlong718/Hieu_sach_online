package com.ute.bookstoreonlinebe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedCart;
import com.ute.bookstoreonlinebe.utils.enums.EnumGender;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
@EqualsAndHashCode
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private String fullname;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date birthday;

    private EnumGender gender;

    private String address;

    @Indexed(unique = true)
    private String phone;

    private String avatar;

    private List<String> roles = new ArrayList<>();

    private boolean enable = true;
}