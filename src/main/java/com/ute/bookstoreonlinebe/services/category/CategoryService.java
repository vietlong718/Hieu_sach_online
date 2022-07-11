package com.ute.bookstoreonlinebe.services.category;

import com.ute.bookstoreonlinebe.dtos.CategoryDto;
import com.ute.bookstoreonlinebe.entities.Book;
import com.ute.bookstoreonlinebe.entities.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    Category getCategoryByName(String name);

    Category getCategoryById(String id);

    List<Category> getAllCategory();

    Page<Category> getCategoryPaging(String search, int page, int size, String sort, String column);

    Page<Book> getBookFromCategoryPaging(String search, int page, int size, String sort, String column);

    Category createNewCategory(String name);

    Category deleteCategory(String id);

    Category createNewCategory(String name, String...id);

    Category addBookToCategory(String categoryId, String...bookID);

    Category removeBookFromCategory(String categoryId, String...bookID);

    Category changeStatusCategory(String id);

    Category updateCategory(Category category);

    Category save(Category category);

    List<CategoryDto> getListNamecategory();
}
