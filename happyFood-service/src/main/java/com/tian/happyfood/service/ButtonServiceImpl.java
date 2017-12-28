package com.tian.happyfood.service;

import com.tian.common.util.ActivemqUtils;
import com.tian.happyfood.dao.entity.Button;
import com.tian.happyfood.dao.mapper.ButtonMapper;
import com.tian.happyfood.service.common.Config;
import com.tian.happyfood.service.dto.ButtonDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/14 0014.
 */
@Service
public class ButtonServiceImpl implements IButtonService {
    @Autowired
    private ButtonMapper buttonMapper;

    public void deleteById(Long id) {
        buttonMapper.deleteById(id);
    }

    public void insert(Button button) {
        buttonMapper.insert(button);
    }

    public Button queryById(Long id) {
        return buttonMapper.queryById(id);
    }


    public void updateById(Button button) {
        buttonMapper.updateById(button);
    }

    /**
     * 条件查询按钮集合
     *
     * @param level
     * @param useStatus
     * @param status
     * @param name
     * @return
     */
    public List<Button> queryButtonByLeval(Integer level, Integer useStatus, Integer status, String name) {
        return buttonMapper.queryByRole(level, useStatus, status, name);
    }

    public List<Button> queryButtonByParentId(Long parentId, Integer useStatus, Integer status) {
        return buttonMapper.queryByParentId(parentId, useStatus, status);
    }

    public List<ButtonDto> queryButtonOfWX() {
        // 先查询出没有父id的按钮, 再递归查询出子按钮
        List<Button> oneButtonList = buttonMapper.queryByRole(1, 1, 1, null);
        List<ButtonDto> dtoList = convertButtonList(oneButtonList);
        queryButtonRecurrence(dtoList);
        return dtoList;
    }

    private void queryButtonRecurrence(List<ButtonDto> buttonDtoList ){
        if(buttonDtoList == null || buttonDtoList.size() == 0){
            return;
        }
        for (ButtonDto b:buttonDtoList) {
            // 最多有三级菜单,就不再递归了
            if(b.getLevel() == 3){
                break;
            }
            List<Button> subButtonList = buttonMapper.queryByParentId(b.getId(),1,1);
            List<ButtonDto> subDtoList = convertButtonList(subButtonList);
            b.setSub_button(subDtoList);
            queryButtonRecurrence(subDtoList);
        }
    }

    public void uploadButtonOfWX() throws Exception {
        ActivemqUtils.Producer producer =ActivemqUtils.getQueueProducerInstance(Config.config.getString("activemq_username"),
                Config.config.getString("activemq_password"),
                Config.config.getString("activemq_url"),
                Config.config.getString("activemq_destination"));
        producer.sendText("======================");

//        List<ButtonDTO> buttonDTOList = queryButtonOfWX();
//        boolean index = WXButtonUtils.uploadButtons(buttonDTOList);
//        if(!index){
//            throw new BusinessException(500, "同步微信按钮失败");
//        }
    }

    public void deleteButtonOfWX() {

    }

    private static List<ButtonDto> convertButtonList(List<Button> buttonList) {
        if(buttonList == null || buttonList.size() ==0){
            return null;
        }
        List<ButtonDto> buttonDtoList = new ArrayList<ButtonDto>();
        for (Button b : buttonList) {
            ButtonDto dto = new ButtonDto();
            BeanUtils.copyProperties(b, dto);
            buttonDtoList.add(dto);
        }
        return buttonDtoList;
    }

}
