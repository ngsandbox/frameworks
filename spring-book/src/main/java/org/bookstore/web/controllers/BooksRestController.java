package org.bookstore.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.bookstore.common.entities.Book;
import org.bookstore.common.entities.BookView;
import org.bookstore.web.services.BookstoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class BooksRestController {

    private final BookstoreService bookstoreService;

    @Autowired
    public BooksRestController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/v1/books")
    public List<Book> getBooks() {
        log.debug("Get list of books");
        return bookstoreService.getBooks();
    }

    @GetMapping("/v1/books/views")
    public List<BookView> getBookViews() {
        log.debug("Get list of books");
        return bookstoreService.getBookViews();
    }

    @GetMapping("/v1/book/views/{email}")
    public List<BookView> getBookViews(@PathVariable(name = "email") String email) {
        log.debug("Get list of book's view for {}", email);
        return bookstoreService.getBooksViews(email);
    }
}
