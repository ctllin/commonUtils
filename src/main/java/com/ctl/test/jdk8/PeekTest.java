package com.ctl.test.jdk8;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PeekTest {
    // peek 对每个元素执行操作并返回一个新的 Stream
    //peek接收一个没有返回值的λ表达式，可以做一些输出，外部处理等。map接收一个有返回值的λ表达式，之后Stream的泛型类型将转换为map参数λ表达式返回的类型
    public static void main(String[] args) {
        Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList());
    }
}
