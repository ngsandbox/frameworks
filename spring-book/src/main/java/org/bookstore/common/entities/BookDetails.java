package org.bookstore.common.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@EqualsAndHashCode(of = "book")
@ToString
public class BookDetails {

    private final String user;

    private final Book book;

    private final List<Book> similars;

    public BookDetails(String user, Book book, List<Book> similars) {
        this.user = user;
        this.book = book;
        this.similars = similars;
    }
}
