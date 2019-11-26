package com.xueqing.demo.springbootsecurity.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.xueqing.demo.springbootsecurity.bean.R;
import com.xueqing.demo.springbootsecurity.bean.User;
import com.xueqing.demo.springbootsecurity.util.ConstantVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager myAuthenticationManager;

    @Autowired
    DefaultKaptcha defaultKaptcha;

    /**
     * 登录方法
     * @param username
     * @param password
     * @param kaptcha
     * @param session
     * @return
     */
    @RequestMapping(value = "/userLogin")
    public String userLogin(String username, String password, String kaptcha,HttpSession session) {

        User userInfo = new User();
        userInfo.setUsername(username);
        userInfo.setPassword(password);






        Object attribute = session.getAttribute(ConstantVal.CHECK_CODE);
        if(attribute != null){
            String s = attribute.toString();
            System.out.println("验证码验证通过");
            if (StringUtils.isEmpty(kaptcha) || !s.equals(kaptcha)) {
                return "redirect:login-error?error=1";
            }
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);


        try{
            //使用SpringSecurity拦截登陆请求 进行认证和授权
            Authentication authenticate = myAuthenticationManager.authenticate(usernamePasswordAuthenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authenticate);
            //使用redis session共享
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext()); // 这个非常重要，否则验证后将无法登陆
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:login-error?error=2";
        }


        return "redirect:index";
    }


    @RequestMapping("/captcha.jpg")
    @ResponseBody
    public R applyCheckCode(HttpServletResponse response, HttpSession session) throws IOException {
        R r = new R();
        //生成文字验证码
        String text = defaultKaptcha.createText();
        //生成图片验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        //保存到session
        session.setAttribute(ConstantVal.CHECK_CODE, text);
        response.setContentType("image/jpeg");
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        return r;
    }
}
