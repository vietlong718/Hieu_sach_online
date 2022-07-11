package com.ute.bookstoreonlinebe.entities.embedded;

import com.ute.bookstoreonlinebe.utils.enums.EnumCurrency;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmbeddedPrice {
    private float price;

    private EnumCurrency currency;
}
