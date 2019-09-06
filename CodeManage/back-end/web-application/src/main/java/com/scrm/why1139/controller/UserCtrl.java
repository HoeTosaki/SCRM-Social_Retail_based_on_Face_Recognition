package com.scrm.why1139.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingxianginc.ctu.client.CaptchaClient;
import com.dingxianginc.ctu.client.model.CaptchaResponse;
import com.scrm.why1139.BioReferenceModule.BioCertificater;
import com.scrm.why1139.BioReferenceModule.BioSaver;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.User;
import com.scrm.why1139.service.ConfigConst;
import com.scrm.why1139.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class UserCtrl {
    @Autowired
    private UserService m_userService;

    /**
     * 映射到login界面的Controller方法
     * @return 页面模型
     * @author why
     */
    @RequestMapping(
            value = {"/login"}
    )
    public ModelAndView login()
    {
        return new ModelAndView("Web/login/html/login.html");
    }

    /**
     * 映射到login-face界面的Controller方法
     * @return 页面模型
     * @author why
     */
    @RequestMapping(
            value = {"/loginFace"}
    )
    public ModelAndView loginFace()
    {
        return new ModelAndView("Web/login/html/login-face.html");
    }


    /**
     * 映射到rcmd界面的Controller方法
     * @return 页面模型
     * @author why
     */
    @RequestMapping(
            value = {"/rcmd"}
    )
    public ModelAndView rcmd()
    {
        return new ModelAndView("Web/rcmd/html/rcmd.html");
    }


    /**
     * 映射到loginCheck的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/loginCheck"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String loginCheck(HttpServletRequest _req)
    {
        User user = JSONProc.parseLoginReq(_req);
        System.out.println(user.getUserID()+"\t"+user.getPassword());
        Map<String,Object> mp = new ConcurrentHashMap<>();

        if(m_userService.hasMatchUser(user.getUserID(),user.getPassword()))
        {
            mp.put("stat","success");
            mp.put("userid",user.getUserID());
        }
        else
        {
            mp.put("stat", "invalid");
        }
        JSONObject obj = new JSONObject(mp);
//        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    /**
     * 映射到loginCheckToken的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/loginCheckToken"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String loginCheckToken(HttpServletRequest _req,String token)
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

    /**
     * 映射到updateRecgBio的Controller方法
     * @return 请求响应String
     * @author why
     */
    @RequestMapping(
            value = {"/updateRecgBio"}
    )
    public String recgBioCer(HttpServletRequest _req, String imgrecg)
    {
        System.out.println(imgrecg);
        return "undefined";
    }

    /**
     * 映射到updateRecgBio的Controller方法
     * @return 请求响应String
     * @author why
     */
    @RequestMapping(
            value = {"/updatePasswd"}
    )
    public String updatePasswd(HttpServletRequest _req)
    {
        return "undefined";
    }

    /**
     * 映射到updatePasswdByRecgBio的Controller方法
     * @return 请求响应String
     * @author why
     */
    @RequestMapping(
            value = {"/updatePasswdByRecgBio"}
    )
    public String updatePasswdByRecgBio(HttpServletRequest _req)
    {
        return "undefined";
    }

    /**
     * 映射到updateRecgBioByPasswd的Controller方法
     * @return 请求响应String
     * @author why
     */
    @RequestMapping(
            value = {"/updateRecgBioByPasswd"}
    )
    public String updateRecgBioByPasswd(HttpServletRequest _req)
    {
        return "undefined";
    }



    @ResponseBody
    @RequestMapping(
            value = {"/userValQuery"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String userValQuery(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        System.out.println("控制器接入："+objReq.toJSONString());

        String strUserID = (String)(objReq.getJSONArray("userid").get(0));
        int cmd = Integer.parseInt( (String)(objReq.getJSONArray("cmd").get(0)));
        int offset = Integer.parseInt( (String)(objReq.getJSONArray("offset").get(0)));
        int limit = Integer.parseInt( (String)(objReq.getJSONArray("limit").get(0)));

        JSONArray arrArgs = new JSONArray();
        if(objReq.keySet().contains("args"))
        {
            /* wechat resolve*/
            arrArgs = JSONArray.parseArray(objReq.getJSONArray("args").get(0).toString());
        }
        else
        {
            /* controller resolve*/
            arrArgs = objReq.getJSONArray("args[]");
        }
        List<Object> lstVal = new CopyOnWriteArrayList<>();
        List<Object> lstArgs = new CopyOnWriteArrayList<>();
        JSONObject objRet = new JSONObject();

        switch(cmd)
        {
            case 5:
            {
                if(m_userService.getAnalyzeValForUser(strUserID,cmd,offset,limit,lstVal,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrVal = new JSONArray();
                    for(int i = 0;i<lstVal.size();++i)
                    {
                        if(i <= 3)
                            arrVal.add(lstVal.get(i));
                        else
                        {
                            arrVal.add((JSONObject)lstVal.get(i));
                        }
                    }
                    objRet.put("vals",arrVal);
                    objRet.put("args",(JSONArray)(lstArgs.get(lstArgs.size()-1)));
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;
            }
            case 6:
            {
                String args0 = arrArgs.getString(0);
                lstArgs.add(args0);
                if(m_userService.getAnalyzeValForUser(strUserID,cmd,offset,limit,lstVal,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrVal = new JSONArray();
                    for(int i = 0;i<lstVal.size();++i)
                    {
                        if(i <= 0)
                            arrVal.add(lstVal.get(i));
                        else
                        {
                            arrVal.add((JSONObject)lstVal.get(i));
                        }
                    }
                    objRet.put("vals",arrVal);
                    objRet.put("args",(JSONArray)(lstArgs.get(lstArgs.size()-1)));
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;
            }
            case 7:
            {
                String args0 = arrArgs.getString(0);
                String args1 = arrArgs.getString(1);
                lstArgs.add(args0);
                lstArgs.add(args1);
                if(m_userService.getAnalyzeValForUser(strUserID,cmd,offset,limit,lstVal,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrVal = new JSONArray();
                    for(int i = 0;i<lstVal.size();++i)
                    {
                        if(i <= 0)
                            arrVal.add(lstVal.get(i));
                        else
                        {
                            arrVal.add((JSONObject)lstVal.get(i));
                        }
                    }
                    objRet.put("vals",arrVal);
                    objRet.put("args",(JSONArray)(lstArgs.get(lstArgs.size()-1)));
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;
            }
            default:
            {
                objRet.put("stat","invalid");
            }
        }
        System.out.println("控制器返回："+objRet.toJSONString());
        return objRet.toJSONString();
    }

    @ResponseBody
    @RequestMapping(
            value = {"/userFigQuery"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String userFigQuery(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        System.out.println("控制器接入："+objReq.toJSONString());

        String strUserID = (String)(objReq.getJSONArray("userid").get(0));
        int cmd = Integer.parseInt( (String)(objReq.getJSONArray("cmd").get(0)));
        int limit = Integer.parseInt( (String)(objReq.getJSONArray("limit").get(0)));
        JSONArray arrArgs = new JSONArray();
        if(objReq.keySet().contains("args"))
        {
            /* wechat resolve*/
            arrArgs = JSONArray.parseArray(objReq.getJSONArray("args").get(0).toString());
        }
        else
        {
            /* controller resolve*/
            arrArgs = objReq.getJSONArray("args[]");
        }

        List<String> lstX = new CopyOnWriteArrayList<>();
        List<String> lstY = new CopyOnWriteArrayList<>();
        List<Object> lstArgs = new CopyOnWriteArrayList<>();
        JSONObject objRet = new JSONObject();

        switch(cmd)
        {
            case 0:
            {
                String args0 = arrArgs.getString(0);
                lstArgs.add(args0);
                if(m_userService.getAnalyzeFigForUser(strUserID,cmd,limit,lstX,lstY,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrX = new JSONArray();
                    JSONArray arrY = new JSONArray();
                    for(int i = 0;i<lstX.size();++i)
                    {
                        arrX.add(lstX.get(i));
                    }

                    for(int i = 0;i<lstY.size();++i)
                    {
                        arrY.add(lstY.get(i));
                    }
                    objRet.put("x",arrX);
                    objRet.put("y",arrY);
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;
            }
            case 1:
            {
                String args0 = arrArgs.getString(0);
                String args1 = arrArgs.getString(1);
                lstArgs.add(args0);
                lstArgs.add(args1);
                if(m_userService.getAnalyzeFigForUser(strUserID,cmd,limit,lstX,lstY,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrX = new JSONArray();
                    JSONArray arrY = new JSONArray();
                    for(int i = 0;i<lstX.size();++i)
                    {
                        arrX.add(lstX.get(i));
                    }

                    for(int i = 0;i<lstY.size();++i)
                    {
                        arrY.add(lstY.get(i));
                    }
                    objRet.put("x",arrX);
                    objRet.put("y",arrY);
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;

            }
            case 2:
            case 3:
            case 4:
            {
                if(m_userService.getAnalyzeFigForUser(strUserID,cmd,limit,lstX,lstY,lstArgs))
                {
                    objRet.put("stat","success");
                    JSONArray arrX = new JSONArray();
                    JSONArray arrY = new JSONArray();
                    for(int i = 0;i<lstX.size();++i)
                    {
                        arrX.add(lstX.get(i));
                    }

                    for(int i = 0;i<lstY.size();++i)
                    {
                        arrY.add(lstY.get(i));
                    }
                    objRet.put("x",arrX);
                    objRet.put("y",arrY);
                }
                else
                {
                    objRet.put("stat","invalid");
                }
                break;
            }
            default:
            {
                objRet.put("stat","invalid");
            }
        }
        System.out.println("控制器返回："+objRet.toJSONString());
        return objRet.toJSONString();
    }

    @ResponseBody
    @RequestMapping(
            value = {"/userGdsQuery"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String userGdsQuery(HttpServletRequest _req)
    {
        JSONObject jsonObj = JSONProc.parseReq(_req);
        String strUserID = (String) (jsonObj.getJSONArray("userid").get(0));
        int nCmd = Integer.parseInt((String) (jsonObj.getJSONArray("cmd").get(0)));

        JSONArray arrArgs = new JSONArray();
        if(jsonObj.keySet().contains("args"))
        {
            /* wechat resolve*/
            arrArgs = JSONArray.parseArray(jsonObj.getJSONArray("args").get(0).toString());
        }
        else
        {
            /* controller resolve*/
            arrArgs = jsonObj.getJSONArray("args[]");
        }

        Map<String,Object> mp = new ConcurrentHashMap<>();

        switch (nCmd)
        {
            case 0:
            {
                String args0 = arrArgs.getString(0);//id
                Goods gds = m_userService.findGoodsByGoodsID(Integer.parseInt(args0));
                if(gds == null ||gds.isEmpty())
                {
                    mp.put("stat", "invalid");
                }
                else
                {
                    mp.put("stat", "success");
                    JSONArray arrGds = new JSONArray();
                    JSONObject jsonGdsNew = new JSONObject();
                    jsonGdsNew.put("id",gds.getGoodsID());
                    jsonGdsNew.put("name",gds.getGoodsName());
                    jsonGdsNew.put("type",gds.getGoodsType());
                    jsonGdsNew.put("desc",gds.getGoodsDesc());
                    jsonGdsNew.put("cnt",gds.getGoodsCnt());
                    jsonGdsNew.put("prc",gds.getPrice());
                    jsonGdsNew.put("pic",gds.getPic());
                    arrGds.add(jsonGdsNew);
                    mp.put("gds",arrGds);
                }
                break;
            }
            case 1:
            {
                String args0 = arrArgs.getString(0);//name
                Goods gds = m_userService.findGoodsByGoodsName(args0);
                if(gds == null ||gds.isEmpty())
                {
                    mp.put("stat", "invalid");
                }
                else
                {
                    mp.put("stat", "success");
                    JSONArray arrGds = new JSONArray();
                    JSONObject jsonGdsNew = new JSONObject();
                    jsonGdsNew.put("id",gds.getGoodsID());
                    jsonGdsNew.put("name",gds.getGoodsName());
                    jsonGdsNew.put("type",gds.getGoodsType());
                    jsonGdsNew.put("desc",gds.getGoodsDesc());
                    jsonGdsNew.put("cnt",gds.getGoodsCnt());
                    jsonGdsNew.put("prc",gds.getPrice());
                    jsonGdsNew.put("pic",gds.getPic());
                    arrGds.add(jsonGdsNew);
                    mp.put("gds",arrGds);
                }
                break;
            }
            case 2:
            {
                mp.put("stat", "success");
                String args0 = arrArgs.getString(0);//type
                List<Goods> lstGds = m_userService.findGoodsByGoodsType(args0);
                if(lstGds == null || lstGds.isEmpty())
                {
                    mp.put("stat", "invalid");
                }
                else
                {
                    JSONArray arrGds = new JSONArray();
                    lstGds.parallelStream().map(gds->
                    {
                        JSONObject jsonGdsNew = new JSONObject();
                        jsonGdsNew.put("id",gds.getGoodsID());
                        jsonGdsNew.put("name",gds.getGoodsName());
                        jsonGdsNew.put("type",gds.getGoodsType());
                        jsonGdsNew.put("desc",gds.getGoodsDesc());
                        jsonGdsNew.put("cnt",gds.getGoodsCnt());
                        jsonGdsNew.put("prc",gds.getPrice());
                        jsonGdsNew.put("pic",gds.getPic());
                        return jsonGdsNew;
                    }).forEach(arrGds::add);
                    mp.put("gds",arrGds);
                }
                break;
            }
        }

        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    /**
     * 映射到rcmdPull的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/userRcmd"}
    )
    public String rcmdPull(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        System.out.println(objReq.toJSONString());
        String strUserID = (String)(objReq.getJSONArray("userid").get(0));
        int cmd = Integer.parseInt((String)(objReq.getJSONArray("cmd").get(0)));
        int offset = Integer.parseInt((String)(objReq.getJSONArray("offset").get(0)));
        int limit = Integer.parseInt((String)(objReq.getJSONArray("limit").get(0)));

        User user = m_userService.findUserByUserID(strUserID);
        JSONObject objRet = new JSONObject();

        if(user == null || user.isEmpty())
        {
            objRet.put("stat","invalid");
        }
        else
        {
            objRet.put("stat","success");
            JSONArray arrRet = new JSONArray();
            switch (cmd)
            {
                case 0:
                {
                    List<Goods> lstRcmd = m_userService.getGoodsRcmdGeneralForUser(user,offset,limit);
                    lstRcmd.stream().map(gds->{
                        JSONObject objGds = new JSONObject();
                        objGds.put("name",gds.getGoodsName());
                        objGds.put("prc",gds.getPrice());
                        objGds.put("cnt",gds.getGoodsCnt());
                        objGds.put("pic",gds.getPic());
                        objGds.put("desc",gds.getGoodsDesc());
                        objGds.put("id",gds.getGoodsID());
                        objGds.put("type",gds.getGoodsType());
                        return objGds;
                    }).forEach(arrRet::add);
                    break;
                }
                case 1:
                {
                    String type = (String)(objReq.getJSONArray("args").get(0));
                    List<Goods> lstRcmd = m_userService.getGoodsRcmdByTypeForUser(user,type,offset,limit);
                    lstRcmd.stream().map(gds->{
                        JSONObject objGds = new JSONObject();
                        objGds.put("name",gds.getGoodsName());
                        objGds.put("prc",gds.getPrice());
                        objGds.put("cnt",gds.getGoodsCnt());
                        objGds.put("pic",gds.getPic());
                        objGds.put("desc",gds.getGoodsDesc());
                        objGds.put("id",gds.getGoodsID());
                        objGds.put("type",gds.getGoodsType());
                        return objGds;
                    }).forEach(arrRet::add);
                    break;
                }
                case 2:
                {
                    int nGoodsID = Integer.parseInt((String)(objReq.getJSONArray("args").get(0)));
                    Goods curGds = m_userService.findGoodsByGoodsID(nGoodsID);
                    List<Goods> lstRcmd = m_userService.getGoodsRelatedForUser(curGds,offset,limit);
                    lstRcmd.stream().map(gds->{
                        JSONObject objGds = new JSONObject();
                        objGds.put("name",gds.getGoodsName());
                        objGds.put("prc",gds.getPrice());
                        objGds.put("cnt",gds.getGoodsCnt());
                        objGds.put("pic",gds.getPic());
                        objGds.put("desc",gds.getGoodsDesc());
                        objGds.put("id",gds.getGoodsID());
                        objGds.put("type",gds.getGoodsType());
                        return objGds;
                    }).forEach(arrRet::add);
                    break;
                }
            }
            objRet.put("gds_lst",arrRet);
        }

        System.out.println(objRet.toJSONString());
        return objRet.toJSONString();
    }

    /**
     * 映射到loginRecgBio的Controller方法
     * @return 请求响应String
     * @author why
     */
    @RequestMapping(value = "/loginRecgBio", method = RequestMethod.POST)
    @ResponseBody
    public String loginRecgBio(HttpServletResponse response, HttpServletRequest request, String imgrecg) throws Exception
    {
        System.out.println(imgrecg);
        List<String > lstUserID = BioCertificater.certificateUser(imgrecg);
        Map<String,Object> mp = new ConcurrentHashMap<>();
        if(lstUserID.size() == 0)
        {
            mp.put("stat","invalid1");
        }
        else
        {
            User user = m_userService.findUserByUserID(lstUserID.get(0));
            if(user == null || user.isEmpty())
            {
                mp.put("stat","invalid2");
            }
            else
            {
                mp.put("stat","success");
                mp.put("userid",user.getUserID());
                mp.put("username",user.getUserName());
            }
        }

        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    /**
     * 映射到signUpRecgBio的Controller方法
     * @return 请求响应String
     * @author why
     */
    @RequestMapping(
            value = {"/signUpRecgBio"}, method = RequestMethod.POST
    )
    public String signUpRecgBio(HttpServletResponse response, HttpServletRequest request, String imgrecg) throws Exception
    {
        Map<String,Object> mp = new ConcurrentHashMap<>();

        List<String > lstUserID = BioCertificater.certificateUser(imgrecg);
        if(lstUserID.size() != 0)
        {
            mp.put("stat","invalid");
            System.out.println("当前人脸已经被注册！");
        }
        else
        {
            String strNewUserID = m_userService.createNewUserID(ConfigConst.ID_TRY_LIMIT);
            List<String> lstImg = new CopyOnWriteArrayList<>();
            lstImg.add(imgrecg);
            if(BioSaver.saveUser(lstImg,strNewUserID,1))
            {
                //在数据库中注册用户
                if(m_userService.creatNewUserByRecgBio(strNewUserID))
                {
                    mp.put("stat","success");
                    mp.put("userid",strNewUserID);
                }
                else
                {
                    mp.put("stat","invalid");
                    System.out.println("数据库写入当前用户失败！用户id为"+strNewUserID);
                    BioSaver.delUser(strNewUserID);
                };
            }
            else
            {
                mp.put("stat","invalid");
            }
        }
        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    /**
     * 映射到delRecgBio的Controller方法
     * @return 请求响应String
     * @author why
     */
    @RequestMapping(
            value = {"/delRecgBio"}
    )
    public String delRecgBio(HttpServletRequest _req) throws Exception
    {
        //TODO:删除人脸信息的控制器逻辑。
        return "undefined";
    }



}