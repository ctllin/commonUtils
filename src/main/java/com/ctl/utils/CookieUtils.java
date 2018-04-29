package com.ctl.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Package: com.ctl.util
 * @Title: Cookie.java Create on 2014-5-15 
 * @Description:
 *  
 *  Cookie\u5DE5\u5177\u7C7B,\u5C01\u88C5Cookie\u5E38\u7528\u64CD\u4F5C
 *  
 * @author ctl
 * @version v 0.1
 */
public class CookieUtils {
    /**
     * \u8BBE\u7F6Ecookie\u6709\u6548\u671F\uFF0C\u6839\u636E\u9700\u8981\u81EA\u5B9A\u4E49[\u672C\u7CFB\u7EDF\u8BBE\u7F6E\u4E3A30\u5929]
     */
    private final static int COOKIE_MAX_AGE = 1000 * 60 * 60 * 24 * 30;

    /**
     *
     * @desc \u5220\u9664\u6307\u5B9ACookie
     * @param response
     * @param cookie
     */
    public static void removeCookie(HttpServletResponse response, Cookie cookie)
    {
            if (cookie != null)
            {
                    cookie.setPath("/");
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
            }
    }

    /**
     *
     * @desc \u5220\u9664\u6307\u5B9ACookie
     * @param response
     * @param cookie
     * @param domain
     */
    public static void removeCookie(HttpServletResponse response, Cookie cookie,String domain)
    {
            if (cookie != null)
            {
                    cookie.setPath("/");
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setDomain(domain);
                    response.addCookie(cookie);
            }
    }

    /**
     *
     * @desc \u6839\u636ECookie\u540D\u79F0\u5F97\u5230Cookie\u7684\u503C\uFF0C\u6CA1\u6709\u8FD4\u56DENull
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name)
    {
            Cookie cookie = getCookie(request, name);
            if (cookie != null)
            {
                    return cookie.getValue();
            }
            else
            {
                    return null;
            }
    }

    /**
     *
     * @desc \u6839\u636ECookie\u540D\u79F0\u5F97\u5230Cookie\u5BF9\u8C61\uFF0C\u4E0D\u5B58\u5728\u8BE5\u5BF9\u8C61\u5219\u8FD4\u56DENull
     * @param request
     * @param name
     */
    public static Cookie getCookie(HttpServletRequest request, String name)
    {
            Cookie cookies[] = request.getCookies();
            if (cookies == null || name == null || name.length() == 0)
                    return null;
            Cookie cookie = null;
            for (int i = 0; i < cookies.length; i++)
            {
                    if (!cookies[i].getName().equals(name))
                            continue;
                    cookie = cookies[i];
                    if (request.getServerName().equals(cookie.getDomain()))
                            break;
            }

            return cookie;
    }

    /**
     *
     * @desc \u6DFB\u52A0\u4E00\u6761\u65B0\u7684Cookie\u4FE1\u606F\uFF0C\u9ED8\u8BA4\u6709\u6548\u65F6\u95F4\u4E3A\u4E00\u4E2A\u6708
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(HttpServletResponse response, String name, String value)
    {
            setCookie(response, name, value, COOKIE_MAX_AGE);
    }

    /**
     *
     * @desc \u6DFB\u52A0\u4E00\u6761\u65B0\u7684Cookie\u4FE1\u606F\uFF0C\u53EF\u4EE5\u8BBE\u7F6E\u5176\u6700\u957F\u6709\u6548\u65F6\u95F4(\u5355\u4F4D\uFF1A\u79D2)
     * @param response
     * @param name
     * @param value
     * @param maxAge
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge)
    {
            if (value == null)
                    value = "";
            Cookie cookie = new Cookie(name, value);
            if(maxAge!=0){
            	cookie.setMaxAge(maxAge);
            }else{
            	cookie.setMaxAge(COOKIE_MAX_AGE);
            }
            cookie.setPath("/");
            response.addCookie(cookie);
    }
}