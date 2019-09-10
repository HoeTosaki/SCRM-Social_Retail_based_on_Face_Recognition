package com.scrm.why1139.DataAnalysisModule.entities;

import javax.persistence.*;

/**
 * 用于执行数据分析的User实体类
 * @author 王浩宇
 * @date 9.2
 */
@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String user_id;

    /**
     * 获取UserID
     * @return String
     */
    public String getUser_id() {
        return user_id;
    }

    /**
     * 设置UserID
     * @param user_id in userID
     */
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    /**
     * 格式化为String
     * @return
     */
    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                '}';
    }

    /**
     * 导出CSV
     * @return String
     */
    public String toCSV()
    {
        return this.getUser_id();
    }

}