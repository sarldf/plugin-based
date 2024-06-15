package org.example.interf.impl;

import org.example.interf.HelloService;

/**
 * @author fenggz
 * @description
 * @datetime 2024/6/15 22:58
 */
public class CNHelloServiceImpl  implements HelloService {

    /**
     *
     */
    @Override
    public void sayHello() {
        System.out.println("你好");
    }
}
