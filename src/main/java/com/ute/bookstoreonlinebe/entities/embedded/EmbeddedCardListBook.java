package com.ute.bookstoreonlinebe.entities.embedded;

import com.ute.bookstoreonlinebe.entities.Book;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmbeddedCardListBook {
    @DBRef
    private Book book;

    private long quantity;

    private float total;
}
