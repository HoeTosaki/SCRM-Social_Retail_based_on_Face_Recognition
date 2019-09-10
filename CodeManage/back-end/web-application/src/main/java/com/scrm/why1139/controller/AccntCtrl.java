package com.scrm.why1139.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.Mngr;
import com.scrm.why1139.domain.Order;
import com.scrm.why1139.domain.User;
import com.scrm.why1139.service.AccntService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 面向收银员的Controller类
 * @author 王浩宇
 * @date 9.1
 */
@RestController
public class AccntCtrl {
    @Autowired
    private AccntService m_accntService;

    /**
     * 映射到login-mngr界面的Controller方法
     * @return 页面模型
     * @author 王浩宇
     * @date 9.1
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
     * @author 王浩宇
     * @date 9.1
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
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/accntRecgMan"}, method = RequestMethod.POST
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
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/accntPay"}, method = RequestMethod.POST
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
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/accntGdsQuery"}, method = RequestMethod.POST
    )
    public String goodsQuery(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        JSONObject objRet = new JSONObject();

        if(!StringUtils.isNumeric((String)(objReq.getJSONArray("goodsid").get(0))) || !StringUtils.isNumeric((String)(objReq.getJSONArray("goodscnt").get(0))))
        {
            objRet.put("stat","invalid");
        }
        else
        {
            int nGoodsID = Integer.parseInt((String)(objReq.getJSONArray("goodsid").get(0)));
            int nGoodsCnt = Integer.parseInt((String)(objReq.getJSONArray("goodscnt").get(0)));
            Goods goods = m_accntService.findGoodsByGoodsID(nGoodsID);
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

        }

        System.out.println("send:"+objRet.toJSONString());
        return objRet.toJSONString();
    }

    /**
     * 映射到accntRcmd的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/accntRcmd"}, method = RequestMethod.POST
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

    /**
     * 映射到pullOrder的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @RequestMapping(
            value = {"/pullOrder"}, method = RequestMethod.POST
    )
    public String pullOrder(HttpServletRequest _req)
    {
        System.out.println("safe null");
//        JSONObject objReq = JSONProc.parseReq(_req);
        JSONObject objRet = new JSONObject();
        List<Order> orderLst = m_accntService.findOrder();
        if(orderLst == null || orderLst.isEmpty())
        {
            objRet.put("stat","invalid");
        }
        else
        {
            objRet.put("stat","success");
            JSONArray arrOrder = new JSONArray();
            orderLst.stream().map(ord->{
                JSONObject objOrd = new JSONObject();
                objOrd.put("order_id",ord.getOrderID());
                objOrd.put("user_id",ord.getUserID());
                objOrd.put("order_cnt",ord.getOrderCnt());
                int nGdsID = ord.getGoodsID();
                Goods gds = m_accntService.findGoodsByGoodsID(nGdsID);
                JSONObject objGds = new JSONObject();
                if(gds == null || gds.isEmpty())
                {
                    objGds.put("name","undef");
                    objGds.put("prc","-1.0");
                    objGds.put("cnt","-1");
                    objGds.put("pic","undef");
                    objGds.put("desc","undef");
                    objGds.put("id","-1");
                    objGds.put("type","undef");
                }
                else
                {
                    objGds.put("name",gds.getGoodsName());
                    objGds.put("prc",gds.getPrice());
                    objGds.put("cnt",gds.getGoodsCnt());
                    objGds.put("pic",gds.getPic());
                    objGds.put("desc",gds.getGoodsDesc());
                    objGds.put("id",gds.getGoodsID());
                    objGds.put("type",gds.getGoodsType());
                }
                objOrd.put("gds",objGds);
                return objOrd;
            }).forEach(arrOrder::add);

            objRet.put("order_lst",arrOrder);
        }
        System.out.println("控制器输出:\t"+objRet.toJSONString());
        return objRet.toJSONString();
    }

    /**
     * 映射到summitOrder的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @RequestMapping(
            value = {"/summitOrder"}, method = RequestMethod.POST
    )
    public String summitOrder(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        System.out.println("控制器输入：\t"+ objReq.toJSONString());
        JSONObject objRet = new JSONObject();

        String strUserID = (String)(objReq.getJSONArray("user_id").get(0));
        String strMngrID = (String)(objReq.getJSONArray("mngr_id").get(0));
        int nOrderID = Integer.parseInt((String)(objReq.getJSONArray("order_id").get(0)));
/**
 *          user_id
 *          order_id
 *          mngr_id
 */
        User user = m_accntService.findUserByUserID(strUserID);
        Mngr mngr = m_accntService.findMngrByMngrID(strMngrID);
        Order order = m_accntService.findOrderByOrderID(nOrderID);

        if(user == null || user.isEmpty() || mngr == null ||mngr.isEmpty() || order== null|| order.isEmpty())
        {
            objRet.put("stat","invalid");
        }
        else
        {
            objRet.put("stat","success");
            m_accntService.addBuyLog(mngr.getMngrID(),user.getUserID(),order.getGoodsID(),order.getOrderCnt());
            m_accntService.removeOrderByOrderIDForAccnt(order.getOrderID());
        }
        System.out.println("控制器输出:\t"+objRet.toJSONString());
        return objRet.toJSONString();
    }
}
