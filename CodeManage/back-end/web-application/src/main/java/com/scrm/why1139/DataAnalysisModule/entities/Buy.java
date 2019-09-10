package com.scrm.why1139.DataAnalysisModule.entities;

import javax.persistence.*;

/**
 * 用于执行数据分析的Buy实体类
 * @author 王浩宇
 * @date 9.2
 */
@Entity
@Table
public class Buy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer buy_id;
    private String user_id;
    private String mngr_id;
    private String goods_id;
    private String buy_date;
    private String buy_cnt;

    /**
     * 获取用户ID
     * @return 用户ID
     */
    public String getUser_id() {
        return user_id;
    }

    /**
     * 设置用户ID
     * @param user_id 用户ID
     */
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    /**
     * 获取收银员ID
     * @return 收银员ID
     */
    public String getMngr_id() {
        return mngr_id;
    }

    /**
     * 设置收银员ID
     * @param mngr_id in 收银员ID
     */
    public void setMngr_id(String mngr_id) {
        this.mngr_id = mngr_id;
    }

    /**
     * 设置商品ID
     * @return 商品ID
     */
    public String getGoods_id() {
        return goods_id;
    }

    /**
     * 设置商品ID
     * @param goods_id
     */
    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    /**
     * 获取购买日期
     * @return 购买日期
     */
    public String getBuy_date() {
        return buy_date;
    }

    /**
     * 设置购买日期
     * @param buy_date in 购买日期
     */
    public void setBuy_date(String buy_date) {
        this.buy_date = buy_date;
    }

    /**
     * 获取购买数量
     * @return 购买数量
     */
    public String getBuy_cnt() {
        return buy_cnt;
    }

    /**
     * 设置购买数量
     * @param buy_cnt in 购买数量
     */
    public void setBuy_cnt(String buy_cnt) {
        this.buy_cnt = buy_cnt;
    }

    /**
     * 获取购买ID
     * @return 购买ID
     */
    public Integer getBuy_id() {
        return buy_id;
    }

    /**
     * 设置购买ID
     * @param buy_id in 购买ID
     */
    public void setBuy_id(Integer buy_id) {
        this.buy_id = buy_id;
    }

    /**
     * 导出字符串
     * @return 字符串String
     */
    @Override
    public String toString() {
        return "Buy{" +
                "buy_id=" + buy_id +"user_id=" + user_id +"\t"+"goods_id="+goods_id+
                '}';
    }

    /**
     * 导出CSV文件
     * @return String
     */
    public String toCSV()
    {
        return this.getBuy_id()+","+this.getUser_id()+","+this.getMngr_id()+","+this.getGoods_id()+","+this.getBuy_date()+","+this.getBuy_cnt();
    }
}