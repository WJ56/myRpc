package com.jia.Serializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoSerializer implements Serializer {
    private final Kryo kryo = new Kryo();

    @Override
    public byte[] serialize(Object obj) {
        Output output = new Output(4096, -1);
        kryo.writeClassAndObject(output, obj);
        return output.toBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Input input = new Input(bytes);
        return clazz.cast(kryo.readClassAndObject(input));
    }
}
