package com.tian.happyfood.service.wechatutil.bean.message;

/**
 * Created by Administrator on 2017/12/20 0020.
 */
public class LinkMessage extends SupperMessage {
    private String Title;
    private String Description;
    private String Url;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    @Override
    public String toString() {
        return "LinkMessage{" +
                "Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", Url='" + Url + '\'' +
                '}';
    }
}
