package com.tian.happyfood.service.wechatutil.bean.response;

/**
 * Created by Administrator on 2017/12/21 0021.
 */
public class WXResponseData {
    /** 消息收件人*/
    private String ToUserName;
    /** 消息发送者*/
    private String FromUserName;
    /** 消息创建时间*/
    private String CreateTime;
    /**
     * 消息类型
     * news 图文消息
     * text 文本消息
     * */
    private String MsgType;

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }
}
