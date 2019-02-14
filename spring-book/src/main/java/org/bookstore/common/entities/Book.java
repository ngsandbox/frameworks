package org.bookstore.common.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
public class Book {
    private final String id;
    private String name;
    private String details;
    private String price;
    private String image;
    private List<BookView> bookViews;

    public Book(String id, String name, String details, String price, String image, List<BookView> bookViews) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.price = price;
        this.image = image;
        this.bookViews = bookViews;
    }
}