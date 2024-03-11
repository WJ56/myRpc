package com.jia.protocol;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

public class KryoCode {

    public static class KryoEncoder extends MessageToByteEncoder<Object> {
        private final Kryo kryo = new Kryo();

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            Output output = new Output(out.array(), out.writerIndex());
            try {
                kryo.writeObject(output, msg);
            } finally {
                output.close();
            }

            int length = output.position();
            out.writerIndex(out.writerIndex() + length);
        }
    }

    public static class KryoDecoder extends ByteToMessageDecoder {
        private final Kryo kryo = new Kryo();
        private final Input input = new Input();

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            if (in.readableBytes() < 4) {
                return;
            }

            in.markReaderIndex();
            int length = in.readInt();
            if (in.readableBytes() < length) {
                in.resetReaderIndex();
                return;
            }

            byte[] bytes = new byte[length];
            in.readBytes(bytes);

            input.setBuffer(bytes, 0, length);
            Object obj = kryo.readObject(input, Object.class);
            out.add(obj);
        }
    }
}
