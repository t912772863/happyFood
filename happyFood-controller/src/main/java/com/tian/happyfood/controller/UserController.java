package com.tian.happyfood.controller;

import com.tian.common.other.BusinessException;
import com.tian.common.other.ResponseData;
import com.tian.common.validation.NotNull;
import com.tian.happyfood.dao.entity.User;
import com.tian.happyfood.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/1/4 0004.
 */
@Controller
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;

    @RequestMapping("login")
    @ResponseBody
    public ResponseData login(@NotNull String username, @NotNull String password, HttpServletRequest request){
        User user = userService.queryByMail(username);
        if(user == null){
            throw new BusinessException(500, "用户名或者密码错误");
        }
        if(!password.equals(user.getPassWord())){
            throw new BusinessException(500, "用户名或者密码错误");
        }
        request.getSession(true).setAttribute("user",user);
        return success;
    }

}
