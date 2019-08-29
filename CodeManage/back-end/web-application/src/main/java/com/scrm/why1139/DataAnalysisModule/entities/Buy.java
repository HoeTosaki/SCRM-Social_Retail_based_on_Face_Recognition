package com.scrm.why1139.DataAnalysisModule.entities;

import javax.persistence.*;
/**
 * Created by EalenXie on 2018/9/10 16:17.
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMngr_id() {
        return mngr_id;
    }

    public void setMngr_id(String mngr_id) {
        this.mngr_id = mngr_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getBuy_date() {
        return buy_date;
    }

    public void setBuy_date(String buy_date) {
        this.buy_date = buy_date;
    }

    public String getBuy_cnt() {
        return buy_cnt;
    }

    public void setBuy_cnt(String buy_cnt) {
        this.buy_cnt = buy_cnt;
    }

    public Integer getBuy_id() {
        return buy_id;
    }

    public void setBuy_id(Integer buy_id) {
        this.buy_id = buy_id;
    }

    @Override
    public String toString() {
        return "Buy{" +
                "buy_id=" + buy_id +"user_id=" + user_id +"\t"+"goods_id="+goods_id+
                '}';
    }

    public String toCSV()
    {
        return this.getBuy_id()+","+this.getUser_id()+","+this.getMngr_id()+","+this.getGoods_id()+","+this.getBuy_cnt()+","+this.getBuy_date()+",";
    }
}