package com.ctl.utils.concurrent;
import com.ctl.utils.bean.Person;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author 		 guolin
 * @tel			 18515287139
 * @date    	 2016-2-19下午9:17:57
 * @package_name com.ctl.util.concurrent
 * @project_name ctlUtils
 * @version 	 version.1.0
 * @description
 */

public class CallableAndFuture {
    public static void main(String[] args) {
        Callable<Integer> callable = new Callable<Integer>() {
            public Integer call() throws Exception {
                return new Random().nextInt(100);
            }
        };
        FutureTask<Integer> future = new FutureTask<Integer>(callable);
        new Thread(future).start();
        try {
           // Thread.sleep(1000);// 可能做一些事情
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //---------------
        Callable<Person> callablePerson = new Callable<Person>() {
            public Person call() throws Exception {
                Person person=new Person();
                person.setId(1);
                person.setAge(27);
                person.setAddress("XX新乡");
                person.setFloatv(1.01f);
                person.setFloatVV(1.02f);
                person.setDoublev(2.01d);
                person.setDoubleVV(2.03d);
                person.setDatenow(new Date());
                Thread.sleep(1000);
                return person;
            }
        };
        FutureTask<Person> futurePerson = new FutureTask<Person>(callablePerson);
        new Thread(futurePerson).start();
        try {
           // Thread.sleep(1000);// 可能做一些事情
            System.out.println(futurePerson.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}