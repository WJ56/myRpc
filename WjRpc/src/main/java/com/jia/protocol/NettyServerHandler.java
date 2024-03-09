package com.jia.protocol;

import com.jia.common.*;
import com.jia.register.LocalRegister;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NettyServerHandler {
    public void handler(ChannelHandlerContext ctx, Invocation invocation) {
        Class classImp = LocalRegister.get(invocation.getInterfaceName());
        System.out.println("classImp: " + classImp.getName());
        try {
            Method method = classImp.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            String res = (String) method.invoke(classImp.getDeclaredConstructor().newInstance(), invocation.getParameters());
            ctx.writeAndFlush(res); // 回写数据给客户端
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
