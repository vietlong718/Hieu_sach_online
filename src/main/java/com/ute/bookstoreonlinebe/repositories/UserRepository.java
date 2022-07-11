package com.ute.bookstoreonlinebe.repositories;

import com.ute.bookstoreonlinebe.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    @Query(value = "{'email' : ?0}")
    Optional<User> getUser(String email);

    @Query(value = "{$or: [{'email' : { $regex: ?0, $options: 'i' } }, {'name' : { $regex: ?0, $options: 'i' } }] }"
            , sort = "{'enable' : -1, 'email' : 1}")
    Page<User> getUserPaging(String search, Pageable pageable);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndEnableTrue(String id);

    @Query(value = "{'phone': ?0}")
    Optional<User> getUserCoreByPhone(String phone);
}
