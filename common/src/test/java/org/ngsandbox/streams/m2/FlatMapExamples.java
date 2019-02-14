package org.ngsandbox.streams.m2;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author José
 */
public class FlatMapExamples {

    @Test
    public void testResources() throws IOException {
        // http://introcs.cs.princeton.edu/java/data/TomSawyer.txt

        Stream<String> stream1 = Files.lines(Paths.get(getFilePath("/streams/m2/TomSawyer_01.txt")));
        Stream<String> stream2 = Files.lines(Paths.get(getFilePath("/streams/m2/TomSawyer_02.txt")));
        Stream<String> stream3 = Files.lines(Paths.get(getFilePath("/streams/m2/TomSawyer_03.txt")));
        Stream<String> stream4 = Files.lines(Paths.get(getFilePath("/streams/m2/TomSawyer_04.txt")));

        Stream<Stream<String>> streamOfStreams =
                Stream.of(stream1, stream2, stream3, stream4);

        //System.out.println("# streams: " + streamOfStreams.count());
        Stream<String> streamOfLines = streamOfStreams.flatMap(Function.identity());

        //System.out.println("# lines " + streamOfLines.count());

        Function<String, Stream<String>> lineSplitter =
                line -> Pattern.compile(" ").splitAsStream(line);

        Stream<String> streamOfWords =
                streamOfLines.flatMap(lineSplitter)
                        .map(String::toLowerCase)
                        .filter(word -> word.length() == 4)
                        .distinct();

        System.out.println("# words :" + streamOfWords.count());
    }

    private String getFilePath(String innerPath) {
        URL resource = getClass().getResource(innerPath);
        Assert.assertNotNull("The file was not found " + innerPath, resource);
        return resource.getFile();
    }
}
