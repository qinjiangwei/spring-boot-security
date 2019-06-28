package com.xueqing.demo.springbootsecurity;

import com.xueqing.demo.springbootsecurity.bean.User;
import com.xueqing.demo.springbootsecurity.cache.RedisService;
import com.xueqing.demo.springbootsecurity.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootSecurityApplicationTests {
    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
        List<User> allUsers = userService.getAllUsers();
        System.out.println(allUsers.size());

    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisService redisService;

    @Test
    public void testRedis() throws Exception {
        boolean set = redisService.set("name", "hello world");
        if(set){
            System.out.println("success");
        }
    }

    @Test
    public void testGet() throws Exception {
        String name = (String) redisService.get("name");
        System.out.println(name);
    }


    @Test
    public void testFindUsers() throws Exception {
        List<User> allUsers = userService.getAllUsers();
        System.out.println(allUsers.size());
    }




}
