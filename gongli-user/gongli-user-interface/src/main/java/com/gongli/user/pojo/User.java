package com.gongli.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   Long id;
    @Length(min = 4,max = 20,message = "用户名只能在4~30位之间")
   String username;
   @JsonIgnore
   String password;
   @Pattern(regexp = "^1[35678]\\d{9}$",message = "手机号格式不正确")
   String phone;
   Date created;
   @JsonIgnore
   String salt;

}
