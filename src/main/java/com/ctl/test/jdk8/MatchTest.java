package com.ctl.test.jdk8;

import java.util.ArrayList;
import java.util.List;

public class MatchTest {
    private static class Person {
        public int no;
        private String name;
        private int age;

        public Person(int no, String name, int age) {
            this.no = no;
            this.name = name;
            this.age = age;
        }

        public Person(int no, String name) {
            this.no = no;
            this.name = name;

        }

        public String getName() {
            System.out.println(name);
            return name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static void main(String[] args) {
//        Stream 有三个 match 方法，从语义上说：
//        allMatch：Stream 中全部元素符合传入的 predicate，返回 true
//        anyMatch：Stream 中只要有一个元素符合传入的 predicate，返回 true
//        noneMatch：Stream 中没有一个元素符合传入的 predicate，返回 true
//        它们都不是要遍历全部元素才能返回结果。例如 allMatch 只要一个元素不满足条件，就 skip 剩下的所有元素，返回 false。
        List<Person> persons = new ArrayList();
        persons.add(new Person(1, "name" + 1, 10));
        persons.add(new Person(2, "name" + 2, 21));
        persons.add(new Person(3, "name" + 3, 34));
        persons.add(new Person(4, "name" + 4, 6));
        persons.add(new Person(5, "name" + 5, 55));
        boolean isAllAdult = persons.stream().allMatch(p -> p.getAge() > 18);
        System.out.println("All are adult? " + isAllAdult);
        boolean isThereAnyChild = persons.stream().anyMatch(p -> p.getAge() < 12);
        System.out.println("Any child? " + isThereAnyChild);
        boolean isNoneChild = persons.stream().noneMatch(p -> p.getAge() < 3);
        System.out.println("None child? " + isNoneChild);
    }
}
