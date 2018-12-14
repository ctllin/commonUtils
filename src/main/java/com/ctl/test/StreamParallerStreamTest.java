package com.ctl.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title: StreamParallerStreamTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-12-13 17:54
 */
public class StreamParallerStreamTest {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> listOfIntegers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> parallelStorage = Collections.synchronizedList(new ArrayList<>());//Collections.synchronizedList(new ArrayList<>());
        listOfIntegers.parallelStream()
                // Don't do this! It uses a stateful lambda expression.
                .map(e -> {
                    parallelStorage.add(e);
                    return e;
                })
                .forEachOrdered(e -> System.out.print(e + " # "));
        System.out.println();
        parallelStorage.stream().forEachOrdered(e -> System.out.print(e + " * "));//不会有空值
        System.out.println("\n-------------------------------");
        List<Integer> parallelStorage2 = new ArrayList<>();//Collections.synchronizedList(new ArrayList<>());
        listOfIntegers.parallelStream()
                // Don't do this! It uses a stateful lambda expression.
                .map(e -> {
                    parallelStorage2.add(e);
                    return e;
                })
                .forEachOrdered(e -> System.out.print(e + " # "));
        System.out.println();
        parallelStorage2.stream().forEachOrdered(e -> System.out.print(e + " * "));//会有空值

    }
}

//1 # 2 # 3 # 4 # 5 # 6 # 7 # 8 #
//6 * 2 * 3 * 1 * 4 * 5 * 7 * 8 *
//-------------------------------
//1 # 2 # 3 # 4 # 5 # 6 # 7 # 8 #
//null * 3 * 4 * 5 * 2 * 1 * 8 * 7