package com.jia.Serializer;
import com.google.protobuf.MessageLite;
import java.lang.reflect.Method;

public class ProtobufSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        try {
            return ((MessageLite) obj).toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            Method method = clazz.getDeclaredMethod("parseFrom", byte[].class);
            return clazz.cast(method.invoke(null, (Object) bytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

