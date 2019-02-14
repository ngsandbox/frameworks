package org.ngsandbox.streams.m3;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author José
 */
public class ParallelStreams {


    @Test
    public void main() {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "2") ;
        List<String> collect =
                Stream.iterate("+", s -> s + "+")
                .parallel()
                .limit(10)
                .peek(s -> System.out.println(s + " processed in the thread " + Thread.currentThread().getName()))
                // .forEach(s -> strings.add(s));
                .collect(Collectors.toList());
        
        System.out.println("# " + collect.size());
    }
}
