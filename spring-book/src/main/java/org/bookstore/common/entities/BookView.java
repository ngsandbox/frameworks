package org.bookstore.common.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = {"book", "user"})
@ToString
public class BookView {
    private final String book;
    private final String user;
}