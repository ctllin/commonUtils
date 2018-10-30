
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
    public static void main(String[] args) {
        System.out.println(Test.class.getClassLoader().getResource("").getPath());
    }
}
