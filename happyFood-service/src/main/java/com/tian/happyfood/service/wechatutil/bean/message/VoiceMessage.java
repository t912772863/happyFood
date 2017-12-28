package com.tian.happyfood.service.wechatutil.bean.message;

/**
 * Created by Administrator on 2017/12/20 0020.
 */
public class VoiceMessage extends SupperMessage {
    private String MediaId;
    private String Format;
    private String Recognition;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }

    public String getRecognition() {
        return Recognition;
    }

    public void setRecognition(String recognition) {
        Recognition = recognition;
    }

    @Override
    public String toString() {
        return "VoiceMessage{" +
                "MediaId='" + MediaId + '\'' +
                ", Format='" + Format + '\'' +
                ", Recognition='" + Recognition + '\'' +
                '}';
    }
}
