package com.example.mapper;

import com.example.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    public List<User> queryAll();

    public int add(User user);

    public User queryByName(String name);

    //为了实现update方法，其实是要求数据库中的每一行有一个一定不会变的列，可以说是主键列吧
    //但是也可以采用第二种方法，也就是参数变为两个，第一个是之前的信息，第二个是带更新的信息
    public int update(User user);

    public int delete(User user);


}
