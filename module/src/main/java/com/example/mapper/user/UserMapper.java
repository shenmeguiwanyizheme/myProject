package com.example.mapper.user;

import com.example.pojo.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;

@Mapper
public interface UserMapper {
    User getById(BigInteger id);

    @Select("select * from user where id=#{id}")
    User extractById(@Param("id") BigInteger id);

    @Select("select * from user where username =#{username}")
    User extractByUsername(@Param("username") String username);

    int update(@Param("entity") User user);

    int delete(@Param("entity") User user);

    int insert(@Param("entity") User user);


}
