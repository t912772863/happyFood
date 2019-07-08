package com.tian.happyfood.dao.espo;

/**
 * Created by tianxiong on 2019/7/6.
 */
public class BulkParam {
    private String index;
    private String type;
    /**
     * 文档id
     */
    private String id;
    private Object source;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
