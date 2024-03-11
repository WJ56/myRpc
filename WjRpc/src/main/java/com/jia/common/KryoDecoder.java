package com.jia.common;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteBuffer;
import java.util.List;

// 解码器：用于将字节数据反序列化为对象
public class KryoDecoder extends ByteToMessageDecoder {

    private final Kryo kryo;

    public KryoDecoder() {
        this.kryo = new Kryo();
        this.kryo.register(java.lang.Class.class);
        this.kryo.register(java.lang.Class[].class);
        this.kryo.register(Object[].class);
        this.kryo.register(com.jia.common.Invocation.class);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        // 假设你知道要读取的对象的大小，这里用整个msg的长度作为示例
        // 实际应用中，你可能需要根据协议来读取对象的长度
        int length = msg.readableBytes();
        byte[] objectBytes = new byte[length];

        // 读取ByteBuf中的数据到字节数组
        msg.readBytes(objectBytes);

        // 创建Input对象，传入读取到的字节数组
        Input input = new Input(new ByteBufferBackedInputStream(ByteBuffer.wrap(objectBytes)));

        // 使用Kryo进行反序列化
        Object o = kryo.readClassAndObject(input);

        // 将解码后的对象添加到输出列表
        out.add(o);
    }
}
