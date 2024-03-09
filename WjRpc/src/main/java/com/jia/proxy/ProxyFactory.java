package com.jia.proxy;

import com.jia.Serializer.JsonSerializer;
import com.jia.common.Invocation;
import com.jia.protocol.NettyClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactory {
    public static <T> T getProxy(Class interfaceClass){
        Object o = Proxy.newProxyInstance(ProxyFactory.class.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Invocation invocation = new Invocation(interfaceClass.getName(), method.getName(),
                        method.getParameterTypes(), args);
                JsonSerializer jsonSerializer = new JsonSerializer();
                byte[] serialize = jsonSerializer.serialize(invocation);
                NettyClient nettyClient = new NettyClient();
                try {
                    nettyClient.send("localhost",8080, serialize);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });
        return (T) o;
    }
}
