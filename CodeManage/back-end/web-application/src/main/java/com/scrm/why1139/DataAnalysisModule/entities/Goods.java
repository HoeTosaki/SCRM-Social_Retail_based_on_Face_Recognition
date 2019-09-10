package com.scrm.why1139.DataAnalysisModule.entities;

import javax.persistence.*;

/**
 * 用于执行数据分析的Goods实体类
 * @author 王浩宇
 * @date 9.3
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
    private String goods_cnt;

    /**
     * 获取GoodsID
     * @return GoodsID
     */
    public Integer getGoods_id() {
        return goods_id;
    }

    /**
     * 设置GoodsID
     * @param goods_id in GoodsID
     */
    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    /**
     * 获取Goods名称
     * @return Goods名称
     */
    public String getGoods_name() {
        return goods_name;
    }

    /**
     * 设置Goods名称
     * @param goods_name in Goods名称
     */
    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    /**
     * 获取Goods类型
     * @return Goods类型
     */
    public String getGoods_type() {
        return goods_type;
    }

    /**
     * 设置Goods类型
     * @param goods_type in Goods类型
     */
    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    /**
     * 获取商品价格
     * @return 商品价格
     */
    public String getGoods_price() {
        return goods_price;
    }

    /**
     * 设置商品价格
     * @param goods_price in 商品价格
     */
    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    /**
     * 获取String
     * @return String
     */
    @Override
    public String toString() {
        return "Goods{" +
                "goods_id=" + goods_id +
                '}';
    }

    /**
     * 导出CSV
     * @return String
     */
    public String toCSV()
    {
        return this.getGoods_id()+","+this.getGoods_type()+","+this.getGoods_price()+","+this.getGoods_cnt();
    }

    /**
     * 获取Goods数量
     * @return String
     */
    public String getGoods_cnt() {
        return goods_cnt;
    }

    /**
     * 设置Goods对象
     * @param goods_cnt in 商品数量
     */
    public void setGoods_cnt(String goods_cnt) {
        this.goods_cnt = goods_cnt;
    }
}