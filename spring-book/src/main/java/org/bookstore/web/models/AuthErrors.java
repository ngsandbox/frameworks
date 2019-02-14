package org.bookstore.web.models;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "email")
@ToString
public class AuthErrors {

    private final String email;

    private final char[] pwd;

    @NonNull
    private final List<String> errors;
}
