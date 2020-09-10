package com.hisoft.paper.service.user;

import com.hisoft.paper.dao.user.UserMapper;
import com.hisoft.paper.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Override
    public User queryUserByName(String userName) {
        User user = userMapper.queryUserByName(userName);
        return user;
    }

    @Override
    public List<User> queryUserList() {
        List<User> userList = userMapper.queryUserList();
        return userList;
    }

    @Override
    public User queryUserById(Integer id) {
        User user = userMapper.queryUserById(id);
        return user;
    }
}
