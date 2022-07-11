package com.ute.bookstoreonlinebe.repositories;

import com.ute.bookstoreonlinebe.entities.TransactionsHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsHistoryRepository extends MongoRepository<TransactionsHistory, String> {
    @Query(value = "{'order.id': ?0}")
    TransactionsHistory getTransactionByOrderId(String id);

    @Query(value = "{$or: [{'id' : { $regex: ?0, $options: 'i' } }, {'order.id' : { $regex: ?0, $options: 'i' }}, {'payDate' : { $regex: ?0, $options: 'i' }}, {'responseCode' : { $regex: ?0, $options: 'i' }}] }"
    , sort = "{'responseCode' : -1, 'payDate' : 1}")
    Page<TransactionsHistory> getTransactionPaging(String search, Pageable pageable);
}
