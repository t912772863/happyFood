package com.tian.happyfood.service.message;

import com.tian.common.other.BusinessException;
import com.tian.happyfood.service.wechatutil.bean.message.SupperMessage;
import com.tian.happyfood.service.wechatutil.bean.response.WXResponseData;
import org.springframework.stereotype.Component;

/**
 * 默认的返回
 * Created by tianxiong on 2019/1/24.
 */
@Component
public class DefaultMessageCreator implements IMessageCreator {
    public WXResponseData buildMsg(SupperMessage reqMsg) {
        throw new BusinessException(500, "请指定菜谱版本");
    }
}
