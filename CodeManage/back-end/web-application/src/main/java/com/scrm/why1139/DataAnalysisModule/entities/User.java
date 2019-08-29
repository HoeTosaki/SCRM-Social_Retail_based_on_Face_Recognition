package com.scrm.why1139.DataAnalysisModule.entities;

import javax.persistence.*;
/**
 * Created by EalenXie on 2018/9/10 16:17.
 */
@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                '}';
    }

    public String toCSV()
    {
        return this.getUser_id()+",";
    }

}