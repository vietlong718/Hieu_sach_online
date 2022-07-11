package com.ute.bookstoreonlinebe.services.transaction;

import com.ute.bookstoreonlinebe.entities.TransactionsHistory;
import com.ute.bookstoreonlinebe.repositories.TransactionsHistoryRepository;
import com.ute.bookstoreonlinebe.services.order.OrderService;
import com.ute.bookstoreonlinebe.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService{
    private TransactionsHistoryRepository transactionsRepository;

    private OrderService orderService;

    public TransactionServiceImpl(TransactionsHistoryRepository transactionsRepository, OrderService orderService) {
        this.transactionsRepository = transactionsRepository;
        this.orderService = orderService;
    }

    @Override
    public TransactionsHistory getTransactionById(String id) {
        return transactionsRepository.findById(id).orElse(null);
    }

    @Override
    public TransactionsHistory getTransactionByOrderId(String id) {
        return transactionsRepository.getTransactionByOrderId(id);
    }

    @Override
    public List<TransactionsHistory> getAllTransactions() {
        return transactionsRepository.findAll();
    }

    @Override
    public Page<TransactionsHistory> getTransactionPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return transactionsRepository.getTransactionPaging(search, pageable);
    }
}
