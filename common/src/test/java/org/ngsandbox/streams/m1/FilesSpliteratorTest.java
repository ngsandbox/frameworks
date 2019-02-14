package org.ngsandbox.streams.m1;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ngsandbox.streams.m1.model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.ngsandbox.streams.FileUtils.getFilePath;


@Slf4j
public class FilesSpliteratorTest {

    @Test
    public void testPeopleConntentSpliterator() {

        Path path = Paths.get(getFilePath("/streams/m1/people.txt"));
        try (Stream<String> lines = Files.lines(path);) {

            Spliterator<String> lineSpliterator = lines.spliterator();
            Spliterator<Person> peopleSpliterator = new PersonSpliterator(lineSpliterator);

            Stream<Person> people = StreamSupport.stream(peopleSpliterator, false);
            people.forEach(System.out::println);

        } catch (IOException ioe) {
            log.error("Error to process file content", ioe);
        }
    }
}
