package com.xueqing.demo.springbootsecurity.config.util;

import com.xueqing.demo.springbootsecurity.util.LZ4CompressUtil;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class ProtoStuffLZ4Serializer<T> implements RedisSerializer<T> {
    @Override
    public byte[] serialize(T t) throws SerializationException {
        byte[] bytes = new byte[]{};
        if(t == null){
            return bytes;
        }
        try {
            bytes = MyProtostuffUtil.serialize(t);
            return LZ4CompressUtil.lz4Compress(bytes, 2048);
        } catch (Exception e) {
            throw new SerializationException("Protostuff 序列化异常"+ e.getMessage(), e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if(bytes == null){
            return null;
        }
        try {
            bytes = LZ4CompressUtil.lz4Decompress(bytes, 2048);
            return (T)MyProtostuffUtil.deserialize(bytes);
        } catch (Exception e) {
            throw new SerializationException("Protostuff 反序列化异常" + e.getMessage(), e);
        }
    }
}
