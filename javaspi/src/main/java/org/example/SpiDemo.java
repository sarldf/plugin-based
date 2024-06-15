package org.example;

import org.example.interf.HelloService;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author fenggz
 * @description
 * @datetime 2024/6/15 22:54
 */
public class SpiDemo {
    public static void main(String[] args) {
        ServiceLoader<HelloService> serviceLoader = ServiceLoader.load(HelloService.class);
        Iterator<HelloService> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            HelloService helloService = iterator.next();
            helloService.sayHello();
        }
    }
}
