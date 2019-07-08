package com.tian.happyfood.dao.espo;

import java.util.List;

/**
 * Created by tianxiong on 2019/7/6.
 */
public class MultiGetParam {
    private String index;
    private String type;
    private List<String> idList;
    /**
     * 部分业务场景需要把从es中查出来的数据再转换成java对象, 这里放类型
     */
    private Class clazz;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
