package com.scrm.why1139.DataAnalysisModule.entities;

import javax.persistence.*;
/**
 * Created by EalenXie on 2018/9/10 16:17.
 */
@Entity
@Table
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer goods_id;
    private String goods_name;
    private String goods_type;
    private String goods_price;

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goods_id=" + goods_id +
                '}';
    }

    public String toCSV()
    {
        return this.getGoods_id()+","+this.getGoods_name()+","+this.getGoods_price()+","+this.getGoods_type();
    }
}