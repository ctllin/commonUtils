package com.ctl.test.jdk8;

import java.util.Arrays;
import java.util.stream.Stream;

public class FilterTest {
    //filter 对原始 Stream 进行某项测试，通过测试的元素被留下来生成一个新 Stream。
    public static void main(String[] args) {
        Integer[] sixNums = {1, 2, 3, 4, 5, 6};
        Integer[] evens = Stream.of(sixNums).filter(n -> n % 2 == 0).toArray(Integer[]::new);
        System.out.println(Arrays.deepToString(evens));
    }
}
