package com.jia.common;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

// 编码器：用于将对象序列化为字节数据
public class KryoEncoder extends MessageToByteEncoder<Object> {

    private final Kryo kryo;

    public KryoEncoder() {
        this.kryo = new Kryo();
        this.kryo.register(java.lang.Class.class);
        this.kryo.register(java.lang.Class[].class);
        this.kryo.register(Object[].class);
        this.kryo.register(com.jia.common.Invocation.class);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        Output output = new Output(4096, -1);
        try {
            kryo.writeClassAndObject(output, msg);
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常堆栈跟踪
            throw e; // 可以选择重新抛出异常，或者进行其他处理
        }
        out.writeBytes(output.toBytes());
    }
}
