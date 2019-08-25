package com.scrm.why1139.web;

import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.BioReferenceModule.BioCertificater;
import com.scrm.why1139.BioReferenceModule.BioSaver;
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
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 对人脸识别内容的响应controller类
 * 同时向小程序、收银平台提供服务。
 * @author why
 */
@RestController
public class RecgBioWeb
{
    private UserService m_userService;

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
     * 映射到login-face界面的Controller方法
     * @return 页面模型
     * @author why
     */
    @RequestMapping(
            value = {"/loginFace"}
    )
    public ModelAndView loginFace()
    {
        return new ModelAndView("Web/html/login-face.html");
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
            mp.put("stat","invalid");
        }
        else
        {
            User user = m_userService.findUserByUserID(lstUserID.get(0));
            if(user == null || user.isEmpty())
            {
                mp.put("stat","invalid");
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


    /**
     * 根据Base64编码的人脸信息生成图片
     * @param imgStr in Base格式人脸信息
     * @return 生成是否成功的boolean值
     * @author why
     */
    public static boolean GenerateImage(String imgStr)
    {
        // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
//        else
//            System.out.println(imgStr);
        System.out.println(imgStr);
//        BASE64Decoder decoder = new BASE64Decoder();
        try {
// Base64解码
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] b = decoder.decode(imgStr);
//            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
// 生成jpeg图片
            String imgFilePath = "why1.png";// 新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
