package com.scrm.why1139.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

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

}
