package com.ctl.test.jdk8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OneToMoreTest {
    static {oneToOne();}
    public static void oneToOne() {
        List<Integer> nums = Arrays.asList(1, 2, 3, 4);
        List<Integer> squareNums = nums.stream().map(n -> n * n).collect(Collectors.toList());
        //从上面例子可以看出，map 生成的是个 1:1 映射，每个输入元素，都按照规则转换成为另外一个元素。还有一些场景，是一对多映射关系的，这时需要 flatMap。
    }
    public static void main(String[] args) {
        //flatMap 把 input Stream 中的层级结构扁平化，就是将最底层元素抽出来放到一起，最终 output 的新 Stream 里面已经没有 List 了，都是直接的数字。
        Stream<List<Integer>> inputStream = Stream.of(Arrays.asList(1), Arrays.asList(2, 3), Arrays.asList(4, 5, 6));
        Stream<Integer> outputStream = inputStream.flatMap((childList) -> childList.stream());
        System.out.println(Arrays.deepToString(outputStream.toArray()));
    }
    //forEach 不能修改自己包含的本地变量值，也不能用 break/return 之类的关键字提前结束循环。
}
