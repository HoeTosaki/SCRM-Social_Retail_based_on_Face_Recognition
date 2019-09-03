package com.scrm.why1139.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.domain.Mngr;
import com.scrm.why1139.domain.User;
import com.scrm.why1139.service.AccntService;
import com.scrm.why1139.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mngr操作的的Controller类。
 * @author why
 */
@RestController
public class MngrOperWeb
{
    private AccntService m_accntService;

    @Autowired
    private AdminService m_adminService;
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
     * 映射到login-mngr界面的Controller方法
     * @return 页面模型
     * @author why
     */
    @RequestMapping(
            value = {"/loginMngr"}
    )
    public ModelAndView loginMngr()
    {
        return new ModelAndView("Web/login/html/login-mngr.html");
    }

    /**
 * 映射到mngrVerify的Controller方法
 * @return 请求响应String
 * @author why
 */
@ResponseBody
@RequestMapping(
        value = {"/mngrVerify"}/*,
            produces = "application/json;charset=UTF-8"*/
)
public String mngrVerify(HttpServletRequest _req)
{
    Mngr mngr = JSONProc.parseMngrVerifyReq(_req);
    System.out.println(mngr.getMngrID()+"\t"+mngr.getPassword());

    Map<String,Object> mp = new ConcurrentHashMap<>();

    if(m_accntService.hasMatchMngr(mngr.getMngrID(),mngr.getPassword()))
    {
        mngr = m_accntService.findMngrByMngrID(mngr.getMngrID());
        if(mngr.getMngrType() == 0)
        {
            /*super admin*/
            mp.put("stat","success");
            mp.put("mngrtype","0");
            mp.put("mngrid",mngr.getMngrID());
        }
        else if(mngr.getMngrType() == 1)
        {
            /*admin*/
            mp.put("stat","success");
            mp.put("mngrtype","1");
            mp.put("mngrid",mngr.getMngrID());
        }
        else if(mngr.getMngrType() == 2)
        {
            /*accnt*/
            mp.put("stat","success");
            mp.put("mngrtype","2");
            mp.put("mngrid",mngr.getMngrID());
        }
        else
        {
            /*user / customer wrong*/
            mp.put("stat","invalid");
        }
    }
    else
    {
        mp.put("stat","invalid");
    }
    JSONObject obj = new JSONObject(mp);
//        System.out.println("ret:"+obj.toJSONString());
    return obj.toJSONString();
}

    /**
     * 映射到mngrUpdate的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/mngrUpdate"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String mngrUpdate(HttpServletRequest _req)
    {
        JSONObject jsonLogin = JSONProc.parseReq(_req);
        String strMngrID = (String) (jsonLogin.getJSONArray("mngrid").get(0));
        String newPasswd = (String)(jsonLogin.getJSONArray("passwd").get(0));

        System.out.println(strMngrID+"\t"+newPasswd);

        Map<String,Object> mp = new ConcurrentHashMap<>();

        Mngr mngr = m_accntService.findMngrByMngrID(strMngrID);
        if(mngr == null || mngr.isEmpty())
        {
            mp.put("stat","invalid");
        }
        else
        {
            Mngr newMngr = new Mngr();
            newMngr.setMngrID(mngr.getMngrID());
            newMngr.setPassword(newPasswd);
            newMngr.setMngrType(mngr.getMngrType());
            m_accntService.updateMngr(newMngr);
            mp.put("stat","success");
            mp.put("mngrid",newMngr.getMngrID());
        }

        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    /**
     * 映射到mngrDel的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/mngrDel"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String mngrDel(HttpServletRequest _req)
    {
        JSONObject jsonLogin = JSONProc.parseReq(_req);
        String strMngrID = (String) (jsonLogin.getJSONArray("mngrid").get(0));

        System.out.println(strMngrID);

        Map<String,Object> mp = new ConcurrentHashMap<>();

        Mngr mngr = m_accntService.findMngrByMngrID(strMngrID);

        if(mngr == null || mngr.isEmpty())
        {
            mp.put("stat","invalid");
        }
        else
        {
            m_accntService.delMngr(mngr);
            mp.put("stat","success");
            mp.put("mngrid",mngr.getMngrID());
        }

        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    /**
     * 映射到mngrAdd的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/mngrAdd"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String mngrAdd(HttpServletRequest _req)
    {
        Mngr mngr = JSONProc.parseMngrAddReq(_req);
        System.out.println(mngr.getMngrID()+"\t"+mngr.getPassword());
        Map<String,Object> mp = new ConcurrentHashMap<>();
        if(!m_accntService.findMngrByMngrID(mngr.getMngrID()).isEmpty())
        {
            mp.put("stat","invalid1");//id已被注册。
        }
        else if(m_accntService.creatNewAccnt(mngr.getMngrID(),mngr.getPassword()))
        {
            mp.put("stat","success");
            mp.put("userid",mngr.getMngrID());
        }
        else
        {
            mp.put("stat","invalid2");//创建失败。
        }
        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    @ResponseBody
    @RequestMapping(
            value = {"/mngrPullPre"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String mngrPullPre(HttpServletRequest _req)
    {
        Map<String,Object> mp = new ConcurrentHashMap<>();
        List<Mngr> lstMngr = m_adminService.getMngrAll();

        if(lstMngr == null)
        {
            mp.put("stat","invalid");
        }
        else
        {
            mp.put("stat","success");
            JSONArray arrMngrID = new JSONArray();
            lstMngr.parallelStream().map(mngr->mngr.getMngrID()).forEach(arrMngrID::add);
            mp.put("mngrid_lst",arrMngrID);
        }

        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    @ResponseBody
    @RequestMapping(
            value = {"/adminUpdate"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String adminUpdate(HttpServletRequest _req)
    {
        JSONObject jsonObj = JSONProc.parseReq(_req);

        String strMngrID = (String) (jsonObj.getJSONArray("mngrid").get(0));
        String passwd = (String)(jsonObj.getJSONArray("passwd").get(0));
        String newPasswd = (String)(jsonObj.getJSONArray("newpasswd").get(0));
        System.out.println(strMngrID+"\t"+passwd+"\t"+newPasswd);

        Map<String,Object> mp = new ConcurrentHashMap<>();

        if(m_accntService.hasMatchMngr(strMngrID,passwd))
        {
            mp.put("stat","success");
            Mngr mngr = m_accntService.findMngrByMngrID(strMngrID);
            mngr.setPassword(newPasswd);
            m_adminService.updateMngr(mngr);
        }
        else
        {
            mp.put("stat","invalid");
        }

        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }


}
