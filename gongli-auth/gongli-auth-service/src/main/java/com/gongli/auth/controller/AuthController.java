package com.gongli.auth.controller;


import com.gongli.auth.bean.JwtProperties;
import com.gongli.auth.bean.UserInfo;
import com.gongli.auth.service.AuthService;
import com.gongli.auth.utils.JwtUtils;
import com.gongli.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("auth")
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    JwtProperties prop;

    @Autowired
    AuthService authService;


    @PostMapping("login")
    public ResponseEntity<Void> loginAuth(@RequestParam("username") String username,@RequestParam("password") String password
    ,HttpServletRequest request,HttpServletResponse response
    ){
        String token=authService.loginAuth(username,password);
        if(StringUtils.isBlank(token)){
            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
        }

        CookieUtils.setCookie(request,response,prop.getCookieName(),token,prop.getCookieMaxAge(),true);

        return ResponseEntity.ok().build();

    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue("GL_TOKEN")String token
            ,HttpServletRequest request,HttpServletResponse response){
        try {
            // 从token中解析token信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            // 解析成功要重新刷新token
            token = JwtUtils.generateToken(userInfo, prop.getPrivateKey(), prop.getExpire());
            // 更新cookie中的token
            CookieUtils.setCookie(request, response, prop.getCookieName(), token, prop.getCookieMaxAge(),true);
            // 解析成功返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 出现异常则，响应500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
