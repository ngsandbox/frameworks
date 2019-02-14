package org.ngsandbox.streams.m6.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(of = {"lastName", "firstName"})
@ToString
@Getter
public class Actor {
    private String lastName;
    private String firstName;

    public Actor(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

}
