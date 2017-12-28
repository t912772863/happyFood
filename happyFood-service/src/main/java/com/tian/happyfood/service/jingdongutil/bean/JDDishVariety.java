package com.tian.happyfood.service.jingdongutil.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public class JDDishVariety {
    private String classid;
    private String name;
    private String parentid;
    private List<JDDishVariety> list;

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public List<JDDishVariety> getList() {
        return list;
    }

    public void setList(List<JDDishVariety> list) {
        this.list = list;
    }
}
