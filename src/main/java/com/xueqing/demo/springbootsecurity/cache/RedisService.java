package com.xueqing.demo.springbootsecurity.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value){
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *写入缓存
     * @param key
     * @param value
     * @param expireTime 超时时间
     * @return
     */
    public boolean set(final String key, Object value, long expireTime){
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 取缓存
     * @param key
     * @return
     */
    public Object get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除key
     * @param key
     */
    public void deleteKey(final String key){
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey != null && hasKey) {
            redisTemplate.delete(key);
        }
    }

    public void batchDelete(final String... keys){
         redisTemplate.delete(Arrays.asList(keys));
    }


}
