package org.bookstore.common;

import org.bookstore.common.entities.Book;
import org.bookstore.common.entities.BookView;

import java.util.List;

public interface BookSourceService {
    List<Book> getBooks();

    List<BookView> getBookViews();
}
