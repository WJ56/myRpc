package com.jia;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.jia.protocol.NettyServer;
import com.jia.register.LocalRegister;
import com.jia.register.NacosRegister;

public class Provider {

    public static void main(String[] args) {

        LocalRegister.register(HelloService.class.getName(), HelloServiceImp.class);
        Instance instance = new Instance();
        instance.setIp("localhost");
        instance.setPort(8080);
        NacosRegister.register(HelloService.class.getName(), instance);

        NettyServer nettyServer = new NettyServer();
        nettyServer.start(instance.getIp(), instance.getPort());
    }
}
