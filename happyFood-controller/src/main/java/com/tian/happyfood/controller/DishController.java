package com.tian.happyfood.controller;

import com.tian.common.other.PageParam;
import com.tian.common.other.ResponseData;
import com.tian.common.validation.NotNull;
import com.tian.common.validation.Number;
import com.tian.happyfood.dao.entity.Dish;
import com.tian.happyfood.dao.esmapper.BaseESSearcher;
import com.tian.happyfood.dao.espo.BulkParam;
import com.tian.happyfood.dao.espo.MultiGetParam;
import com.tian.happyfood.service.IDishService;
import com.tian.happyfood.service.IDishVarietyService;
import com.tian.happyfood.service.dto.DishDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
@Controller
public class DishController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(DishController.class);
    @Autowired
    private IDishVarietyService dishVarietyService;
    @Autowired
    private IDishService dishService;
    @Autowired
    private BaseESSearcher baseESSearcher;

    /**
     * 从京东万象上下载菜品分类
     * @return
     */
    @RequestMapping("downloadDishVariety")
    @ResponseBody
    public ResponseData downloadDishVariety(){
        dishVarietyService.getJDDishVariety();
        return success;
    }

    /**
     * 根据菜品名查询某个菜品
     * @param name
     * @return
     */
    @RequestMapping("queryDishByName")
    @ResponseBody
    public ResponseData queryDishByName(@NotNull String name, @Number(minValue = 1, maxValue = 2, nullAble = true) Integer type) throws JMSException{
        return successData.setData(dishService.queryDetailByDishName(name, type));
    }

    /**
     * 跳转菜品详情页
     * @param id 菜品id
     * @return
     * @throws JMSException
     */
    @RequestMapping("f/toDishDetail")
    public ModelAndView toDishDetail(String id) throws JMSException {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("dishDetail");
        mav.addObject("id", id);
        return mav;
    }

    /**
     * 根据菜品ID查询菜品详情
     * @param id
     * @return
     */
    @RequestMapping("f/queryDishDetail")
    @ResponseBody
    public ResponseData queryDishDetail(String id){
        DishDto dishDto = dishService.queryDetailById(id);
        return successData.setData(dishDto);
    }

    @RequestMapping("putAllDishToEs")
    @ResponseBody
    public ResponseData putAllDishToEs(){
        PageParam<Dish> pageParam = new PageParam<Dish>();
        List<Dish> list = null;
        do{
            dishService.queryByPage(pageParam);
            list =  pageParam.getResult();
            pageParam.setPageNumber(pageParam.getPageNumber()+1);
            for(Dish d: list){
                try {
                    baseESSearcher.put("dish", d);
                } catch (Exception e) {
                    LOGGER.error("error", e);
                }
            }
        }while (list.size()>0);
        return success;
    }

    /**
     * 测试从es中删除一个文档
     * @param id
     * @return
     */
    @RequestMapping("deleteFromEs")
    @ResponseBody
    public ResponseData deleteFromEs(String id){
        baseESSearcher.delete("dish", id);
        return success;
    }

    /**
     * 测试从es中根据id精确获取文档
     * @param id
     * @return
     */
    @RequestMapping("getFromEsById")
    @ResponseBody
    public ResponseData getFromEsById(String id){
        return successData.setData(baseESSearcher.getById("dish", id));
    }

    /**
     * 测试在es中只更新指定字段
     * @param id
     * @param filed
     * @param value
     * @return
     */
    @RequestMapping("updateEsById")
    @ResponseBody
    public ResponseData updateEsById(@NotNull String id, @NotNull String filed, @NotNull String value) throws InterruptedException, ExecutionException, IOException {
        Map<String, String> filedValues = new HashMap();
        filedValues.put(filed, value);
        baseESSearcher.post("dish", id,filedValues);
        return success;
    }

    /**
     * 测试一次获取多种类型的值
     * @return
     */
    @RequestMapping("testMultiGet")
    @ResponseBody
    public ResponseData testMultiGet(){
        List<MultiGetParam> paramList = new ArrayList<MultiGetParam>();
        MultiGetParam param1 = new MultiGetParam();
        param1.setIndex("happy_food_dish");
        param1.setType("dish");
        List<String> idList1 = new ArrayList<String>();
        idList1.add("1030");
        idList1.add("11004");
        param1.setIdList(idList1);
        paramList.add(param1);

        MultiGetParam param2 = new MultiGetParam();
        param2.setIndex("happy_food");
        param2.setType("dish");
        List<String> idList2 = new ArrayList<String>();
        idList2.add("1");
        param2.setIdList(idList2);
        paramList.add(param2);

        return successData.setData(baseESSearcher.multiGet(paramList));
    }

    public static class TempClass{
        private String name;
        private String desc;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    /**
     * 测试bulk一次更新多个文档
     * @return
     */
    @RequestMapping("testBulk")
    @ResponseBody
    public ResponseData testBulk(){
        List<BulkParam> bulkParamList = new ArrayList<BulkParam>();
        BulkParam param1 = new BulkParam();
        param1.setIndex("happy_food");
        param1.setType("dish");
        param1.setId("2");
        TempClass tempClass = new TempClass();
        tempClass.setName("name2");
        tempClass.setDesc("desc2");
        param1.setSource(tempClass);
        bulkParamList.add(param1);

        BulkParam param2 = new BulkParam();
        param2.setIndex("happy_food");
        param2.setType("dish");
        param2.setId("3");
        TempClass tempClass2 = new TempClass();
        tempClass2.setName("name3");
        tempClass2.setDesc("desc3");
        param2.setSource(tempClass2);
        bulkParamList.add(param2);

        baseESSearcher.bulk(bulkParamList);
        return success;
    }

    @RequestMapping("testGetAll")
    @ResponseBody
    public ResponseData testGetAll(){
        baseESSearcher.queryAll("happy_food");
        return success;
    }


}
