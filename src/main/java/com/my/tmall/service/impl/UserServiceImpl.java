package com.my.tmall.service.impl;

import com.my.tmall.mapper.UserMapper;
import com.my.tmall.pojo.User;
import com.my.tmall.pojo.UserExample;
import com.my.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public void add(User user) {
        userMapper.insert(user);
    }

    @Override
    public void delete(int id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User get(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List list() {
        UserExample example=new UserExample();
        example.setOrderByClause("id desc");
        return userMapper.selectByExample(example);
    }

    @Override
    public boolean isExist(String name) {
        UserExample example=new UserExample();
        example.createCriteria().andNameEqualTo(name);
        List<User> result=userMapper.selectByExample(example);
        if(!result.isEmpty())
            return true;
        return false;
    }

    @Override
    public User get(String name, String password) {
        UserExample example=new UserExample();
        example.createCriteria().andNameEqualTo(name).andPasswordEqualTo(password);
        List<User> result=userMapper.selectByExample(example);
        if (result.isEmpty())
            return null;
        return result.get(0);
    }
}
