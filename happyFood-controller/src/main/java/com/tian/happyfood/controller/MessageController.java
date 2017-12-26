package com.tian.happyfood.controller;

import com.tian.common.other.ResponseData;
import com.tian.common.util.SHA1Utils;
import com.tian.happyfood.service.ITestService;
import com.tian.happyfood.service.common.DetributionWXMessage;
import com.tian.happyfood.service.wechatutil.WXMessageUtils;
import com.tian.happyfood.service.wechatutil.bean.WXRequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
    private DetributionWXMessage detributionWXMessage;

    @RequestMapping("queryTestById")
    @ResponseBody
    public ResponseData queryTestById(Long id) {
        return successData.setData(testService.selectByPrimaryKey(id));
    }

    @RequestMapping("get")
    @ResponseBody
    public String get(String signature, String timestamp, String nonce,
                      String echostr, HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("utf-8");
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
        WXRequestData wxRequestData = WXMessageUtils.parseXML(reqStr);
        String result = detributionWXMessage.executeWXMessage(wxRequestData);

        return result;
    }

    public static void main(String[] args) {
        String s = "ykR2sFsqsrycxbyJBAo3ujBoYpWfY5TZCTtFUelCZum6e5jh_U4obuN2r5mbnScJ";
        System.out.println(s.length());
    }
}
