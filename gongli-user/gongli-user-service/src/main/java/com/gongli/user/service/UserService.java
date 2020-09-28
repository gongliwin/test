package com.gongli.user.service;


import com.gongli.user.mapper.UserMapper;
import com.gongli.user.pojo.User;
import com.gongli.utils.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    static final String KEY_PREFIX = "user:code:phone:";

    static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public Boolean checkData(String data, Integer type) {
        User user = new User();
        switch (type){
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                return null;
        }

        return userMapper.selectCount(user)==0;

    }

    public Boolean sendVerifyCode(String phone) {
        Map<String, String> map = new HashMap<>();
        String code = NumberUtils.generateCode(6);
        map.put("phone",phone);
        map.put("code",code);
        try {
            amqpTemplate.convertAndSend("gongli.sms.exchange","sms.verify.code",map);
            stringRedisTemplate.opsForValue().set(KEY_PREFIX+phone,code,5, TimeUnit.MINUTES);
            return true;
        } catch (AmqpException e) {
            e.printStackTrace();
            return false;
        }

    }

    public Boolean register(User user, String code) {
        String key = KEY_PREFIX + user.getPhone();
        // 从redis取出验证码
        String codeCache = stringRedisTemplate.opsForValue().get(key);
        // 检查验证码是否正确
        if (!code.equals(codeCache)) {
            // 不正确，返回
            return false;
        }
        //生产一个盐值，给密码加密。
        //todo

        user.setId(null);
        user.setCreated(new Date());

        Boolean b = userMapper.insertSelective(user)==1;

        if(b){
            stringRedisTemplate.delete(key);
        }

        return b;

    }

    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        // 校验用户名
        if (user == null) {
            return null;
        }
//        // 校验密码
//        if (!user.getPassword().equals(CodecUtils.md5Hex(password, user.getSalt()))) {
//            return null;
//        }
        // 用户名密码都正确
        return user;


    }
}
