package com.ute.bookstoreonlinebe.dtos.book;

import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedCategory;
import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedDescription;
import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedPrice;
import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedPublishers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookDto {
    private String name;

    private String author;

    private EmbeddedPublishers publishers;

    private List<EmbeddedDescription> description;

    private EmbeddedPrice price;

    private List<String> image_URLs = new ArrayList<>();

    private long quantity;

    private int discount;

    private Set<EmbeddedCategory> fallIntoCategories = new HashSet<>();
}
