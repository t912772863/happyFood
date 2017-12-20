package com.tian.happyfood.service.wechatutil.bean.message;

/**
 * Created by Administrator on 2017/12/20 0020.
 */
public class VideoMessage extends SupperMessage {
    private String MediaId;
    private String ThumbMediaId;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getThumbMediaId() {
        return ThumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        ThumbMediaId = thumbMediaId;
    }

    @Override
    public String toString() {
        return "VideoMessage{" +
                "MediaId='" + MediaId + '\'' +
                ", ThumbMediaId='" + ThumbMediaId + '\'' +
                '}';
    }
}
