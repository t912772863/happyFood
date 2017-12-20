package com.tian.happyfood.service.wechatutil.bean.event;

import com.tian.happyfood.service.wechatutil.bean.WXRequestData;

/**
 * Created by Administrator on 2017/12/20 0020.
 */
public class SupperEvent extends WXRequestData {
    private String Event;

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }
}
