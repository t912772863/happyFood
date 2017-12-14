package com.tian.happyfood.controller;

import com.tian.common.other.ResponseData;
import com.tian.common.util.SHA1Utils;
import com.tian.common.validation.NotNull;
import com.tian.happyfood.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

@Controller
public class TestController extends BaseController{
    @Autowired
    private ITestService testService;

    @RequestMapping("queryTestById")
    @ResponseBody
    public ResponseData queryTestById(Long id){
        return successData.setData(testService.selectByPrimaryKey(id));
    }

    @RequestMapping("get")
    @ResponseBody
    public ResponseData get(@NotNull String signature,@NotNull String timestamp,@NotNull String nonce,
                            @NotNull String echostr,@NotNull String token, HttpServletResponse response) throws IOException {
        if("happyfood2017".equals(token)){
            String[] str = { token, timestamp, nonce };
            Arrays.sort(str); // 字典序排序
            String bigStr = str[0] + str[1] + str[2];
            // SHA1加密
            String digest = SHA1Utils.getSha1(bigStr);
            // 确认请求来至微信
            if (digest.equals(signature)) {
                response.getWriter().print(echostr);
            }
        }else {
            return failed;
        }
        return failed;
    }


    public static void main(String[] args) {
        String s = "aaa";
        String result = SHA1Utils.getSha1(s);
        System.out.println(result);
    }
}
