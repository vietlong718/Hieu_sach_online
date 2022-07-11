package com.ute.bookstoreonlinebe.dtos.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartDetail {
    private String bookID;

    private long quantity;
}
