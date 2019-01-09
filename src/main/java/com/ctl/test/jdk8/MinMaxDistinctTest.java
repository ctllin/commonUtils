package com.ctl.test.jdk8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MinMaxDistinctTest {
    public static void main(String[] args) throws Exception {
        //min 和 max 的功能也可以通过对 Stream 元素先排序，再 findFirst 来实现，但前者的性能会更好，为 O(n)，
        // 而 sorted 的成本是 O(n log n)。同时它们作为特殊的 reduce 方法被独立出来也是因为求最大最小值是很常见的操作。
        //找出最长一行的长度
        BufferedReader br = new BufferedReader(new FileReader("test.txt"));
        int longest = br.lines().mapToInt(String::length).max().getAsInt();
        br.close();
        System.out.println(longest);
    }
}
