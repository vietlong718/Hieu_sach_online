package com.ute.bookstoreonlinebe.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Document(collection = "discounts")
public class Discount {
    @Id
    private String id;

    private String secret;

    private String percent;
}
