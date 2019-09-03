package com.scrm.why1139.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.Mngr;
import com.scrm.why1139.service.AccntService;
import com.scrm.why1139.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class GoodsOperWeb {
    @Autowired
    private AccntService m_accntService;
    @Autowired
    private AdminService m_adminService;

    /**
     * 映射到mngrUpdate的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/gdsUpdate"}/*,
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

        Goods gdsFind = m_accntService.findGoodsByGoodsID(gds.getGoodsID());
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
     * 映射到mngrDel的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/gdsDel"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String gdsDel(HttpServletRequest _req)
    {
        JSONObject jsonObj = JSONProc.parseReq(_req);
        int nGoodsID = Integer.parseInt((String) (jsonObj.getJSONArray("goodsid").get(0)));

        System.out.println(nGoodsID);

        Map<String,Object> mp = new ConcurrentHashMap<>();

        Goods gds = m_accntService.findGoodsByGoodsID(nGoodsID);

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
     * 映射到mngrAdd的Controller方法
     * @return 请求响应String
     * @author why
     */
    @ResponseBody
    @RequestMapping(
            value = {"/gdsAdd"}/*,
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
        if(!m_accntService.findGoodsByGoodsID(gds.getGoodsID()).isEmpty())
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

    @ResponseBody
    @RequestMapping(
            value = {"/gdsPullPre"}/*,
            produces = "application/json;charset=UTF-8"*/
    )
    public String gdsPullPre(HttpServletRequest _req)
    {
        JSONObject jsonObj = JSONProc.parseReq(_req);
        int nGoodsID = Integer.parseInt((String) (jsonObj.getJSONArray("goodsid").get(0)));


        Map<String,Object> mp = new ConcurrentHashMap<>();
        Goods gdsPre = m_accntService.findGoodsByGoodsID(nGoodsID);

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
            /* web resolve*/
            arrArgs = jsonObj.getJSONArray("args[]");
        }

        Map<String,Object> mp = new ConcurrentHashMap<>();

        switch (nCmd)
        {
            case 0:
            {
                String args0 = arrArgs.getString(0);//id
                Goods gds = m_accntService.findGoodsByGoodsID(Integer.parseInt(args0));
                if(gds == null ||gds.isEmpty())
                {
                    mp.put("stat", "invalid");
                }
                else
                {
                    JSONObject jsonGdsNew = new JSONObject();
                    jsonGdsNew.put("id",gds.getGoodsID());
                    jsonGdsNew.put("name",gds.getGoodsName());
                    jsonGdsNew.put("type",gds.getGoodsType());
                    jsonGdsNew.put("desc",gds.getGoodsDesc());
                    jsonGdsNew.put("cnt",gds.getGoodsCnt());
                    jsonGdsNew.put("prc",gds.getPrice());
                    jsonGdsNew.put("pic",gds.getPic());
                    mp.put("gds",jsonGdsNew);
                }
                break;
            }
            case 1:
            {
                String args0 = arrArgs.getString(0);//name
                Goods gds = m_accntService.findGoodsByGoodsName(args0);
                if(gds == null ||gds.isEmpty())
                {
                    mp.put("stat", "invalid");
                }
                else
                {
                    JSONObject jsonGdsNew = new JSONObject();
                    jsonGdsNew.put("id",gds.getGoodsID());
                    jsonGdsNew.put("name",gds.getGoodsName());
                    jsonGdsNew.put("type",gds.getGoodsType());
                    jsonGdsNew.put("desc",gds.getGoodsDesc());
                    jsonGdsNew.put("cnt",gds.getGoodsCnt());
                    jsonGdsNew.put("prc",gds.getPrice());
                    jsonGdsNew.put("pic",gds.getPic());
                    mp.put("gds",jsonGdsNew);
                }
                break;
            }
            case 2:
            {
                String args0 = arrArgs.getString(0);//type
                List<Goods> lstGds = m_accntService.findGoodsByGoodsType(args0);
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


}
