package com.ute.bookstoreonlinebe.services.book;

import com.ute.bookstoreonlinebe.dtos.book.BookDto;
import com.ute.bookstoreonlinebe.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    Book getBookById(String id);

    Book getBookByName(String name);

    List<Book> getAllBook();

    Page<Book> getBookPaging(String search, int page, int size, String sort, String column);

    List<Book> getAllBookFromCategory(String name);

    Book convertDtoToBook(BookDto dto);

    Book createNewBook(BookDto dto, MultipartFile files);

    Book updateBook(String id, BookDto dto, MultipartFile files);

    Book changeStatusBook(String id);

    Book addCategoryToBook(String bookId, String cateId);

    Book removeCategoryFromBook(String bookId, String cateId);

    List<Book> searchBook(String search);

    Book addImageBook(String id, MultipartFile files);

    Book deleteImageBook(String id, String name);

    Book save(Book book);
}
