package com.tian.happyfood.controller;

import com.tian.common.other.ResponseData;
import com.tian.happyfood.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

@Controller
@RequestMapping("test")
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
    public ResponseData get(String signature, String timestamp, String nonce, String echostr, String token, HttpServletResponse response) throws IOException {
        if(signature == null || nonce == null|| timestamp == null || echostr == null){
            return failed;
        }
        if("happyfood2017".equals(token)){
            String[] str = { token, timestamp, nonce };
            Arrays.sort(str); // 字典序排序
            String bigStr = str[0] + str[1] + str[2];
            // SHA1加密
            String digest = getSha1(bigStr);
            // 确认请求来至微信
            if (digest.equals(signature)) {
                response.getWriter().print(echostr);
            }
        }else {
            return failed;
        }
        return failed;
    }


    //Sha1签名
    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }
}
