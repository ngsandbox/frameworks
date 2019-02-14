package org.bookstore.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.bookstore.common.entities.BookDetails;
import org.bookstore.web.services.BookstoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
public class BookstoreController {

    private final BookstoreService bookstoreService;

    @Autowired
    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/task/books")
    public String books(@RequestParam(name = "user") String user,
                        Model model) {
        model.addAttribute("books", bookstoreService.getBooks());
        model.addAttribute("user", user);
        return "books";
    }

    @PostMapping("/task/details")
    public String details(@RequestParam(name = "book") String book,
                          @RequestParam(name = "user") String user,
                          Model model) {
        Optional<BookDetails> bookDetails = bookstoreService.trackBookAndGetDetails(book, user);
        if (bookDetails.isPresent()) {
            model.addAttribute("detail", bookDetails.get());
            return "details";
        }

        model.addAttribute("books", bookstoreService.getBooks());
        model.addAttribute("user", user);
        return "books";
    }
}
