package com.tian.happyfood.service.wechatutil.bean.message;

/**
 * Created by Administrator on 2017/12/20 0020.
 */
public class ImageMessage extends SupperMessage{
    private String PicUrl;
    private String MediaId;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    @Override
    public String toString() {
        return "ImageMessage{" +
                "PicUrl='" + PicUrl + '\'' +
                ", MediaId='" + MediaId + '\'' +
                '}';
    }
}
