package com.scrm.why1139.web;

import com.alibaba.fastjson.JSONObject;
import com.dingxianginc.ctu.client.CaptchaClient;
import com.dingxianginc.ctu.client.model.CaptchaResponse;
import com.scrm.why1139.domain.Mngr;
import com.scrm.why1139.domain.User;
import com.scrm.why1139.service.AccntService;
import com.scrm.why1139.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户登录的Controller类。支持User的登录处理
 * @author why
 */
@RestController
public class SignUpWeb
{
    private UserService m_userService;
    private AccntService m_accntService;

    /**
     * setter注入
     * @param _userService in
     * @author why
     */
    @Autowired
    public void setUserService(UserService _userService)
    {
        this.m_userService=_userService;
    }

    /**
     * setter注入
     * @param _accntService in
     * @author why
     */
    @Autowired
    public void setMngrService(AccntService _accntService)
    {
        this.m_accntService = _accntService;
    }

    /**
     * 映射到signUpCheck的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/signUpCheck"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String signUpCheck(HttpServletRequest _req)
    {
        User user = JSONProc.parseSignUpReq(_req);
        System.out.println(user.getUserName()+"\t"+user.getUserID()+"\t"+user.getPassword());
        Map<String,Object> mp = new ConcurrentHashMap<>();
        if(!m_userService.findUserByUserID(user.getUserID()).isEmpty())
        {
            mp.put("stat","invalid1");//id已被注册。
        }
        else if(m_userService.creatNewUserByPasswd(user.getUserName(),user.getUserID(),user.getPassword()))
        {
            mp.put("stat","success");
            mp.put("userid",user.getUserID());
        }
        else
        {
            mp.put("stat","invalid2");//创建失败。
        }
        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    /**
     * 映射到signUpCheckPre的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/signUpCheckPre"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String signUpCheckPre(HttpServletRequest _req)
    {
        User user = JSONProc.parseSignUpPreReq(_req);
        System.out.println(user.getUserID());
        Map<String,Object> mp = new ConcurrentHashMap<>();
        if(!m_userService.findUserByUserID(user.getUserID()).isEmpty())
        {
            mp.put("stat","invalid");
        }
        else
        {
            mp.put("stat","success");
        }
        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    /**
     * 映射到signUpCheckToken的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/signUpCheckToken"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String signUpCheckToken(HttpServletRequest _req,String token)
    {
        System.out.println(token);
        Map<String,Object> mp = new ConcurrentHashMap<>();
        /**构造入参为appId和appSecret
         * appId和前端验证码的appId保持一致，appId可公开
         * appSecret为秘钥，请勿公开
         * token在前端完成验证后可以获取到，随业务请求发送到后台，token有效期为两分钟
         * ip 可选，提交业务参数的客户端ip
         **/
        String appId = "dfbcdad62304cb024d8a02efcc126200";
        String appSecret = "2f0f2a714f43e9872d77110b0b4c73a8";
        CaptchaClient captchaClient = new CaptchaClient(appId,appSecret);
        //captchaClient.setCaptchaUrl(captchaUrl);
        //特殊情况需要额外指定服务器,可以在这个指定，默认情况下不需要设置
        CaptchaResponse response = null;
        try
        {
            response = captchaClient.verifyToken(token);
            //CaptchaResponse response = captchaClient.verifyToken(token, ip);
            //针对一些token冒用的情况，业务方可以采集客户端ip随token一起提交到验证码服务，验证码服务除了判断token的合法性还会校验提交业务参数的客户端ip和验证码颁发token的客户端ip是否一致
            System.out.println(response.getCaptchaStatus());
            //确保验证状态是SERVER_SUCCESS，SDK中有容错机制，在网络出现异常的情况会返回通过
            //System.out.println(response.getIp());
            //验证码服务采集到的客户端ip
            if (response.getResult())
            {
                /**token验证通过，继续其他流程**/
                mp.put("stat","success");
            }
            else
            {
                /**token验证失败，业务系统可以直接阻断该次请求或者继续弹验证码**/
                mp.put("stat","invalid");
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }



}
