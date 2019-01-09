package com.ctl.test.jdk8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class StreamTest1 {
    public static void main(String[] args) {
        Stream<List<Integer>> inputStream = Stream.of(Arrays.asList(1), Arrays.asList(2, 3), Arrays.asList(4, 5, 6));
        Stream<Integer> outputStream = inputStream.flatMap((childList) -> childList.stream());
        List<Integer> collect = outputStream.collect(toList());
        System.out.println(collect);
    }
}
