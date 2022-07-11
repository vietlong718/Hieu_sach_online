package com.ute.bookstoreonlinebe.dtos.payment;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PaymentDto {
    String orderId;
    int amount;
    String description;
    String bankCode;
}
