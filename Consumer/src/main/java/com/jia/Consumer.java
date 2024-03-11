package com.jia;
import com.jia.proxy.ProxyFactory;

public class Consumer {
    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        String res = helloService.sayHello("WangJia");
        System.out.println(res);
    }
}
