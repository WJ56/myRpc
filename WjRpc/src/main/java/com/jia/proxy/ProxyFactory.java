package com.jia.proxy;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.jia.Serializer.JsonSerializer;
import com.jia.common.Invocation;
import com.jia.protocol.NettyClient;
import com.jia.register.NacosRegister;

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
                NettyClient nettyClient = new NettyClient();
                try {
                    Instance instance = NacosRegister.get(interfaceClass.getName());
                    nettyClient.send(instance.getIp(),instance.getPort(), invocation);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });
        return (T) o;
    }
}
