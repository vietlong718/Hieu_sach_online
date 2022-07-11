package com.ute.bookstoreonlinebe.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter @Setter
@ToString
@EqualsAndHashCode
@Document(collection = "categories")
@TypeAlias(value = "Category")
public class Category implements Serializable {
    @Id
    private String id;

    @Indexed(unique = true)
    @NonNull private String name;

    @DBRef(lazy = true)
    private List<Book> booksOfCategory = new ArrayList<>();

    private boolean enable = true;
}
