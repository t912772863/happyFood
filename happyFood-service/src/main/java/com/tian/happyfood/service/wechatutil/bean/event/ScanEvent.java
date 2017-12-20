package com.tian.happyfood.service.wechatutil.bean.event;

/**
 * Created by Administrator on 2017/12/20 0020.
 */
public class ScanEvent extends SupperEvent {
    private String EventKey;
    private String Ticket;

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }

    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String ticket) {
        Ticket = ticket;
    }
}
