package com.ute.bookstoreonlinebe.entities.embedded;

import com.ute.bookstoreonlinebe.entities.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class EmbeddedBookInOrder {
    @DBRef
    private Book book;
    private long quantity;
    private float total;
}
