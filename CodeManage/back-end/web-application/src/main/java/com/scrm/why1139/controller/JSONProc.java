package com.scrm.why1139.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.domain.Mngr;
import com.scrm.why1139.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Json转义处理类。
 * @author why
 */
public class JSONProc
{
    /**
     * 解析Http协议请求数据段，获取Json对象
     * @param _req in httpReq
     * @return Json对象
     * @author why
     */
    public static JSONObject parseReq(HttpServletRequest _req)
    {
        return JSONObject.parseObject(JSON.toJSONString(_req.getParameterMap()));
    }

    /**
     * 解析登录请求，获取User对象
     * @param _req in httpReq
     * @return User对象
     * @author why
     */
    public static User parseLoginReq(HttpServletRequest _req)
    {
        User user = new User();
        JSONObject jsonLogin = parseReq(_req);
        user.setUserID((String) (jsonLogin.getJSONArray("userid").get(0)));
        user.setPassword((String)(jsonLogin.getJSONArray("passwd").get(0)));
        return user;
    }

    /**
     * 解析注册请求，获取User对象
     * @param _req in httpReq
     * @return User对象
     * @author why
     */
    public static User parseSignUpReq(HttpServletRequest _req)
    {
        User user = new User();
        JSONObject jsonSignUp = parseReq(_req);
        user.setUserID((String) (jsonSignUp.getJSONArray("userid").get(0)));
        user.setPassword((String)(jsonSignUp.getJSONArray("passwd").get(0)));
        user.setUserName((String)(jsonSignUp.getJSONArray("username").get(0)));
        return user;
    }


    /**
     * 解析注册预处理请求，获取User对象
     * @param _req in httpReq
     * @return User对象
     * @author why
     */
    public static User parseSignUpPreReq(HttpServletRequest _req)
    {
        User user = new User();
        JSONObject jsonSignUp = parseReq(_req);
        user.setUserID((String) (jsonSignUp.getJSONArray("userid").get(0)));
        return user;
    }

    /**
     * 解析Mngr登录请求，获取Mngr对象
     * @param _req in httpReq
     * @return Mngr对象
     * @author why
     */
    public static Mngr parseMngrVerifyReq(HttpServletRequest _req)
    {
        Mngr mngr = new Mngr();
        JSONObject jsonLogin = parseReq(_req);
        System.out.println(jsonLogin.toJSONString());
        mngr.setMngrID((String) (jsonLogin.getJSONArray("mngrid").get(0)));
        mngr.setPassword((String)(jsonLogin.getJSONArray("passwd").get(0)));
        return mngr;
    }

    /**
     * 处理新增Mngr的请求，获取Mngr对象
     * @param _req in httpReq
     * @return Mngr对象
     */
    public static Mngr parseMngrAddReq(HttpServletRequest _req)
    {
        return parseMngrVerifyReq(_req);
    }



}
