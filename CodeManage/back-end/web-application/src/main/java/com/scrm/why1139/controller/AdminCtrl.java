package com.scrm.why1139.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.Mngr;
import com.scrm.why1139.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 面向管理员的控制器类
 * @author 王浩宇
 * @date 9.1
 */
@RestController
public class AdminCtrl {
    @Autowired
    private AdminService m_adminService;

    /**
     * 映射到admin界面的Controller方法
     * @return 页面模型
     * @author 王浩宇
     * @date 9.1
     */
    @RequestMapping(
            value = {"/admin"}
    )
    public ModelAndView testAnal()
    {
        return new ModelAndView("Web/admin/html/admin.html");
    }

    /**
     * 映射到adminValQuery的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/adminValQuery"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String adminValQuery(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        System.out.println("控制器接入："+objReq.toJSONString());
        System.out.println("wun");
        String strMngrID = (String)(objReq.getJSONArray("mngrid").get(0));
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

        Mngr mngr = m_adminService.findMngrByMngrID(strMngrID);
        if(mngr == null ||mngr.isEmpty())
        {
            objRet.put("stat","invalid/no mngr");
        }
        else
        {
            switch(cmd)
            {
                case 13:
                {
                    if(m_adminService.getBuyListAnalyzeValForAdmin(cmd,offset,limit,lstVal,lstArgs))
                    {
                        objRet.put("stat","success");
                        JSONArray arrVal = new JSONArray();
                        for(int i = 0;i<lstVal.size();++i)
                        {
                            if(i <= 4)
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
                        objRet.put("stat","invalid/no val");
                    }
                    break;
                }
                case 14:
                {
                    String args0 = arrArgs.getString(0);
                    lstArgs.add(args0);
                    if(m_adminService.getBuyListAnalyzeValForAdmin(cmd,offset,limit,lstVal,lstArgs))
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
                case 15:
                {
                    String args0 = arrArgs.getString(0);
                    String args1 = arrArgs.getString(1);
                    lstArgs.add(args0);
                    lstArgs.add(args1);
                    if(m_adminService.getBuyListAnalyzeValForAdmin(cmd,offset,limit,lstVal,lstArgs))
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

        }
        System.out.println("控制器返回："+objRet.toJSONString());
        return objRet.toJSONString();
    }

    /**
     * 映射到adminFigQuery的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/adminFigQuery"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String adminFigQuery(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        System.out.println("控制器接入："+objReq.toJSONString());

        String strMngrID = (String)(objReq.getJSONArray("mngrid").get(0));
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
            case 8:
            {
                String args0 = arrArgs.getString(0);
                String args1 = arrArgs.getString(1);
                lstArgs.add(args0);
                lstArgs.add(args1);
                if(m_adminService.getBuyListAnalyzeFigForAdmin(cmd,limit,lstX,lstY,lstArgs))
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
            case 9:
            {
                if(m_adminService.getBuyListAnalyzeFigForAdmin(cmd,limit,lstX,lstY,lstArgs))
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
            case 10:
            case 11:
            case 12:
            {
                String args0 = arrArgs.getString(0);
                String args1 = arrArgs.getString(1);
                String args2 = arrArgs.getString(2);
                String args3 = arrArgs.getString(3);
                lstArgs.add(args0);
                lstArgs.add(args1);
                lstArgs.add(args2);
                lstArgs.add(args3);
                if(m_adminService.getBuyListAnalyzeFigForAdmin(cmd,limit,lstX,lstY,lstArgs))
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

    /**
     * 映射到goodsAnalPull的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/goodsAnalPull"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String goodsAnalPull(HttpServletRequest _req)
    {
        System.out.println("控制器接入："+"safe null");
        JSONObject objRet = new JSONObject();

        List<Goods> gds1 = new CopyOnWriteArrayList<>();
        List<Goods> gds2 = new CopyOnWriteArrayList<>();
        List<Double> relNum = new CopyOnWriteArrayList<>();
        List<Goods> gds = new CopyOnWriteArrayList<>();
        List<Integer> buyCnt = new CopyOnWriteArrayList<>();
        List<Double> cntNum = new CopyOnWriteArrayList<>();

        if(m_adminService.getGoodsAnalyzeForAdmin(gds1,gds2,relNum,gds,buyCnt,cntNum))
        {
            objRet.put("stat","success");
            JSONArray arrRel = new JSONArray();
            JSONArray arrGds = new JSONArray();
            for(int i = 0;i < gds1.size();++i)
            {
                JSONObject objRel = new JSONObject();
                objRel.put("gds1_name",gds1.get(i).getGoodsName());
                objRel.put("gds1_id",gds1.get(i).getGoodsID());
                objRel.put("gds2_name",gds2.get(i).getGoodsName());
                objRel.put("gds2_id",gds2.get(i).getGoodsID());
                objRel.put("rel_num",relNum.get(i));
                arrRel.add(objRel);
            }
            for(int i = 0; i < gds.size();++i)
            {
                JSONObject objGds = new JSONObject();
                objGds.put("gds_name",gds.get(i).getGoodsName());
                objGds.put("gds_id",gds.get(i).getGoodsID());
                objGds.put("buy_cnt",buyCnt.get(i));
                objGds.put("cnt_num",cntNum.get(i));
                objGds.put("gds_prc",gds.get(i).getPrice());
                objGds.put("gds_type",gds.get(i).getGoodsType());
                objGds.put("gds_cnt",gds.get(i).getGoodsCnt());


                arrGds.add(objGds);
            }
            objRet.put("gds_lst",arrGds);
            objRet.put("rel_lst",arrRel);
        }
        else
        {
            objRet.put("stat","invalid");
        }

        System.out.println("控制器返回："+objRet.toJSONString());
        return objRet.toJSONString();
    }

    /**
     * 映射到accntCntPullFig的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/accntCntPullFig"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String accntCntPullFig(HttpServletRequest _req)
    {
        System.out.println("控制器接入："+"safe null");
        JSONObject objRet = new JSONObject();

        List<Mngr> mngr = new CopyOnWriteArrayList<>();
        List<String> sale = new CopyOnWriteArrayList<>();

        if(m_adminService.getMngrAnalyzeFigForAdmin(mngr,sale))
        {
            objRet.put("stat","success");
            JSONArray arrMngr = new JSONArray();
            JSONArray arrSale = new JSONArray();
            mngr.stream().map(mn->mn.getMngrID()).forEach(arrMngr::add);
            sale.stream().forEach(arrSale::add);
            objRet.put("x",arrMngr);
            objRet.put("y",arrSale);
        }
        else
        {
            objRet.put("stat","invalid");
        }

        System.out.println("控制器返回："+objRet.toJSONString());
        return objRet.toJSONString();
    }

    /**
     * 映射到gdsUpdate的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/gdsUpdate"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String gdsUpdate(HttpServletRequest _req)
    {
        JSONObject jsonObj = JSONProc.parseReq(_req);
        JSONObject jsonGds = jsonObj.getJSONObject("gds");
        Goods gds = new Goods();
        gds.setGoodsID(jsonGds.getInteger("id"));
        gds.setGoodsName(jsonGds.getString("name"));
        gds.setGoodsType(jsonGds.getString("type"));
        gds.setGoodsDesc(jsonGds.getString("desc"));
        gds.setGoodsCnt(jsonGds.getInteger("cnt"));
        gds.setPic(jsonGds.getString("pic"));
        gds.setPrice(jsonGds.getDouble("prc"));


        Map<String,Object> mp = new ConcurrentHashMap<>();

        Goods gdsFind = m_adminService.findGoodsByGoodsID(gds.getGoodsID());
        if(gdsFind == null || gdsFind.isEmpty())
        {
            mp.put("stat","invalid");
        }
        else
        {
            mp.put("stat","success");
            m_adminService.updateGoods(gds);
        }

        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    /**
     * 映射到gdsDel的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/gdsDel"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String gdsDel(HttpServletRequest _req)
    {
        JSONObject jsonObj = JSONProc.parseReq(_req);
        int nGoodsID = Integer.parseInt((String) (jsonObj.getJSONArray("goodsid").get(0)));

        System.out.println(nGoodsID);

        Map<String,Object> mp = new ConcurrentHashMap<>();

        Goods gds = m_adminService.findGoodsByGoodsID(nGoodsID);

        if(gds == null || gds.isEmpty())
        {
            mp.put("stat","invalid");
        }
        else
        {
            mp.put("stat","success");
            m_adminService.delGoods(gds);
        }

        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    /**
     * 映射到gdsAdd的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/gdsAdd"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String gdsAdd(HttpServletRequest _req)
    {
        JSONObject jsonObj = JSONProc.parseReq(_req);
        System.out.println(jsonObj.toJSONString());
        Goods gds = new Goods();
        gds.setGoodsID(Integer.parseInt((String)jsonObj.getJSONArray("gds[id]").get(0)));
        gds.setGoodsName((String)jsonObj.getJSONArray("gds[name]").get(0));
        gds.setGoodsType((String)jsonObj.getJSONArray("gds[type]").get(0));
        gds.setGoodsDesc((String)jsonObj.getJSONArray("gds[desc]").get(0));
        gds.setGoodsCnt(Integer.parseInt((String)jsonObj.getJSONArray("gds[cnt]").get(0)));
        gds.setPic((String)jsonObj.getJSONArray("gds[pic]").get(0));
        gds.setPrice(Double.parseDouble((String)jsonObj.getJSONArray("gds[prc]").get(0)));

        System.out.println("id:\t"+gds.getGoodsID()+"name:\t"+gds.getGoodsName()+"type:\t"+gds.getGoodsType()+"prc:\t"+gds.getPrice()+"pic:\t"+gds.getPic()+"desc:\t"+gds.getGoodsDesc());

        Map<String,Object> mp = new ConcurrentHashMap<>();
        if(!m_adminService.findGoodsByGoodsID(gds.getGoodsID()).isEmpty())
        {
            mp.put("stat","invalid1");//id已被注册。
        }
        else
        {
            mp.put("stat","success");
            m_adminService.addGoods(gds);
        }
        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    /**
     * 映射到gdsPullPre的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/gdsPullPre"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String gdsPullPre(HttpServletRequest _req)
    {
        JSONObject jsonObj = JSONProc.parseReq(_req);
        int nGoodsID = Integer.parseInt((String) (jsonObj.getJSONArray("goodsid").get(0)));


        Map<String,Object> mp = new ConcurrentHashMap<>();
        Goods gdsPre = m_adminService.findGoodsByGoodsID(nGoodsID);

        if(gdsPre == null || gdsPre.isEmpty())
        {
            mp.put("stat","invalid");
        }
        else
        {
            mp.put("stat","success");
            JSONObject jsonGdsNew = new JSONObject();
            jsonGdsNew.put("id",gdsPre.getGoodsID());
            jsonGdsNew.put("name",gdsPre.getGoodsName());
            jsonGdsNew.put("type",gdsPre.getGoodsType());
            jsonGdsNew.put("desc",gdsPre.getGoodsDesc());
            jsonGdsNew.put("cnt",gdsPre.getGoodsCnt());
            jsonGdsNew.put("prc",gdsPre.getPrice());
            jsonGdsNew.put("pic",gdsPre.getPic());
            mp.put("gds",jsonGdsNew);
        }

        JSONObject obj = new JSONObject(mp);
        System.out.println("ret:"+obj.toJSONString());
        return obj.toJSONString();
    }

    /**
     * 映射到mngrVerify的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/mngrVerify"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String mngrVerify(HttpServletRequest _req)
    {
        Mngr mngr = JSONProc.parseMngrVerifyReq(_req);
        System.out.println(mngr.getMngrID()+"\t"+mngr.getPassword());

        Map<String,Object> mp = new ConcurrentHashMap<>();

        if(m_adminService.hasMatchMngr(mngr.getMngrID(),mngr.getPassword()))
        {
            mngr = m_adminService.findMngrByMngrID(mngr.getMngrID());
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
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/mngrUpdate"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String mngrUpdate(HttpServletRequest _req)
    {
        JSONObject jsonLogin = JSONProc.parseReq(_req);
        String strMngrID = (String) (jsonLogin.getJSONArray("mngrid").get(0));
        String newPasswd = (String)(jsonLogin.getJSONArray("passwd").get(0));

        System.out.println(strMngrID+"\t"+newPasswd);

        Map<String,Object> mp = new ConcurrentHashMap<>();

        Mngr mngr = m_adminService.findMngrByMngrID(strMngrID);
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
            m_adminService.updateMngr(newMngr);
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
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/mngrDel"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String mngrDel(HttpServletRequest _req)
    {
        JSONObject jsonLogin = JSONProc.parseReq(_req);
        String strMngrID = (String) (jsonLogin.getJSONArray("mngrid").get(0));

        System.out.println(strMngrID);

        Map<String,Object> mp = new ConcurrentHashMap<>();

        Mngr mngr = m_adminService.findMngrByMngrID(strMngrID);

        if(mngr == null || mngr.isEmpty())
        {
            mp.put("stat","invalid");
        }
        else
        {
            m_adminService.delMngr(mngr);
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
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/mngrAdd"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String mngrAdd(HttpServletRequest _req)
    {
        Mngr mngr = JSONProc.parseMngrAddReq(_req);
        System.out.println(mngr.getMngrID()+"\t"+mngr.getPassword());
        Map<String,Object> mp = new ConcurrentHashMap<>();
        if(!m_adminService.findMngrByMngrID(mngr.getMngrID()).isEmpty())
        {
            mp.put("stat","invalid1");//id已被注册。
        }
        else if(m_adminService.creatNewAccnt(mngr.getMngrID(),mngr.getPassword()))
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

    /**
     * 映射到mngrPullPre的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/mngrPullPre"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String mngrPullPre(HttpServletRequest _req)
    {
        Map<String,Object> mp = new ConcurrentHashMap<>();
        List<Mngr> lstMngr = m_adminService.getMngrAllForAdmin();

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

    /**
     * 映射到adminUpdate的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/adminUpdate"}, method = RequestMethod.POST/*,
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

        if(m_adminService.hasMatchMngr(strMngrID,passwd))
        {
            mp.put("stat","success");
            Mngr mngr = m_adminService.findMngrByMngrID(strMngrID);
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

    /**
     * 映射到adminDataModeling的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/adminDataModeling"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String adminDataModeling(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        JSONObject objRet = new JSONObject();
        System.out.println(objReq.toJSONString());
        String strMngrID = (String) (objReq.getJSONArray("mngrid").get(0));
        Mngr mngr = m_adminService.findMngrByMngrID(strMngrID);
        int nCmd = Integer.parseInt((String) (objReq.getJSONArray("cmd").get(0)));
        if(mngr == null || mngr.isEmpty())
        {
            objRet.put("stat","invalid");
        }
        else
        {
            m_adminService.dataModeling(nCmd);
            objRet.put("stat","success");
        }
        System.out.println("ret:"+objRet.toJSONString());
        return objRet.toJSONString();
    }

    /**
     * 映射到adminDataExtract的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/adminDataExtract"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String adminDataExtract(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        JSONObject objRet = new JSONObject();
        System.out.println(objReq.toJSONString());
        String strMngrID = (String) (objReq.getJSONArray("mngrid").get(0));
        Mngr mngr = m_adminService.findMngrByMngrID(strMngrID);
        if(mngr == null || mngr.isEmpty())
        {
            objRet.put("stat","invalid");
        }
        else
        {
            m_adminService.dataExtract();
            objRet.put("stat","success");
        }
        System.out.println("ret:"+objRet.toJSONString());
        return objRet.toJSONString();
    }

    /**
     * 映射到adminDataTransfer的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/adminDataTransfer"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String adminDataTransfer(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        JSONObject objRet = new JSONObject();
        System.out.println(objReq.toJSONString());
        String strMngrID = (String) (objReq.getJSONArray("mngrid").get(0));
        Mngr mngr = m_adminService.findMngrByMngrID(strMngrID);
        if(mngr == null || mngr.isEmpty())
        {
            objRet.put("stat","invalid");
        }
        else
        {
            m_adminService.dataTransfer();
            objRet.put("stat","success");
        }
        System.out.println("ret:"+objRet.toJSONString());
        return objRet.toJSONString();
    }

    /**
     * 映射到adminMonitorBaseInfo的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/adminMonitorBaseInfo"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String adminMonitorBaseInfo(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        JSONObject objRet = new JSONObject();
        System.out.println(objReq.toJSONString());
        String strMngrID = (String) (objReq.getJSONArray("mngrid").get(0));
        Mngr mngr = m_adminService.findMngrByMngrID(strMngrID);
        if(mngr == null || mngr.isEmpty())
        {
            objRet.put("stat","invalid");
        }
        else
        {
            List<String> info = new CopyOnWriteArrayList<>();
            if(m_adminService.monitorBaseInfo(info))
            {
                StringBuffer sb = new StringBuffer();
                info.stream().map(str->"ww$"+str+"$ww").forEachOrdered(sb::append);
                System.out.println("base:\t"+sb.toString());

                objRet.put("stat","success");
                objRet.put("bt_time",info.get(0));
                objRet.put("lgc_num",info.get(1));
                objRet.put("phc_num",info.get(2));
                objRet.put("mem_sum",info.get(3));
                objRet.put("disk_sum",info.get(4));
            }
            else
            {
                objRet.put("stat","invalid");
            }
        }
        System.out.println("ret:"+objRet.toJSONString());
        return objRet.toJSONString();
    }

    /**
     * 映射到adminMonitorMach的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/adminMonitorMach"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String adminMonitorMach(HttpServletRequest _req)
    {
//        System.out.println("enter Mach");
        JSONObject objReq = JSONProc.parseReq(_req);
        JSONObject objRet = new JSONObject();
        System.out.println(objReq.toJSONString());
        String strMngrID = (String) (objReq.getJSONArray("mngrid").get(0));
        Mngr mngr = m_adminService.findMngrByMngrID(strMngrID);
        if(mngr == null || mngr.isEmpty())
        {
            objRet.put("stat","invalid");
        }
        else
        {
            List<String> info = new CopyOnWriteArrayList<>();
            if(m_adminService.monitorMach(info))
            {
                StringBuffer sb = new StringBuffer();
                info.stream().map(str->"ww$"+str+"$ww").forEachOrdered(sb::append);
                System.out.println("mach:\t"+sb.toString());


                objRet.put("stat","success");
                objRet.put("cpu_use",info.get(0));
                objRet.put("mem_use",info.get(1));
                objRet.put("disk_use",info.get(2));
            }
            else
            {
                objRet.put("stat","invalid");
            }
        }
        System.out.println("ret:"+objRet.toJSONString());
        return objRet.toJSONString();
    }

    /**
     * 映射到adminMonitorFigy的Controller方法
     * @return 请求响应String
     * @author 王浩宇
     * @date 9.1
     */
    @ResponseBody
    @RequestMapping(
            value = {"/adminMonitorFig"}, method = RequestMethod.POST/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String adminMonitorFig(HttpServletRequest _req)
    {
        JSONObject objReq = JSONProc.parseReq(_req);
        JSONObject objRet = new JSONObject();
        System.out.println(objReq.toJSONString());
        String strMngrID = (String) (objReq.getJSONArray("mngrid").get(0));
        Mngr mngr = m_adminService.findMngrByMngrID(strMngrID);
        if(mngr == null || mngr.isEmpty())
        {
            objRet.put("stat","invalid");
        }
        else
        {
            List<String> info = new CopyOnWriteArrayList<>();
            if(m_adminService.monitorFig(info))
            {
                StringBuffer sb = new StringBuffer();
                info.stream().map(str->"ww$"+str+"$ww").forEachOrdered(sb::append);
                System.out.println("ctrl:\t"+sb.toString());

                objRet.put("stat","success");
                objRet.put("disk_time",info.get(0));
                objRet.put("disk_read",info.get(1));
                objRet.put("disk_write",info.get(2));
                objRet.put("net_time",info.get(3));
                objRet.put("net_send",info.get(4));
            }
            else
            {
                objRet.put("stat","invalid");
            }
        }
        System.out.println("ret:"+objRet.toJSONString());
        return objRet.toJSONString();
    }


}
