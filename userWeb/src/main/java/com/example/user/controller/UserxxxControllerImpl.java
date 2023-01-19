package com.example.user.controller;

import com.example.mapper.UserMapper;
import com.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")//一般写在方法上
public class UserxxxControllerImpl implements UserxxxController {//不需要写接口...  如果要写那么要写成BaseController
    @Autowired
    UserMapper userMapper;

    @RequestMapping("/queryByName")
    public User getUserByName(@RequestParam("name") String name) {
        return userMapper.queryByName(name);
    }

    @RequestMapping("queryAll")

    public String f() {
        List<User> users = userMapper.queryAll();
        System.out.println(users);
        String s = "";
        for (User u : users) {
            s += u.toString() + "    ";
        }
        System.out.println(s);
        return "hello successfully invoked" + "==============" + s;
    }

    @RequestMapping("insert")
    public String g(@RequestParam Integer age, @RequestParam String name) {
        User u = new User(age, name);
        userMapper.add(u);
        List<User> users = userMapper.queryAll();
        System.out.println(users);
        String s = "";
        for (User user : users) {
            s += user.toString() + "    ";
        }
        System.out.println(s);
        return s;

    }

    @RequestMapping("delete")
    public String l(@RequestParam(value = "id", required = true) Integer id) {
        User u = new User(id);
        System.out.println(u + "===================");
        System.out.println(userMapper.delete(u));
        List<User> users = userMapper.queryAll();
        System.out.println(users);
        String s = "";
        for (User user : users) {
            s += user.toString() + "    ";
        }
        System.out.println(s);
        return s;


    }

    @RequestMapping("update")
    public String p(@RequestParam("id") Integer id, @RequestParam("age") Integer age, @RequestParam(required = false) String name) {
        return String.valueOf(userMapper.update(new User(id, age, name, 0)));
    }
}
