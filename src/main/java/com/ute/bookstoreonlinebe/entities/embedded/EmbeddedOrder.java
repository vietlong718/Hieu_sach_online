package com.ute.bookstoreonlinebe.entities.embedded;

import com.mongodb.lang.Nullable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmbeddedOrder {
    @Id
    private String id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date orderDate;

    @Nullable
    private List<EmbeddedCart> books = new ArrayList<>();

    @NonNull
    private EmbeddedPrice total;

    private String address;

    private boolean status = true;

    private boolean pay = false;

    private boolean processing = true;
}
