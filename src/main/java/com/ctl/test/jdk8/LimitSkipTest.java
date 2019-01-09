package com.ctl.test.jdk8;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class LimitSkipTest {
    private static class Person {
        public int no;
        private String name;

        public Person(int no, String name) {
            this.no = no;
            this.name = name;
        }

        public String getName() {
            System.out.println(name);
            return name;
        }
    }

    public static void testLimitAndSkip() {
        List<Person> persons = new ArrayList();
        for (int i = 1; i <= 10000; i++) {
            Person person = new Person(i, "name" + i);
            persons.add(person);
        }
        List<String> personList2 = persons.stream().map(Person::getName).limit(10).skip(3).collect(Collectors.toList());
        System.out.println(personList2);
        //这是一个有 10，000 个元素的 Stream，但在 short-circuiting 操作 limit 和 skip 的作用下，管道中 map 操作指定的 getName() 方法的执行次数为 limit 所限定的 10 次，而最终返回结果在跳过前 3 个元素后只有后面 7 个返回。



    }

    public static void testLimitAndSkip2() {
        //有一种情况是 limit/skip 无法达到 short-circuiting 目的的，就是把它们放在 Stream 的排序操作后，
        // 原因跟 sorted 这个 intermediate 操作有关：此时系统并不知道 Stream 排序后的次序如何，
        // 所以 sorted 中的操作看上去就像完全没有被 limit 或者 skip 一样。
        List<Person> persons = new ArrayList();
        for (int i = 1; i <= 5; i++) {
            Person person = new Person(i, "name" + i);
            persons.add(person);
        }
        List<Person> personList2 = persons.stream().sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).limit(2).collect(Collectors.toList());
        System.out.println(personList2);
        //即虽然最后的返回元素数量是 2，但整个管道中的 sorted 表达式执行次数没有像前面例子相应减少。
        //最后有一点需要注意的是，对一个 parallel 的 Steam 管道来说，如果其元素是有序的，那么 limit 操作的成本会比较大，
        // 因为它的返回对象必须是前 n 个也有一样次序的元素。取而代之的策略是取消元素间的次序，或者不要用 parallel Stream。

    }

    public static void main(String[] args) {
        //testLimitAndSkip();
        testLimitAndSkip2();
    }
}
