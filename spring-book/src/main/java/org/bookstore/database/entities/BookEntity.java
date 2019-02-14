package org.bookstore.database.entities;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.bookstore.common.entities.Book;
import org.bookstore.common.entities.BookView;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.PERSIST;

@Slf4j
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@NamedEntityGraph(name = "BookEntity.views", attributeNodes = @NamedAttributeNode("views"))
public class BookEntity {

    @Id
    private String id;

    private String name;
    private String details;
    private String price;
    private String image;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book", cascade = PERSIST)
    private List<BookViewEntity> views;

    public BookEntity(String id, String name, String details, String price, String image) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.price = price;
        this.image = image;
        views = new ArrayList<>();
    }

    public static BookEntity of(@NonNull Book book) {
        log.trace("Convert to database entity the book {}", book);
        return new BookEntity(book.getId(), book.getName(), book.getDetails(), book.getPrice(), book.getImage());
    }

    public Book toBook() {
        log.trace("Convert to book to database entity {}", this);
        List<BookView> bookViews = views == null ?
                Collections.emptyList() :
                views.stream().map(BookViewEntity::toBookView).collect(Collectors.toList());
        return new Book(id, name, details, price, image, bookViews);
    }
}