package com.tian.happyfood.service.wechatutil.bean.message;

import com.tian.happyfood.service.wechatutil.bean.WXRequestData;

/**
 * Created by Administrator on 2017/12/19 0019.
 */
public class SupperMessage extends WXRequestData{
    private String MsgId;

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                ", MsgId='" + MsgId + '\'' +
                '}'+super.toString();
    }
}
