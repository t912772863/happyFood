package com.tian.happyfood.service.wechatutil.bean.event;

/**
 * Created by Administrator on 2017/12/20 0020.
 */
public class LocationEvent extends SupperEvent {
    private String Latitude;
    private String Longitude;
    private String Precision;

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getPrecision() {
        return Precision;
    }

    public void setPrecision(String precision) {
        Precision = precision;
    }
}
