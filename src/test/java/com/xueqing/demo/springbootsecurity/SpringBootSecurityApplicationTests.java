package com.xueqing.demo.springbootsecurity;

import com.xueqing.demo.springbootsecurity.bean.User;
import com.xueqing.demo.springbootsecurity.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

}
