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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        if(jdDishList == null || jdDishList.size() == 0){
            return;
        }
        for (JDDish j: jdDishList) {
            // 由于从京东万象同步下来的数据,有些内容中会含有<br />标签,导致发到微信后解析异常,
            // 这里加一个过滤, 对内容中所有的html标签进行过滤

            //先通过菜品id查一下本地是否有这个菜品, 有就不重复插入了
            Dish dish = dishMapper.queryById(j.getId());
            if(dish != null){
                continue;
            }
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
                p.setContent(filterXml(procedures.get(i).getPcontent()));
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

    public List<DishDto> queryDetailByDishName(String dishName) throws JMSException {
        List<Dish> dishList = dishMapper.queryByName(dishName);
        if(dishList == null || dishList.size() == 0){
            // 如果在本地查不到该菜品,则去京东万象上去同步类似菜品
            producer.sendText(dishName);
            return null;
        }
        List<DishDto> dishDtoList = new ArrayList<DishDto>();
        for (Dish d:dishList) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(d, dishDto);
            // 查询出该菜品所有的用料
            List<DishMaterial> materialList = dishMaterialService.queryByDishId(d.getId());
            dishDto.setMaterials(materialList);
            // 查询出所有的操作步骤
            List<DishProcedure> procedureList = dishProcedureService.queryByDishId(d.getId());
            dishDto.setProcedures(procedureList);
            dishDtoList.add(dishDto);
        }
        return dishDtoList;
    }

    public Set<String> queryDishName() {
        return dishMapper.queryAllDishName();
    }

    private static String filterXml(String s){
        int start = 0;
        int end = 0;
        do{
            start = s.indexOf("<");
            end = s.indexOf(">");
            if(start != -1 && end != -1){
                String subStr = s.substring(start,end+1);
                s = s.replace(subStr, "");
            }
        }while (start != -1 && end != -1);
        return s;
    }
}
