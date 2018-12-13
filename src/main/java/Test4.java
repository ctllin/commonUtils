/**
 * <p>Title: Test3</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.hanshow.com</p>
 *
 * @author guolin
 * @version 1.0
 * @date 2018-12-01 17:57
 */
public class Test4 {
    public static void main(String[] args) {
        try {
            Class<?> className = Class.forName("Test3");
            System.out.println(className.getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
