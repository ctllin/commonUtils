package com.ctl.test.jdk8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamCreateTest {
    public static void main(String[] args) {
        // 1. Individual values
         Stream stream = Stream.of("a", "b", "c");
        // 2. Arrays
         String [] strArray = new String[] {"a", "b", "c"};
         stream = Stream.of(strArray);
         stream = Arrays.stream(strArray);
        // 3. Collections
         List<String> list = Arrays.asList(strArray);
         stream = list.stream();
    }
}
