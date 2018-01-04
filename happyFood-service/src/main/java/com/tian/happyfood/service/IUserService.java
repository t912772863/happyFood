package com.tian.happyfood.service;

import com.tian.common.other.PageParam;
import com.tian.happyfood.dao.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2018/1/4 0004.
 */
public interface IUserService {
    void deleteById(Long id);

    void insert(User record);

    User queryById(Long id);

    void updateById(User record);

    User queryByMail(String mail);

    List<User> queryByPage(PageParam<User> pageParam);
}
