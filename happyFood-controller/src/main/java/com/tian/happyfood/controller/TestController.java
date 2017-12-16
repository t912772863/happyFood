package com.tian.happyfood.controller;

import com.tian.common.other.ResponseData;
import com.tian.common.util.SHA1Utils;
import com.tian.happyfood.service.ITestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

@Controller
public class TestController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private ITestService testService;

    @RequestMapping("queryTestById")
    @ResponseBody
    public ResponseData queryTestById(Long id) {
        return successData.setData(testService.selectByPrimaryKey(id));
    }

//    @RequestMapping("get")
    @ResponseBody
    public String get(String signature, String timestamp, String nonce,
                      String echostr) throws IOException {
        String[] str = {"happyfood2017", timestamp, nonce};
        Arrays.sort(str); // 字典序排序
        String bigStr = str[0] + str[1] + str[2];
        // SHA1加密
        String digest = SHA1Utils.getSha1(bigStr);
        logger.info("digest = " + digest);
        // 确认请求来至微信
        if (digest.equals(signature)) {
            return echostr;
        }
        return "error";
    }

    public static void main(String[] args) {
        String s = "aaa";
        String result = SHA1Utils.getSha1(s);
        System.out.println(result);
    }
}
