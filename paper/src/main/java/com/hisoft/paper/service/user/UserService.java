package com.hisoft.paper.service.user;

import com.hisoft.paper.pojo.User;

import java.util.List;

public interface UserService {
    User queryUserByName(String userName);

    List<User> queryUserList();

    User queryUserById(Integer id);
}
