package com.gongli.auth.service;


import com.gongli.auth.api.UserClient;
import com.gongli.auth.bean.JwtProperties;
import com.gongli.auth.bean.UserInfo;
import com.gongli.auth.utils.JwtUtils;
import com.gongli.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {
    @Autowired
    UserClient userClient;

    @Autowired
    JwtProperties prop;


    public String loginAuth(String username, String password) {
        try {
            User user = userClient.queryUser(username, password);
            if(user==null){
                return null;
            }
            //待实现
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()),
                    prop.getPrivateKey(), prop.getExpire());
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
