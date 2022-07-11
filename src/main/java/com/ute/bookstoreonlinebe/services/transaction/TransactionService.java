package com.ute.bookstoreonlinebe.services.transaction;

import com.ute.bookstoreonlinebe.entities.TransactionsHistory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    TransactionsHistory getTransactionById(String id);

    TransactionsHistory getTransactionByOrderId(String id);

    List<TransactionsHistory> getAllTransactions();

    Page<TransactionsHistory> getTransactionPaging(String search, int page, int size, String sort, String column);
}
