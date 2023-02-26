package com.example.service.user;

import com.example.mapper.user.UserMapper;
import com.example.pojo.user.User;
import com.example.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class UserService {
    @Resource
    UserMapper userMapper;

    public User extractByUsername(String username) {
        User user = userMapper.extractByUsername(username);
        char[] chars = user.getPassword().toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if ((i & 1) == 0) {
                builder.append(chars[i]);
            }
        }
        user.setPassword(builder.toString());
        return user;
    }

    public User getByUsername(String username) {

        User user = userMapper.getByUsername(username);
        char[] chars = user.getPassword().toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if ((i & 1) == 0) {
                builder.append(chars[i]);
            }
        }
        user.setPassword(builder.toString());
        return user;
    }

    public int checkLogin(String username, String password) throws RuntimeException {
        User user = userMapper.getByUsername(username);
        if (user == null) {
            log.error("用户不存在");
            return 4010;
        }
        char[] chars = user.getPassword().toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if ((i & 1) == 0) {
                builder.append(chars[i]);
            }
        }//加盐的策略是奇数下标为随机字符，这是还原为原始的md5加密后的密码
        String realMd5Password = builder.toString();
        String paramMd5Password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!realMd5Password.equals(paramMd5Password)) {
            log.info("username or password unmatched ,login  failed");
            return 4021;
        }
        return 200;
    }

    public User getByPhone(String phone) {
        if (!DataUtils.isPhoneNumber(phone)) {
            return null;
        }
        return null;
    }

    public User extractByPhone(String phone) {
        if (!DataUtils.isPhoneNumber(phone)) {
            return null;
        }
        return null;
    }

    public User getById(BigInteger id) {
        return userMapper.getById(id);
    }

    public int insert(User user) {
        return userMapper.insert(user);
    }
}
