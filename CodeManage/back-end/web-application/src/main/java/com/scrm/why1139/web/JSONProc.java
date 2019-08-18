package com.scrm.why1139.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

public class JSONProc
{
    public static JSONObject parseReq(HttpServletRequest _req)
    {
        return JSONObject.parseObject(JSON.toJSONString(_req.getParameterMap()));
    }

    public static User parseLoginReq(HttpServletRequest _req)
    {
        User user = new User();
        JSONObject jsonLogin = parseReq(_req);
        user.setUserID((String) (jsonLogin.getJSONArray("userid").get(0)));
        user.setPassword((String)(jsonLogin.getJSONArray("passwd").get(0)));
        return user;
    }

}
