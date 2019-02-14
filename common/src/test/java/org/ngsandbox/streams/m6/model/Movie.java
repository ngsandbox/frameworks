package org.ngsandbox.streams.m6.model;

import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
public class Movie {
    private String title;
    private int releaseYear;

    private Set<Actor> actors = new HashSet<>();

    public Movie(String title, int releaseYear) {
        this.title = title;
        this.releaseYear = releaseYear;
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }
}
