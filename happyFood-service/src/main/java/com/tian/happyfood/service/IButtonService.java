package com.tian.happyfood.service;

import com.tian.common.other.PageParam;
import com.tian.happyfood.dao.entity.Button;
import com.tian.happyfood.service.dto.ButtonDto;

import java.util.List;

/**
 * Created by Administrator on 2017/12/14 0014.
 */
public interface IButtonService {
    void deleteById(Long id);

    void insert(Button record);

    Button queryById(Long id);

    void updateById(Button record);

    /**
     * 根据等级查询按钮
     * @param level
     * @param useStatus
     * @param status
     * @param name
     * @return
     */
    List<Button> queryButtonByLeval(Integer level, Integer useStatus, Integer status, String name);

    List<ButtonDto> queryButtonOfWX();

    List<Button> queryButtonByParentId(Long parentId, Integer useStatus, Integer status);

    /**
     * 删除微信公众号上现有的所有按钮
     */
    void deleteButtonOfWX();

    /**
     * 上传微信按钮
     */
    void uploadButtonOfWX() throws Exception;

    List<Button> queryByPage(PageParam<Button> page);
}
