package com.ute.bookstoreonlinebe.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionStatusDto {
    String status;
    String message;
    String data;
}
