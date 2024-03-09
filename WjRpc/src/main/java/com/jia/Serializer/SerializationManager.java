package com.jia.Serializer;

public class SerializationManager {
    private static final Serializer DEFAULT_SERIALIZER = new KryoSerializer();

    public static Serializer getSerializer(SerializationType type) {
        switch (type) {
            case JSON:
                return new JsonSerializer();
            case KRYO:
                return new KryoSerializer();
            case HESSIAN:
                return new HessianSerializer();
            case PROTOBUF:
                return new ProtobufSerializer();
            default:
                return DEFAULT_SERIALIZER;
        }
    }
}
