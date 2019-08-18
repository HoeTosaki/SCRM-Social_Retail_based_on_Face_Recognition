package com.scrm.why1139.web;

import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.domain.Mngr;
import com.scrm.why1139.domain.User;
import com.scrm.why1139.service.MngrService;
import com.scrm.why1139.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@RestController
public class LoginWeb
{
    private UserService m_userService;
    private MngrService m_mngrService;

    @Autowired
    public void setUserService(UserService _userService)
    {
        this.m_userService=_userService;
    }

    @Autowired
    public void setMngrService(MngrService _mngrService)
    {
        this.m_mngrService=_mngrService;
    }


    @RequestMapping(
            value = {"/login"}
    )
    public ModelAndView login()
    {
        return new ModelAndView("Web/html/login.html");
    }

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

        if(m_mngrService.hasMatchMngr(user.getUserID(),user.getPassword()))
        {
            Mngr mngr = m_mngrService.findMngrByMngrID(user.getUserID());
            if(mngr.getMngrType() == 0)
            {
                /*super admin*/
                mp.put("stat","admin");
                mp.put("mngrid",mngr.getMngrID());
            }
            else if(mngr.getMngrType() == 1)
            {
                /*admin*/
                mp.put("stat","admin");
                mp.put("mngrid",mngr.getMngrID());
            }
            else if(mngr.getMngrType() == 2)
            {
                /*accnt*/
                mp.put("stat","accnt");
                mp.put("mngrid",mngr.getMngrID());
            }
            else
            {
                /*user / customer wrong*/
                mp.put("stat","fatal");
            }
        }
        else if(m_userService.hasMatchUser(user.getUserID(),user.getPassword()))
        {
            mp.put("stat","user");
            mp.put("userid",user.getUserID());
        }
        else
        {
            mp.put("stat", "invalid");
        }
        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

}
