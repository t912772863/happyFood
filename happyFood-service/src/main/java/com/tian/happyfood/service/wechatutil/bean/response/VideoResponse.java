package com.tian.happyfood.service.wechatutil.bean.response;

/**
 * Created by Administrator on 2017/12/21 0021.
 */
public class VideoResponse extends WXResponseData {
    private String MediaId;
    private String Title;
    private String Description;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

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
}
