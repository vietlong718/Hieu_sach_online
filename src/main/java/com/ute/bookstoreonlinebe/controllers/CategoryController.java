package com.ute.bookstoreonlinebe.controllers;

import com.ute.bookstoreonlinebe.dtos.CategoryDto;
import com.ute.bookstoreonlinebe.entities.Book;
import com.ute.bookstoreonlinebe.entities.Category;
import com.ute.bookstoreonlinebe.services.category.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "Get all category")
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategory(){
        return new ResponseEntity<>(categoryService.getAllCategory(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get list name Category")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getListNameCategory(){
        return new ResponseEntity<>(categoryService.getListNamecategory(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Category by id")
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String id){
        return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get danh sách Category có phân trang")
    @PostMapping("/paging")
    public ResponseEntity<Page<Category>> getCategoryPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "name") String column) {
        return new ResponseEntity<>(categoryService.getCategoryPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    @ApiOperation(value = "Get danh sách book của 1 Category có phân trang")
    @PostMapping("/paging/{id}")
    public ResponseEntity<Page<Book>> getBookOfCategoryPaging(
            @PathVariable String id,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "booksOfCategory") String column) {
        return new ResponseEntity<>(categoryService.getBookFromCategoryPaging(id, page, size, sort, column), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin tạo mới 1 category(name)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{name}")
    public ResponseEntity<Category> createNewCategory(@PathVariable String name){
        return new ResponseEntity<>(categoryService.createNewCategory(name), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin delete(xóa dưới db) category")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable String id){
        return new ResponseEntity<>(categoryService.deleteCategory(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin thay đổi trạng thái kích hoạt của 1 category")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/status/{id}")
    public ResponseEntity<Category> changeStatusCategory(@PathVariable String id){
        return new ResponseEntity<>(categoryService.changeStatusCategory(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin update category")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/addBook/{cateId}/{bookId}")
    public ResponseEntity<Category> addBookToCategory(@PathVariable String cateId
            , @PathVariable String bookId){
        return new ResponseEntity<>(categoryService.createNewCategory(cateId), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin xóa book khỏi category")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/removeBook/{cateId}/{BookId}")
    public ResponseEntity<Category> removeBookFromCategory(@PathVariable String cateId, @PathVariable String...bookId){
        return new ResponseEntity<>(categoryService.removeBookFromCategory(cateId, bookId), HttpStatus.OK);
    }

}
