package com.ute.bookstoreonlinebe.dtos.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class BookDetailDto {
    private String id;
    private long quantity;
}
