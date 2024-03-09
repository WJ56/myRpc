package com.jia.Serializer;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class HessianSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Hessian2Output ho = new Hessian2Output(baos);
            ho.writeObject(obj);
            ho.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Hessian2Input hi = new Hessian2Input(bais);
            Object obj = hi.readObject();
            hi.close();
            return clazz.cast(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

