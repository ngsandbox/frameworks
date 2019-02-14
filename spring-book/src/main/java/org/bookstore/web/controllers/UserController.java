package org.bookstore.web.controllers;

import org.bookstore.web.models.AuthErrors;
import org.bookstore.web.services.BookstoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.bookstore.web.services.UserService;

import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final BookstoreService bookstoreService;

    @Autowired
    public UserController(UserService userService, BookstoreService bookstoreService) {
        this.userService = userService;
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/")
    public ModelAndView redirect() {
        return new ModelAndView("redirect:/task/login");
    }

    @GetMapping("/task/login")
    public String welcome() {
        return "login";
    }

    @PostMapping("/task/login")
    public String loginUser(@RequestParam(name = "user") String user,
                            @RequestParam(name = "pwd") char[] pwd,
                            Model model) {

        Optional<AuthErrors> authError = userService.getAuthError(user, pwd);
        if (authError.isPresent()) {
            AuthErrors errors = authError.get();
            model.addAttribute("user", user);
            model.addAttribute("errors", errors.getErrors());
            return "login";
        }

        model.addAttribute("books", bookstoreService.getBooks());
        model.addAttribute("user", user);
        return "books";
    }

}
