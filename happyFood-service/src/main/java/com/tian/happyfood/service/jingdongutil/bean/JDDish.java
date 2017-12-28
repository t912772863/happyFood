package com.tian.happyfood.service.jingdongutil.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public class JDDish {
    private String classid;

    private String preparetime;

    private String name;

    private String id;

    private String pic;

    private String tag;

    private String peoplenum;

    private String content;

    private String cookingtime;

    private List<JDDishMaterial> material;
    private List<JDDishProcedure> process;

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getPreparetime() {
        return preparetime;
    }

    public void setPreparetime(String preparetime) {
        this.preparetime = preparetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPeoplenum() {
        return peoplenum;
    }

    public void setPeoplenum(String peoplenum) {
        this.peoplenum = peoplenum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCookingtime() {
        return cookingtime;
    }

    public void setCookingtime(String cookingtime) {
        this.cookingtime = cookingtime;
    }

    public List<JDDishMaterial> getMaterial() {
        return material;
    }

    public void setMaterial(List<JDDishMaterial> material) {
        this.material = material;
    }

    public List<JDDishProcedure> getProcess() {
        return process;
    }

    public void setProcess(List<JDDishProcedure> process) {
        this.process = process;
    }
}
