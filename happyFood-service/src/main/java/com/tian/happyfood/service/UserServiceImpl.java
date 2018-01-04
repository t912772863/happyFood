package com.tian.happyfood.service;

import com.tian.common.other.PageParam;
import com.tian.happyfood.dao.entity.User;
import com.tian.happyfood.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4 0004.
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }

    public void insert(User user) {
        user.setCreateTime(new Date());
        userMapper.insert(user);
    }

    public User queryById(Long id) {
        return userMapper.queryById(id);
    }

    public void updateById(User user) {
        user.setUpdateTime(new Date());
        userMapper.updateById(user);
    }

    public User queryByMail(String mail) {
        return userMapper.queryByMail(mail);
    }

    public List<User> queryByPage(PageParam<User> pageParam) {
        List<User> list = userMapper.queryByPage(pageParam);
        pageParam.setResult(list);
        return list;
    }
}
