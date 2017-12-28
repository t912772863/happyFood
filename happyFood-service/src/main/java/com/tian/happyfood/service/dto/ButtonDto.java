package com.tian.happyfood.service.dto;

import java.util.Date;
import java.util.List;

/**
 * DTO 
 * Created by Administrator on 2017/12/14 0014.
 */
public class ButtonDto {
    private Long id;

    private String name;

    private String type;

    private Integer level;

    private Integer orders;

    private Long parentId;

    private Integer useStatus;

    private Integer status;

    private Date createTime;

    private Date updateTime;
    /**
     * 子级按钮
     */
    private List<ButtonDto> sub_button;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<ButtonDto> getSub_button() {
        return sub_button;
    }

    public void setSub_button(List<ButtonDto> sub_button) {
        this.sub_button = sub_button;
    }

    @Override
    public String toString() {
        return "ButtonDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", level=" + level +
                ", orders=" + orders +
                ", parentId=" + parentId +
                ", useStatus=" + useStatus +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", sub_button=" + sub_button +
                '}';
    }
}
