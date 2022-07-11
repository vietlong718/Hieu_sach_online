package com.ute.bookstoreonlinebe.entities;

import com.mongodb.lang.Nullable;
import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedBookInOrder;
import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedCardListBook;
import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedPrice;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
@EqualsAndHashCode
@Document(collection = "orders")
public class Order {
    @Id
    private String id;

    @DBRef
    private User user;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date orderDate;

    private List<EmbeddedBookInOrder> booksInOrder = new ArrayList<>();

    @NonNull
    private EmbeddedPrice subtotal;

    private String address;

    private String phone;

    private String note;

    private boolean status = true; // trạng thái của order

    private boolean pay = false; // trạng thái thanh toán

    private boolean shipping = false; // trạng thái vận chuyển hàng (chưa giao hàng(false) or đang giao hàng(true))

    private boolean delivered = false; // trạng thái hoàn tất việc giao hàng
}
