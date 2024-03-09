package com.jia;

import com.jia.protocol.NettyServer;
import com.jia.register.LocalRegister;

public class Provider {

    public static void main(String[] args) {

        LocalRegister.register(HelloService.class.getName(), HelloServiceImp.class);

        NettyServer nettyServer = new NettyServer();
        nettyServer.start("localhost", 8080);
    }
}
