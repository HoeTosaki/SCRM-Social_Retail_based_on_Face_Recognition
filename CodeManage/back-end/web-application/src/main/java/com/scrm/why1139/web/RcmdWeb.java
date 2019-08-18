package com.scrm.why1139.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.User;
import com.scrm.why1139.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class RcmdWeb
{
    private UserService m_userService;

    @Autowired
    private void setUserService(UserService _userService)
    {
        m_userService = _userService;
    }

    @RequestMapping(
            value = {"/rcmd"}
    )
    public ModelAndView rcmd()
    {
        return new ModelAndView("Web/html/rcmd.html");
    }

    @ResponseBody
    @RequestMapping(
            value = {"/rcmdPull"}
    )
    public String rcmdPull(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        String strUserID = (String)(objReq.getJSONArray("userid").get(0));
//        int nPointer = (String)(objReq.getJSONArray("pointer").get(0));
        User user = m_userService.findUserByUserID(strUserID);
        List<Goods> lstRcmd = m_userService.getGoodsRcmd(user);
        JSONArray arrRet = new JSONArray();
        lstRcmd.parallelStream().map(gds->{
            JSONObject objRet = new JSONObject();
            objRet.put("title",gds.getGoodsName());
            objRet.put("src",gds.getPic());
            objRet.put("height", ""+(Math.random()*100 + 50));
            objRet.put("width",122.5);

            return objRet;
        }).map(obj->(Object)obj).forEach(arrRet::add);
        return arrRet.toJSONString();
    }

}