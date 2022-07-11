package com.ute.bookstoreonlinebe.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "transactions-history")
public class TransactionsHistory {
    @Id
    private String id;
    private String amount;
    private String bankCode;
    private String bankTranNo;
    private String cardType;
    private String orderInfo;
    private String payDate;
    private String responseCode;
    private String tmnCode;
    private String transactionNo;
    private String txnRef;
    private String secureHashType;
    private String secureHash;

    @DBRef
    private Order order;
}
