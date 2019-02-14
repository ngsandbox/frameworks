package org.ngsandbox.streams.m6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.ngsandbox.streams.m6.model.Actor;
import org.ngsandbox.streams.m6.model.Movie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.ngsandbox.streams.FileUtils.getFilePath;

public class MainMoviesActors {

    @Test
    public void testMoviesProcessor() throws IOException {

        Stream<String> lines = Files.lines(Paths.get(getFilePath("/streams/m6/movies-mpaa.txt")));

        Set<Movie> movies = lines
                .map(this::processLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        System.out.println("# movies = " + movies.size());

        // # of actors
        long numberOfActors = movies.stream()
                .flatMap(movie -> movie.getActors().stream()) // Stream<Actors>
                .distinct()
                .count();

        System.out.println("# of actors : " + numberOfActors);

        // # of actors that played in the greatest # of movies
        // Map<Actor, Long> collect =
        Map.Entry<Actor, Long> mostViewedActor =
                movies.stream()
                        .flatMap(movie -> movie.getActors().stream()) // Stream<Actors>
                        .collect(
                                Collectors.groupingBy(
                                        Function.identity(),
                                        Collectors.counting()
                                )
                        )
                        .entrySet().stream() // Stream<Map.Entry<Actor, Long>>
                        .max(
                                // Comparator.comparing(entry -> entry.getValue())
                                Map.Entry.comparingByValue()
                        )
                        .get();
        System.out.println("Most viewed actor : " + mostViewedActor);

        // actor that played in the greatest # of movies during a year
        // Map<release years, Map<Actor, # of movies during that year>>
        // Map<Integer, HashMap<Actor, AtomicLong>> collect =
        Entry<Integer, Entry<Actor, AtomicLong>> get =
                movies.stream()
                        .collect(
                                Collectors.groupingBy(
                                        Movie::getReleaseYear,
                                        Collector.of(
                                                (Supplier<HashMap<Actor, AtomicLong>>) HashMap::new,// supplier
                                                (map, movie) -> movie.getActors().forEach(
                                                        actor -> map.computeIfAbsent(actor, a -> new AtomicLong()).incrementAndGet()
                                                ), // accumulator
                                                (map1, map2) -> {
                                                    map2.forEach((key, value) -> map1.merge(
                                                            key, value,
                                                            (al1, al2) -> {
                                                                al1.addAndGet(al2.get());
                                                                return al1;
                                                            }
                                                    ));
                                                    return map1;
                                                }, // combiner
                                                Collector.Characteristics.IDENTITY_FINISH
                                        )
                                )
                        ) // Map<Integer, HashMap<Actor, AtomicLong>>
                        .entrySet().stream()
                        .collect(
                                Collectors.toMap(
                                        Entry::getKey,
                                        entry -> entry.getValue().entrySet().stream()
                                                .max(
                                                        Map.Entry.comparingByValue(Comparator.comparing(AtomicLong::get))
                                                )
                                                .get()
                                )
                        ) // Map<Integer, Map.Entry<Actor, AtomicLong>>
                        .entrySet().stream()
                        .max(
                                Map.Entry.comparingByValue(
                                        Comparator.comparing(
                                                entry -> entry.getValue().get()
                                        )
                                )
                        )
                        .get();
        // Map.Entry<Integer, Map.Entry<Actor, AtomicLong>>
        System.out.println(get);

    }

    private Optional<Movie> processLine(String line) {
        String[] elements = line.split("/");
        Assertions.assertTrue(elements.length > 1, "The line does not contains /. Line: " + line);
        String title = elements[0].substring(0, elements[0].lastIndexOf("(")).trim();
        String releaseYear = elements[0].substring(elements[0].lastIndexOf("(") + 1, elements[0].lastIndexOf(")"));

        if (releaseYear.contains(",")) {
            // with skip movies with a coma in their title
            return Optional.empty();
        }

        Movie movie = new Movie(title, Integer.valueOf(releaseYear));

        for (int i = 1; i < elements.length; i++) {
            String[] name = elements[i].split(", ");
            String lastName = name[0].trim();
            String firstName = "";
            if (name.length > 1) {
                firstName = name[1].trim();
            }

            Actor actor = new Actor(lastName, firstName);
            movie.addActor(actor);
        }

        return Optional.of(movie);
    }
}
