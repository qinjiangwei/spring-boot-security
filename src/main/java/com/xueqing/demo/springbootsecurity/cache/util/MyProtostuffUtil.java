package com.xueqing.demo.springbootsecurity.cache.util;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyProtostuffUtil {

    /** 包装类Schema对象 */
    private static final Schema<Wrapper> WRAPPER_SCHEMA = RuntimeSchema.createFrom(Wrapper.class);

    /** 缓存对象Schema */
    private static final Map<Class<?>, Schema<?>> SCHEMA_CACHED_MAP = new ConcurrentHashMap<>();


    private static class Wrapper {
        private Object data;
    }

    /**
     * 任意对象序列化成字节数组
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T obj){
        byte[] data;
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);//LinkedBuffer.allocate是缓存器，当压缩的对象太大是，起到缓冲的作用，该方法是protobuff自带的。
        try {
            Wrapper wrapper = new Wrapper();
            wrapper.data = obj;
            data = ProtostuffIOUtil.toByteArray(wrapper, WRAPPER_SCHEMA, buffer);
        } finally {
            buffer.clear();
        }
        return data;
    }

    /**
     * 字节数组反序列化成任意对象
     * @param bytes
     * @param <T>
     * @return
     */
    public static <T>T deserialize(byte[] bytes){
        Wrapper newMessage = WRAPPER_SCHEMA.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, newMessage, WRAPPER_SCHEMA);
        return (T) newMessage.data;
    }

    /**
     * pojo对象序列化，不使用包装类，序列化过后体积更小，不适用集合类，Object
     * @param pojo
     * @param <T>
     * @return
     */
    public static <T> byte[] serializePOJO (T pojo){
        byte[] data;
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Class<T> clazz = (Class<T>) pojo.getClass();
            data = ProtostuffIOUtil.toByteArray(pojo, getSchema(clazz), buffer);
        } finally {
            buffer.clear();
        }
        return data;
    }

    /**
     * 字节数组反序列化成pojo对象
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T>T deserialize(byte[] data, Class<T> clazz){
        Schema<T> schema = getSchema(clazz);
        T message = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, message, schema);
        return message;
    }




    public static <T> Schema<T> getSchema(Class<T> clazz){
        Schema<T> schema = (Schema<T>) SCHEMA_CACHED_MAP.get(clazz);
        if(schema == null){
            schema = RuntimeSchema.getSchema(clazz);
            if(schema != null){
                SCHEMA_CACHED_MAP.put(clazz, schema);
            }
        }
        return schema;
    }







}


