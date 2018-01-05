package com.tian.happyfood.controller;

import com.tian.common.other.BusinessException;
import com.tian.common.other.ResponseData;
import com.tian.common.util.JavaMailUtil;
import com.tian.common.util.JedisUtil;
import com.tian.common.util.RandomUtil;
import com.tian.common.validation.NotNull;
import com.tian.happyfood.dao.entity.User;
import com.tian.happyfood.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户相关请求, 以及不需要登录就可以访问的请求
 * Created by Administrator on 2018/1/4 0004.
 */
@Controller
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;

    /**
     * 登录方法
     * @param username 用户名(邮箱)
     * @param password 密码
     * @param request
     * @return
     */
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

    /**
     * 通过邮件发送验证码
     * @param mail 收件人邮件
     * @return
     * @throws Exception
     */
    @RequestMapping("getRegisterCode")
    @ResponseBody
    public ResponseData getRegisterCode(@NotNull String mail) throws Exception {
        // 先查询一下该邮件是否已注册, 不允许同一个邮件注册多个帐号
        User user = userService.queryByMail(mail);
        if(user != null){
            throw new BusinessException(500,"该邮箱已注册");
        }
        String code = RandomUtil.getRandomCode(6,6);
        JavaMailUtil.sendMail("MW美之味",mail,mail,"注册验证码","您好, 您在正在［MW美之味］平台注册, 验证码为: "+code+". 有效时间5分钟. 如果不是本人操作, 请忽略.");
        JedisUtil.set(mail, code, 5*60);
        return success;
    }

    /**
     * 用户注册方法
     * @param user 用户输入的相关信息
     * @param code 邮件的验证码
     * @return
     */
    @RequestMapping("insert")
    @ResponseBody
    public ResponseData insert(User user,@NotNull String code, HttpServletRequest request){
        // 先验证验证码是否通过
        String cachCode = JedisUtil.get(user.getMail());
        if(!code.equals(cachCode)){
            throw new BusinessException(500, "您输入的验证码有误");
        }
        userService.insert(user);
        // 注册成功后, 默认登录
        request.getSession(true).setAttribute("user", user);
        return success;
    }

}
