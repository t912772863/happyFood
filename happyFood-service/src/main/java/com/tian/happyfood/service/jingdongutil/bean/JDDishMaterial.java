package com.tian.happyfood.service.jingdongutil.bean;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public class JDDishMaterial {
    private String amount;

    private String mname;

    private String type;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "JDDishMaterialDto{" +
                "amount='" + amount + '\'' +
                ", mname='" + mname + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
