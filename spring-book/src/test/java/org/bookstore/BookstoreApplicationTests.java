package org.bookstore;

import org.assertj.core.api.Assertions;
import org.bookstore.common.BookSourceService;
import org.bookstore.common.entities.Book;
import org.bookstore.common.entities.BookDetails;
import org.bookstore.common.entities.BookView;
import org.bookstore.common.patterns.AuthPatterns;
import org.bookstore.web.models.AuthErrors;
import org.bookstore.web.services.BookstoreService;
import org.bookstore.web.services.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookstoreApplicationTests {

    @Autowired
    private UserService userService;


    @Autowired
    private BookstoreService bookstoreService;

    @Autowired
    private BookSourceService bookSourceService;

    @Test
    public void validateAuth() {
        validateAuthErrors(userService.getAuthError("test@test.com", new char[1]), AuthPatterns.INVALID_DOMAIN);
        validateAuthErrors(userService.getAuthError(null, new char[1]), AuthPatterns.USER_EMAIL_IS_NOT_PROVIDED);
        validateAuthErrors(userService.getAuthError("234324test.de", new char[1]), AuthPatterns.INVALID_EMAIL_FORMAT);
        validateAuthErrors(userService.getAuthError("asdfdsf@test.de", new char[0]), AuthPatterns.USER_PASSWORD_IS_NOT_PROVIDED);
    }


    @Test
    public void validateBookSource() {
        List<Book> books = bookSourceService.getBooks();
        List<BookView> bookViews = bookSourceService.getBookViews();
        Assert.assertFalse("List of books is empty", books.isEmpty());
        Assert.assertEquals("List of books is empty", 3, books.size());
        Assert.assertFalse("List of book views is empty", bookViews.isEmpty());
        Assert.assertEquals("List of book views is empty", 6, bookViews.size());
    }

    @Test
    public void validateBookstoreStorage() {
        List<Book> books = bookstoreService.getBooks();
        List<BookView> bookViews = bookstoreService.getBookViews();
        Assert.assertFalse("List of books is empty", books.isEmpty());
        Assert.assertEquals("List of books is empty", 3, books.size());
        Assert.assertFalse("List of book views is empty", bookViews.isEmpty());
        Assert.assertEquals("List of book views is empty", 6, bookViews.size());
    }

    @Test
    public void validateBookViews() {
        List<BookView> booksViews = bookstoreService.getBooksViews("test3@check24.de");
        Assert.assertFalse("List of books view is empty", booksViews.isEmpty());
        Assert.assertEquals("List of books views is not equal to", 2, booksViews.size());
    }

    @Test
    public void validateBockTracing() {
        Optional<BookDetails> bookDetails = bookstoreService.trackBookAndGetDetails("b3", "test3@check24.de");
        Assert.assertTrue("Tracking book details is empty", bookDetails.isPresent());
        Assert.assertEquals("Tracking book details is empty", "b3", bookDetails.get().getBook().getId());
        Assert.assertEquals("List of similar books is not equal to", 2, bookDetails.get().getSimilars().size());
    }

    private void validateAuthErrors(Optional<AuthErrors> authError, String expectedError) {
        Assert.assertTrue("Errors list is empty", authError.isPresent());
        Assert.assertEquals("Errors list is not determined ", 1, authError.get().getErrors().size());
        Assertions.assertThat(authError.get().getErrors().get(0))
                .startsWith(expectedError);
    }
}

