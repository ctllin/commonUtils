package com.ctl.web.dubbo.service.imp;

import com.ctl.web.dubbo.service.HelloService;

public class HelloImp implements HelloService{

    @Override
    public String say(String name) {
        return "HelloImp say:"+name;
    }
}
