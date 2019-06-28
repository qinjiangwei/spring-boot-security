package com.xueqing.demo.springbootsecurity.config;

import com.xueqing.demo.springbootsecurity.config.util.CacheKeyConversionService;
import com.xueqing.demo.springbootsecurity.config.util.ProtoStuffLZ4Serializer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class MyConfiguration extends CachingConfigurerSupport {

    //重新配序列化器
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        ProtoStuffLZ4Serializer protoStuffLZ4Serializer = new ProtoStuffLZ4Serializer();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setValueSerializer(protoStuffLZ4Serializer);
        template.setHashValueSerializer(protoStuffLZ4Serializer);
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    @Primary
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory){
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        ProtoStuffLZ4Serializer protoStuffLZ4Serializer = new ProtoStuffLZ4Serializer();

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                //定义默认的cache time-to-live
                .entryTtl(Duration.ofSeconds(3600L))
                //禁止缓存null对象
                .disableCachingNullValues()
                //定义可以前缀，避免不同的项目可以冲突
                .computePrefixWith(keyName -> "spring-boot-security".concat(":").concat(keyName))
                //key的序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(protoStuffLZ4Serializer))
                .withConversionService(new CacheKeyConversionService());
        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
    }

    @Primary
    @Bean
    public KeyGenerator keyGenerator(){
        return (target, method, params) -> CacheKeyConversionService.CacheHashCode.of(params);
    }
}
