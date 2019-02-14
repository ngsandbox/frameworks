package org.bookstore.database.entities;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.bookstore.common.entities.BookView;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Slf4j
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BookViewEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "book", nullable = false)
    private String book;

    @Column(name = "user", nullable = false)
    private String user;

    public BookViewEntity(String book, String user) {
        this.book = book;
        this.user = user;
    }

    public static BookViewEntity of(@NonNull BookView bookView) {
        log.trace("Convert to database entity from book view {}", bookView);
        return new BookViewEntity(bookView.getBook(), bookView.getUser());
    }

    public BookView toBookView() {
        log.trace("Convert from database entity to book view {}", this);
        return new BookView(book, user);
    }
}