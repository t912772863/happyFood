package com.tian.happyfood.dao.mapper;

import com.tian.common.other.PageParam;
import com.tian.happyfood.dao.entity.Button;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ButtonMapper {
    void deleteById(Long id);

    void insert(Button record);

    Button queryById(Long id);

    void updateById(Button record);

    /**
     * 条件查询按钮
     * @param level 等级
     * @param useStatus 是否使用中
     * @param status 状态
     * @param name 名称
     * @return
     */
    List<Button> queryByRole(@Param("level") Integer level, @Param("useStatus") Integer useStatus,
                             @Param("status") Integer status, @Param("name") String name);

    /**
     * 查询某个父按钮下的子按钮
     * @param parentId
     * @return
     */
    List<Button> queryByParentId(@Param("parentId") Long parentId, @Param("useStatus")Integer useStatus,
                                    @Param("status")Integer status);

    /**
     * 条件分页查询按钮信息
     * @param page
     * @return
     */
    List<Button> queryByPage(PageParam<Button> page);
}