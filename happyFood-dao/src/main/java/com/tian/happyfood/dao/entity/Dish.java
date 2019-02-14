package com.tian.happyfood.dao.entity;

public class Dish {
    private String classId;

    private String prepareTime;

    private String name;

    private String id;

    private String pic;

    private String tag;

    private String peopleNum;

    private String content;

    private String cookingTime;

    private Integer Type;

    private String detailUrl;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId == null ? null : classId.trim();
    }

    public String getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(String prepareTime) {
        this.prepareTime = prepareTime == null ? null : prepareTime.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(String peopleNum) {
        this.peopleNum = peopleNum == null ? null : peopleNum.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime == null ? null : cookingTime.trim();
    }

    public Integer getType() {
        return Type;
    }

    public void setType(Integer type) {
        Type = type;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "classId='" + classId + '\'' +
                ", prepareTime='" + prepareTime + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", pic='" + pic + '\'' +
                ", tag='" + tag + '\'' +
                ", peopleNum='" + peopleNum + '\'' +
                ", content='" + content + '\'' +
                ", cookingTime='" + cookingTime + '\'' +
                ", Type=" + Type +
                ", detailUrl='" + detailUrl + '\'' +
                '}';
    }
}