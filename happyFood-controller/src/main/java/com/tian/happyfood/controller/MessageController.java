package com.tian.happyfood.controller;

import com.tian.common.other.ResponseData;
import com.tian.common.util.SHA1Utils;
import com.tian.happyfood.dao.entity.Message;
import com.tian.happyfood.service.IMessageService;
import com.tian.happyfood.service.ITestService;
import com.tian.happyfood.service.wechatutil.WechatMessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

@Controller
public class MessageController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    private ITestService testService;
    @Autowired
    private IMessageService messageService;

    @RequestMapping("queryTestById")
    @ResponseBody
    public ResponseData queryTestById(Long id) {
        return successData.setData(testService.selectByPrimaryKey(id));
    }

    @RequestMapping("get")
    @ResponseBody
    public String get(String signature, String timestamp, String nonce,
                      String echostr, HttpServletRequest request) throws IOException {
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        if (isGet) {
            String[] str = { "happyfood2017", timestamp, nonce };
            Arrays.sort(str);
            String bigStr = str[0] + str[1] + str[2];

            String digest = SHA1Utils.getSha1(bigStr);
            logger.info("digest = " + digest);

            if (digest.equals(signature)) {
                return echostr;
            }
            return "error";
        }

        String line = null;
        StringBuffer stringBuffer = new StringBuffer("");
        while ((line = request.getReader().readLine()) != null) {
            stringBuffer.append(line);
        }
        String reqStr = stringBuffer.toString();
        logger.info("请求的参数: " + reqStr);
        Message message = WechatMessageUtils.parseXML(reqStr);
        messageService.insert(message);
        return "";
    }

    public static void main(String[] args) {
        String s = "aaa";
        String result = SHA1Utils.getSha1(s);
        System.out.println(result);
    }
}
