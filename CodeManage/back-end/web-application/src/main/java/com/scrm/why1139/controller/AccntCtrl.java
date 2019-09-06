package com.scrm.why1139.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.User;
import com.scrm.why1139.service.AccntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class AccntCtrl {
    @Autowired
    private AccntService m_accntService;

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
     * 映射到accnt界面的Controller方法
     * @return 页面模型
     * @author why
     */
    @RequestMapping(
            value = {"/accnt"}
    )
    public ModelAndView accnt()
    {
        return new ModelAndView("Web/accnt/html/accnt.html");
    }

    /**
     * 映射到accntRecgMan的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/accntRecgMan"}
    )
    public String accntRecgMan(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        String strUserID = (String)(objReq.getJSONArray("userid").get(0));
        User user = m_accntService.findUserByUserID(strUserID);
        JSONObject objRet = new JSONObject();
        if(user == null || user.getUserID() == null || user.getUserID().trim().equals(""))
        {
            objRet.put("stat","invalid");
        }
        else
        {
            objRet.put("stat","completed");
            objRet.put("userid",user.getUserID());
            objRet.put("username",user.getUserName());
        }
        System.out.println("recv:"+strUserID);
        System.out.println("recv:"+user);
        System.out.println("find:"+ m_accntService.findUserByUserID("why"));
        System.out.println("match:"+ m_accntService.findMngrByMngrID("Tosaki"));
        System.out.println("send:"+objRet.toJSONString());
        return objRet.toJSONString();
    }

    /**
     * 映射到accntPay的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/accntPay"}
    )
    public String pay(HttpServletRequest _req)
    {
//        JSONObject objReq = JSONProc.parseReq(_req);
//        JSONArray arrUserID = objReq.getJSONArray("userid[]");
//        JSONArray arrAccntID = objReq.getJSONArray("accntid");
//        JSONArray arrGoodsID = objReq.getJSONArray("goodsid");
//        JSONArray arrGoodsCnt = objReq.getJSONArray("goodscnt");
//        _req.get
        JSONObject objReq = JSONProc.parseReq(_req);
//        String strUserID = (String)(objReq.getJSONArray("userid").get(0));
//        String strAccntID = (String)(objReq.getJSONArray("accntid").get(0));
//        int nGoodsID = Integer.parseInt((String)(objReq.getJSONArray("goodsid").get(0)));
//        int nGoodsCnt = Integer.parseInt((String)(objReq.getJSONArray("goodscnt").get(0)));

        String strUserID = (String)(objReq.getJSONArray("userid").get(0));
        String strAccntID = (String)(objReq.getJSONArray("accntid").get(0));
        int nGoodsID = Integer.parseInt((String)(objReq.getJSONArray("goodsid").get(0)));
        int nGoodsCnt = Integer.parseInt((String)(objReq.getJSONArray("goodscnt").get(0)));

        System.out.println(strUserID);
        System.out.println(strAccntID);
        System.out.println(nGoodsID);
        System.out.println(nGoodsCnt);

        m_accntService.addBuyLog(strAccntID,strUserID,nGoodsID,nGoodsCnt);
        JSONObject objRet = new JSONObject();
        objRet.put("stat","success");
        return objRet.toJSONString();
    }

    /**
     * 映射到accntGdsQuery的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/accntGdsQuery"}
    )
    public String goodsQuery(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        int nGoodsID = Integer.parseInt((String)(objReq.getJSONArray("goodsid").get(0)));
        int nGoodsCnt = Integer.parseInt((String)(objReq.getJSONArray("goodscnt").get(0)));
        Goods goods = m_accntService.findGoodsByGoodsID(nGoodsID);
        JSONObject objRet = new JSONObject();
        if(goods == null ||goods.getGoodsID() == 0)
        {
            objRet.put("stat","invalid");
        }
        else
        {
            objRet.put("stat","success");
            objRet.put("name",goods.getGoodsName());
            objRet.put("id",goods.getGoodsID());
            objRet.put("img",goods.getPic());
            objRet.put("prc",goods.getPrice());
            objRet.put("desc",goods.getGoodsDesc());
            objRet.put("cnt",nGoodsCnt);
//            objRet.put("type",goods.getGoodsType());
//            //TODO:Oach！！ i forgot to add this in html...su ma a
        }
        System.out.println("recv:"+nGoodsID);
        System.out.println("recv:"+nGoodsCnt);
        System.out.println("recv:"+goods);
        System.out.println("recv:"+objReq.toJSONString());
        System.out.println("send:"+objRet.toJSONString());
        return objRet.toJSONString();
    }

    /**
     * 映射到accntRcmd的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/accntRcmd"}
    )
    public String accntRcmd(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        String strUserID = (String)(objReq.getJSONArray("userid").get(0));
//        int nPointer = (String)(objReq.getJSONArray("pointer").get(0));
        User user = m_accntService.findUserByUserID(strUserID);
        if(user == null || user.getUserID() == null || user.getUserID().trim().equals(""))
        {
            JSONObject objRet = new JSONObject();
            objRet.put("stat","invalid");
            return objRet.toJSONString();
        }
        List<Goods> lstRcmd = m_accntService.getGoodsRcmdForUser(user);
        if(lstRcmd == null || lstRcmd.size() == 0)
        {
            JSONObject objRet = new JSONObject();
            objRet.put("stat","invalid");
            return objRet.toJSONString();
        }
        JSONArray arrRet = new JSONArray();
        lstRcmd.parallelStream().map(gds->{
            JSONObject objRet = new JSONObject();
            objRet.put("id",gds.getGoodsID());
            objRet.put("name",gds.getGoodsName());
            objRet.put("prc",gds.getPrice());
            objRet.put("img",gds.getPic());
            objRet.put("desc",gds.getGoodsDesc());
            return objRet;
        }).map(obj->(Object)obj).forEach(arrRet::add);

        System.out.println("send:"+strUserID);
        System.out.println("send:"+arrRet.toJSONString());
        return arrRet.toJSONString();
    }
}
