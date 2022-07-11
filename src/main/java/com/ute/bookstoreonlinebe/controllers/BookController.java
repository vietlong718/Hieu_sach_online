package com.ute.bookstoreonlinebe.controllers;

import com.ute.bookstoreonlinebe.dtos.book.BookDto;
import com.ute.bookstoreonlinebe.entities.Book;
import com.ute.bookstoreonlinebe.services.book.BookService;
import com.ute.bookstoreonlinebe.services.file.FilesStorageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/rest/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private FilesStorageService storageService;

    @ApiOperation(value = "Get tất cả sách (không phân trang)")
    @GetMapping("/all")
    public ResponseEntity<List<Book>> getAllBook( ){
        return new ResponseEntity<>(bookService.getAllBook(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get danh sách book có phân trang (bao gồm tìm kiếm sách)")
    @GetMapping("/paging")
    public ResponseEntity<Page<Book>> getBookPaging(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "book") String column) {
        return new ResponseEntity<>(bookService.getBookPaging(search, page, size, sort, column), HttpStatus.OK);
    }

    @ApiOperation(value = "Get 1 book với id")
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id){
        return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin tạo mới 1 book")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Book> createNewBook(
            @RequestBody BookDto dto,
            @RequestParam(value = "files", required = false) MultipartFile files) throws HttpMediaTypeException {
        return new ResponseEntity<>(bookService.createNewBook(dto, files), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin thay đổi trạng thái kích hoạt của book")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Book> changStatusBook(@PathVariable String id){
        return new ResponseEntity<>(bookService.changeStatusBook(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin cập nhập thông tin book")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable String id, @RequestBody BookDto dto,
            @RequestParam(value = "files", required = false) MultipartFile files){
        return new ResponseEntity<>(bookService.updateBook(id, dto, files), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin thêm hình ảnh cho book")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/image/{id}")
    public ResponseEntity<Book> addImageBook(
            @PathVariable String id, @RequestParam(value = "files", required = false) MultipartFile files){
        return new ResponseEntity<>(bookService.addImageBook(id,  files), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin xóa hình ảnh cho book")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/image/{id}/{name}")
    public ResponseEntity<Book> deleteImageBook(
            @PathVariable(value = "id") String id, @PathVariable(value = "name") String name){
        return new ResponseEntity<>(bookService.deleteImageBook(id,  name), HttpStatus.OK);
    }

    @ApiOperation(value = "Get book image")
    @GetMapping(value = "/image/{id}/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getImage(
            @PathVariable(value = "id") String id,@PathVariable(value = "name") String name){
        String path = "books/" + id + "/" + name;
        Resource file = storageService.loadFile(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(file, headers , HttpStatus.OK);
    }
}
