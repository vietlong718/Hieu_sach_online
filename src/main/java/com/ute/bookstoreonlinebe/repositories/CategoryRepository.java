package com.ute.bookstoreonlinebe.repositories;

import com.ute.bookstoreonlinebe.entities.Book;
import com.ute.bookstoreonlinebe.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findCategoryByName(String categoryName);

    List<Category> findAll();

    @Query(value = "{'name' : { $regex: ?0, $options: 'i' } }"
            , sort = "{'enable' : -1, 'name' : 1}")
    Page<Category> getCategoryPaging(String search, Pageable pageable);

    @Query(value = "{'id' : { $regex: ?0, $options: 'i' } }"
            , sort = "{'enable' : -1, 'name' : 1}")
    Page<Book> getBookPaging(String search, Pageable pageable);


}