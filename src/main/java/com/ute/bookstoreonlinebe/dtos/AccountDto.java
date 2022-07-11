package com.ute.bookstoreonlinebe.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountDto {
    private String email;

    private String password;
}
