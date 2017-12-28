package com.tian.happyfood.service;

import com.tian.happyfood.dao.entity.DishVariety;
import com.tian.happyfood.dao.mapper.DishVarietyMapper;
import com.tian.happyfood.service.jingdongutil.JDDishUtils;
import com.tian.happyfood.service.jingdongutil.bean.JDDishVariety;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
@Service
public class DishVarietyServiceImpl implements IDishVarietyService {
    @Autowired
    private DishVarietyMapper dishVarietyMapper;

    public void insert(DishVariety dishVariety) {
        dishVarietyMapper.insertSelective(dishVariety);
    }

    /**
     * 把京东万象上的菜品分类信息同步到本地数据库
     */
    public void getJDDishVariety() {
        List<JDDishVariety> jdDisheVarietyList = JDDishUtils.recipeClass();
        insertDishToLocal(jdDisheVarietyList);
    }

    /**
     * 递归把从京东万象上的数据插入本地
     * @param dishVarietyList
     */
    private void insertDishToLocal(List<JDDishVariety> dishVarietyList){
        for (JDDishVariety j:dishVarietyList) {
            DishVariety dishVariety = new DishVariety();
            dishVariety.setClassId(j.getClassid());
            dishVariety.setName(j.getName());
            dishVariety.setParentId(j.getParentid());
            insert(dishVariety);
            List<JDDishVariety> subList = j.getList();
            if(subList != null && subList.size() > 0){
                insertDishToLocal(subList);
            }
        }
    }
}
