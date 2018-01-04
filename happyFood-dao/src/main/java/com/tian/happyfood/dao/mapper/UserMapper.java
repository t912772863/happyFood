package com.tian.happyfood.dao.mapper;

import com.tian.common.other.PageParam;
import com.tian.happyfood.dao.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    void deleteById(Long id);

    void insert(User record);

    User queryById(Long id);

    void updateById(User record);

    User queryByMail(@Param("mail")String mail);

    List<User> queryByPage(PageParam<User> pageParam);
}