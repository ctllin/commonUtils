package com.ctl.test;

import com.ctl.test.po.Person;
import com.ctl.test.po.Student;
import net.sf.json.JSONObject;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * <p>Title: Singleton</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-12-05 10:03
 */
public final class Singleton {
    private String uuid;
   // private volatile static Singleton instalce = null;
    private  static Singleton instalce = null;
    private Person person;
    private Student student;

    private Singleton() {
        this.uuid= UUID.randomUUID().toString();
        this.person = new Person(1, "ctl");
        this.student = new Student(27, "xx");
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public static Singleton getInstalce(){
        if(null==instalce){
            synchronized (Singleton.class){
                if(null==instalce){
                    instalce = new Singleton();
                }
            }
        }
        return instalce;
    }

    public static void main(String[] args) {
        IntStream.range(0, 10).boxed().forEach(integer -> new Thread(() -> System.out.println(Singleton.getInstalce().getUuid() + "\t"+JSONObject.fromObject(Singleton.getInstalce().getPerson()) + "\t" + JSONObject.fromObject(Singleton.getInstalce().getStudent()))).start());
    }
}

//{"id":1,"name":"ctl"}	{"address":"xx","age":27}
//{"id":1,"name":"ctl"}	{"address":"xx","age":27}
//{"id":1,"name":"ctl"}	{"address":"xx","age":27}
//{"id":1,"name":"ctl"}	{"address":"xx","age":27}
//{"id":1,"name":"ctl"}	{"address":"xx","age":27}
//{"id":1,"name":"ctl"}	{"address":"xx","age":27}
//{"id":1,"name":"ctl"}	{"address":"xx","age":27}
//{"id":1,"name":"ctl"}	{"address":"xx","age":27}
//{"id":1,"name":"ctl"}	{"address":"xx","age":27}
//{"id":1,"name":"ctl"}	{"address":"xx","age":27}

//{"id":1,"name":"ctl"}	{"address":"","age":0}
//{"id":1,"name":"ctl"}	{"address":"","age":0}
//{"id":1,"name":"ctl"}	{"address":"","age":0}
//{"id":1,"name":"ctl"}	{"address":"","age":0}
//{"id":1,"name":"ctl"}	{"address":"","age":0}
//{"id":1,"name":"ctl"}	{"address":"","age":0}
//{"id":1,"name":"ctl"}	{"address":"","age":0}
//{"id":1,"name":"ctl"}	{"address":"","age":0}
//{"id":1,"name":"ctl"}	{"address":"","age":0}
//{"id":1,"name":"ctl"}	{"address":"","age":0}