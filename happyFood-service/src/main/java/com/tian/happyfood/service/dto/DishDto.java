package com.tian.happyfood.service.dto;

import com.tian.happyfood.dao.entity.Dish;
import com.tian.happyfood.dao.entity.DishMaterial;
import com.tian.happyfood.dao.entity.DishProcedure;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public class DishDto extends Dish{
    private List<DishMaterial> materials;
    private List<DishProcedure> procedures;

    public List<DishMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<DishMaterial> materials) {
        this.materials = materials;
    }

    public List<DishProcedure> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<DishProcedure> procedures) {
        this.procedures = procedures;
    }
}
