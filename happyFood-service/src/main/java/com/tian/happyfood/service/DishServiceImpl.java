package com.tian.happyfood.service;

import com.tian.common.util.ActivemqUtils;
import com.tian.happyfood.dao.entity.Dish;
import com.tian.happyfood.dao.entity.DishMaterial;
import com.tian.happyfood.dao.entity.DishProcedure;
import com.tian.happyfood.dao.mapper.DishMapper;
import com.tian.happyfood.service.common.Config;
import com.tian.happyfood.service.dto.DishDto;
import com.tian.happyfood.service.jingdongutil.bean.JDDish;
import com.tian.happyfood.service.jingdongutil.bean.JDDishMaterial;
import com.tian.happyfood.service.jingdongutil.bean.JDDishProcedure;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import java.util.List;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
@Service
public class DishServiceImpl implements IDishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private IDishVarietyService dishVarietyService;
    @Autowired
    private IDishProcedureService dishProcedureService;
    @Autowired
    private IDishMaterialService dishMaterialService;

    private static ActivemqUtils.Producer producer;

    public static void init() throws JMSException {
        producer =  ActivemqUtils.getQueueProducerInstance(Config.config.getString("activemq_username"),
                Config.config.getString("activemq_password"),
                Config.config.getString("activemq_url"),
                Config.config.getString("activemq_destination_dish"));
    }

    public void insert(Dish dish) {
        dishMapper.insertSelective(dish);
    }

    @Transactional
    public void insertDishDetail(List<JDDish> jdDishList) {
        for (JDDish j: jdDishList) {
            // 入库菜品
            Dish d = new Dish();
            d.setName(j.getName());
            d.setClassId(j.getClassid());
            d.setContent(j.getContent());
            d.setCookingTime(j.getCookingtime());
            d.setPeopleNum(j.getPeoplenum());
            d.setPic(j.getPic());
            d.setId(j.getId());
            d.setPrepareTime(j.getPreparetime());
            d.setTag(j.getTag());
            insert(d);
            // 入库材料
            List<JDDishMaterial> materials = j.getMaterial();
            for (JDDishMaterial j1:materials) {
                DishMaterial m = new DishMaterial();
                m.setName(j1.getMname());
                m.setAmount(j1.getAmount());
                m.setType(j1.getType());
                m.setDishId(j.getId());
                dishMaterialService.insert(m);
            }
            // 入库操作步骤
            List<JDDishProcedure> procedures = j.getProcess();
            for (int i = 0; i < procedures.size(); i++) {
                DishProcedure p = new DishProcedure();
                p.setDishId(j.getId());
                p.setPic(procedures.get(i).getPic());
                p.setContent(procedures.get(i).getPcontent());
                p.setOrders(i+1);
                dishProcedureService.insert(p);
            }
        }
    }

    public Dish queryById(String id) {
        return dishMapper.queryById(id);
    }

    /**
     * 查询菜品详细信息, 包括所有用料和操作步骤
     * @param id
     * @return
     */
    public DishDto queryDetailById(String id) {
        Dish dish = dishMapper.queryById(id);
        if(dish == null){
            return null;
        }
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        // 查询出该菜品所有的用料
        List<DishMaterial> materialList = dishMaterialService.queryByDishId(id);
        dishDto.setMaterials(materialList);
        // 查询出所有的操作步骤
        List<DishProcedure> procedureList = dishProcedureService.queryByDishId(id);
        dishDto.setProcedures(procedureList);

        return dishDto;
    }

    public DishDto queryDetailByDishName(String dishName) throws JMSException {
        Dish dish = dishMapper.queryByName(dishName);
        if(dish == null){
            // 如果在本地查不到该菜品,则去京东万象上去同步类似菜品
            producer.sendText(dishName);
            return null;
        }
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        // 查询出该菜品所有的用料
        List<DishMaterial> materialList = dishMaterialService.queryByDishId(dish.getId());
        dishDto.setMaterials(materialList);
        // 查询出所有的操作步骤
        List<DishProcedure> procedureList = dishProcedureService.queryByDishId(dish.getId());
        dishDto.setProcedures(procedureList);
        return dishDto;
    }
}
