package com.ctl.test.po;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title: Student</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-12-05 10:21
 */
public class Student{
    public Student() {
    }

    public Student(int age, String address) {
        Random random = new Random();
        int nextInt = random.nextInt(1000);
        try {
            TimeUnit.MILLISECONDS.sleep(nextInt);
            this.age = age;
            this.address = address;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        Thread thread = new Thread(() -> {
//            try {
//                TimeUnit.MILLISECONDS.sleep(nextInt);
//                this.age = age;
//                this.address = address;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        thread.setDaemon(true);
//        thread.start();
    }

    private int age;
    private String address;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

