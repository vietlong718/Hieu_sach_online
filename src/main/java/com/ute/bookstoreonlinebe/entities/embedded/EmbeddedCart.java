package com.ute.bookstoreonlinebe.entities.embedded;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmbeddedCart {
    private List<EmbeddedCardListBook> listBookInCart = new ArrayList<>();
}
