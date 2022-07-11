package com.ute.bookstoreonlinebe.controllers;

import com.ute.bookstoreonlinebe.entities.TransactionsHistory;
import com.ute.bookstoreonlinebe.services.transaction.TransactionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/transactions")
public class TransactionController {
    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @ApiOperation(value = "Get lịch sử giao dịch by id.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable String id){
        return new ResponseEntity<>(transactionService.getTransactionById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get lịch sử giao dịch by orderid.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/order/{id}")
    public ResponseEntity<?> getAllTransactions(@PathVariable String id){
        return new ResponseEntity<>(transactionService.getTransactionByOrderId(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get tất cả lịch sử giao dịch không phân trang.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllTransactions(){
        return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get tất cả lịch sử giao dịch không phân trang.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paging")
    public ResponseEntity<Page<TransactionsHistory>> getTransactionPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "payDate") String column) {
        return new ResponseEntity<>(transactionService.getTransactionPaging(search, page, size, sort, column), HttpStatus.OK);
    }
}
