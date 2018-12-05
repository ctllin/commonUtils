
/**
 * <p>Title: Test</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-08-14 21:15
 */
public class Test {
    private static Test test = new Test();//A
    private static int x = 0;//B
    private static int y;//C

    private Test() {
        x++;
        y++;
    }

    public static Test getTest() {
        return test;
    }


    public static void main(String[] args) {
        Test test = Test.getTest();
        System.out.println(test.x);//0
        System.out.println(test.y);//1
        System.out.println("1"+Test.class.getClassLoader());
        System.out.println("1"+Test.class.getClassLoader().getParent());
        System.out.println("1"+Test.class.getClassLoader().getParent().getParent());
    }
}

class Test2 {
    private static int x = 0;//B
    private static int y;//C
    private static Test2 test = new Test2();//A

    private Test2() {
        x++;
        y++;
    }

    public static Test2 getTest2() {
        return test;
    }

    public static void main(String[] args) {
        Test2 test2 = Test2.getTest2();
        System.out.println(test2.x);//1
        System.out.println(test2.y);//1
    }
}
