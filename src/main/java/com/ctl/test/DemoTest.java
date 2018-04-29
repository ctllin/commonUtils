package com.ctl.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * <p>Title: DemoTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: www.ctl.com</p>
 *
 * @author ctl
 * @version 1.0
 * @date 2018-04-26 14:23
 */
public class DemoTest {
    public static void main(String[] args) {
        try {
            System.out.println(URLEncoder.encode("轻盈购","utf-8"));
            System.out.println(URLDecoder.decode("%E8%BD%BB%E7%9B%88%E8%B4%AD","utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
