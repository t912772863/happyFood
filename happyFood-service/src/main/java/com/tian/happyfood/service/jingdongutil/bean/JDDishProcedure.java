package com.tian.happyfood.service.jingdongutil.bean;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public class JDDishProcedure {
    private String pcontent;

    private String pic;

    public String getPcontent() {
        return pcontent;
    }

    public void setPcontent(String pcontent) {
        this.pcontent = pcontent;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "JDDishProcedure{" +
                "pcontent='" + pcontent + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
