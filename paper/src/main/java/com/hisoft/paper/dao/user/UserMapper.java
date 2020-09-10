package com.hisoft.paper.dao.user;

import com.hisoft.paper.pojo.User;

import java.util.List;

public interface UserMapper {
    User queryUserByName(String userName);

    List<User> queryUserList();

    User queryUserById(Integer id);
}
