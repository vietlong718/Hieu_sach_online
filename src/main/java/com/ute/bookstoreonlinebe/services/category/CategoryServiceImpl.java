package com.ute.bookstoreonlinebe.services.category;

import com.ute.bookstoreonlinebe.dtos.CategoryDto;
import com.ute.bookstoreonlinebe.exceptions.InvalidException;
import com.ute.bookstoreonlinebe.exceptions.NotFoundException;
import com.ute.bookstoreonlinebe.entities.Book;
import com.ute.bookstoreonlinebe.entities.Category;
import com.ute.bookstoreonlinebe.repositories.CategoryRepository;
import com.ute.bookstoreonlinebe.services.book.BookService;
import com.ute.bookstoreonlinebe.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookService bookService;


    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name).orElse(null);
    }

    @Override
    public Category getCategoryById(String id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Category có id %s không tồn tại", id)));
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> getCategoryPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return categoryRepository.getCategoryPaging(search, pageable);
    }

    @Override
    public Page<Book> getBookFromCategoryPaging(String id, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return categoryRepository.getBookPaging(id, pageable);
    }

    @Override
    public Category createNewCategory(String name) {
        Category category = getCategoryByName(name);
        if(!ObjectUtils.isEmpty(category)){
            throw new InvalidException(String.format("Category có name %s đã tồn tại", name));
        }
        return categoryRepository.save(new Category(name));
    }

    @Override
    public Category deleteCategory(String id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
        return category;
    }

    @Override
    public Category createNewCategory(String name, String... bookId) {
        Category category = createNewCategory(name);
        List<Book> books = new ArrayList<>();
        for(String id : bookId){
            Book book = bookService.getBookById(id);
            if(!ObjectUtils.isEmpty(book)){
                books.add(book);
            }
        }
        category.setBooksOfCategory(books);
        categoryRepository.save(category);
        return category;
    }


    @Override
    public Category addBookToCategory(String categoryId, String... bookID) {
        Category category = getCategoryById(categoryId);
        List<Book> books = new ArrayList<>();
        for(String id : bookID){
            Book book = bookService.getBookById(id);
            if(!ObjectUtils.isEmpty(book)){
                books.add(book);
            }
        }
        category.setBooksOfCategory(books);
        categoryRepository.save(category);
        return category;
    }

    @Override
    public Category removeBookFromCategory(String categoryId, String... bookID) {
        return null;
    }

    @Override
    public Category changeStatusCategory(String id) {
        Category category = getCategoryById(id);
        category.setEnable(!category.isEnable());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        Category cg = getCategoryById(category.getId());
        cg.setBooksOfCategory(category.getBooksOfCategory());
        categoryRepository.save(cg);
        return null;
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<CategoryDto> getListNamecategory() {
        List<Category> categories = getAllCategory();
        List<CategoryDto> dtos = new ArrayList<>();
        categories.forEach(e -> dtos.add(new CategoryDto(e.getId(),e.getName())));
        return dtos;
    }
}
