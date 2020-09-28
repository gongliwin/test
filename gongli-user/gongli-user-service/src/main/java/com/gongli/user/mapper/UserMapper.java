package com.gongli.user.mapper;


import com.gongli.user.pojo.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


@Repository
public interface UserMapper extends Mapper<User> {

}
