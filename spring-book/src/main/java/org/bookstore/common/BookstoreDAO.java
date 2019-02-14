package org.bookstore.common;

import lombok.NonNull;
import org.bookstore.common.entities.Book;
import org.bookstore.common.entities.BookView;

import java.util.List;
import java.util.Optional;

public interface BookstoreDAO {

    void saveBooks(@NonNull List<Book> books);

    void saveBookViews(@NonNull List<BookView> bookViews);

    List<Book> getBooks();

    Optional<Book> trackBook(String book, String email);

    List<BookView> getBooksView(String email);

    List<BookView> getBooksView();

    List<Book> findBooksThrowViewers(String user, String bookId);
}
