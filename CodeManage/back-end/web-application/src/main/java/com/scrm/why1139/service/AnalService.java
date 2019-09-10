package com.scrm.why1139.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.dao.AnalDao;
import com.scrm.why1139.dao.BuyDao;
import com.scrm.why1139.dao.GoodsDao;
import com.scrm.why1139.dao.MngrDao;
import com.scrm.why1139.domain.Buy;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.Mngr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 面向AnalDao的Service类。
 * @author 王浩宇
 * @date 9.5
 */
@Service
public class AnalService {
    @Autowired
    private AnalDao m_analDao;
    @Autowired
    private BuyDao m_buyDao;
    @Autowired
    private GoodsDao m_goodsDao;
    @Autowired
    private MngrDao m_mngrDao;

    /**
     * 向用户提供购物记录的分析图
     * @param _strUserID in 用户ID
     * @param _nCmd 查询指令
     * @param _nLimit in 长度限制
     * @param _x in x列表
     * @param _y in y列表
     * @param _args in 额外参数
     * @return 逻辑值
     */
    public boolean getAnalyzeFigForUser(String _strUserID, int _nCmd, int _nLimit, List<String> _x, List<String> _y, List<Object> _args)
    {
        return m_analDao.buyListAnalyzeFigForUser(_strUserID,_nCmd,_nLimit,_x,_y,_args);
    }

    /**
     * 向用户提供购物记录的分析表
     * @param _strUserID in 用户ID
     * @param _nCmd in 查询指令
     * @param _nOffset in 偏移量
     * @param _nLimit in 长度限制
     * @param _vals in val列表
     * @param _args in 额外传递参数
     * @return 逻辑值
     */
    public boolean getAnalyzeValForUser(String _strUserID,int _nCmd,int _nOffset, int _nLimit, List<Object> _vals,List<Object> _args)
    {
        List<String> valsStr = new CopyOnWriteArrayList<>();
        boolean ret = m_analDao.buyListAnalyzeValForUser(_strUserID,_nCmd,_nOffset,_nLimit,valsStr,_args);
        if(!ret)
            return ret;
        if(_nCmd == 5)
        {
            for(int i = 0; i< valsStr.size();++i)
            {
                if(i <= 3)
                    _vals.add(valsStr.get(i));
                else
                {
                    int buyID = Integer.parseInt(valsStr.get(i));
                    Buy buy = m_buyDao.findBuyByBuyID(buyID);
                    JSONObject jsonbuy = new JSONObject();
                    if(buy == null || buy.isEmpty())
                    {
                        jsonbuy.put("buyid","-1");
                        jsonbuy.put("userid","undefined");
                        jsonbuy.put("mngrid","undefined");
                        jsonbuy.put("goodsid","undefined");
                        jsonbuy.put("buycnt","-1");
                        jsonbuy.put("buydate","undefined");
                        jsonbuy.put("gds","undefined");
                    }
                    else
                    {
                        jsonbuy.put("buyid",buy.getBuyID());
                        jsonbuy.put("userid",buy.getUserID());
                        jsonbuy.put("mngrid",buy.getMngrID());
                        jsonbuy.put("goodsid",buy.getGoodsID());
                        jsonbuy.put("buycnt",buy.getBuyCnt());
                        jsonbuy.put("buydate",buy.getBuyDate());
                        int gdsID = buy.getGoodsID();
                        Goods gds = m_goodsDao.findGoodsByGoodsID(gdsID);
                        JSONObject objGds = new JSONObject();
                        if(gds == null || gds.isEmpty())
                        {
                            objGds.put("name","undefined");
                            objGds.put("prc","0.0");
                            objGds.put("cnt","-1");
                            objGds.put("pic","undefined");
                            objGds.put("desc","undefined");
                            objGds.put("id","-1");
                            objGds.put("type","undefined");
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
                        jsonbuy.put("gds",objGds);
                    }
                    _vals.add(jsonbuy);
                }
            }
            JSONArray arr = new JSONArray();
            List<String> lstStrArgs = (List<String>) (_args.get(_args.size() - 1));
            for(int i = 0;i<lstStrArgs.size();++i)
            {
                arr.add(lstStrArgs.get(i));
            }
            _args.remove(_args.size()-1);
            _args.add(arr);
        }
        else if(_nCmd == 6)
        {
            for(int i = 0; i< valsStr.size();++i)
            {
                if(i <= 0)
                    _vals.add(valsStr.get(i));
                else
                {
                    int buyID = Integer.parseInt(valsStr.get(i));
                    Buy buy = m_buyDao.findBuyByBuyID(buyID);
                    JSONObject jsonbuy = new JSONObject();
                    if(buy == null || buy.isEmpty())
                    {
                        jsonbuy.put("buyid","-1");
                        jsonbuy.put("userid","undefined");
                        jsonbuy.put("mngrid","undefined");
                        jsonbuy.put("goodsid","undefined");
                        jsonbuy.put("buycnt","-1");
                        jsonbuy.put("buydate","undefined");
                        jsonbuy.put("gds","undefined");
                    }
                    else
                    {
                        jsonbuy.put("buyid",buy.getBuyID());
                        jsonbuy.put("userid",buy.getUserID());
                        jsonbuy.put("mngrid",buy.getMngrID());
                        jsonbuy.put("goodsid",buy.getGoodsID());
                        jsonbuy.put("buycnt",buy.getBuyCnt());
                        jsonbuy.put("buydate",buy.getBuyDate());
                        int gdsID = buy.getGoodsID();
                        Goods gds = m_goodsDao.findGoodsByGoodsID(gdsID);
                        JSONObject objGds = new JSONObject();
                        if(gds == null || gds.isEmpty())
                        {
                            objGds.put("name","undefined");
                            objGds.put("prc","0.0");
                            objGds.put("cnt","-1");
                            objGds.put("pic","undefined");
                            objGds.put("desc","undefined");
                            objGds.put("id","-1");
                            objGds.put("type","undefined");
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
                        jsonbuy.put("gds",objGds);
                    }
                    _vals.add(jsonbuy);
                }
            }
            JSONArray arr = new JSONArray();
            List<String> lstStrArgs = (List<String>) (_args.get(_args.size() - 1));
            for(int i = 0;i<lstStrArgs.size();++i)
            {
                arr.add(lstStrArgs.get(i));
            }
            _args.remove(_args.size()-1);
            _args.add(arr);
        }
        else if(_nCmd == 7)
        {
            for(int i = 0; i< valsStr.size();++i)
            {
                if(i <= 0)
                    _vals.add(valsStr.get(i));
                else
                {
                    int buyID = Integer.parseInt(valsStr.get(i));
                    Buy buy = m_buyDao.findBuyByBuyID(buyID);
                    JSONObject jsonbuy = new JSONObject();
                    if(buy == null || buy.isEmpty())
                    {
                        jsonbuy.put("buyid","-1");
                        jsonbuy.put("userid","undefined");
                        jsonbuy.put("mngrid","undefined");
                        jsonbuy.put("goodsid","undefined");
                        jsonbuy.put("buycnt","-1");
                        jsonbuy.put("buydate","undefined");
                        jsonbuy.put("gds","undefined");
                    }
                    else
                    {
                        jsonbuy.put("buyid",buy.getBuyID());
                        jsonbuy.put("userid",buy.getUserID());
                        jsonbuy.put("mngrid",buy.getMngrID());
                        jsonbuy.put("goodsid",buy.getGoodsID());
                        jsonbuy.put("buycnt",buy.getBuyCnt());
                        jsonbuy.put("buydate",buy.getBuyDate());
                        int gdsID = buy.getGoodsID();
                        Goods gds = m_goodsDao.findGoodsByGoodsID(gdsID);
                        JSONObject objGds = new JSONObject();
                        if(gds == null || gds.isEmpty())
                        {
                            objGds.put("name","undefined");
                            objGds.put("prc","0.0");
                            objGds.put("cnt","-1");
                            objGds.put("pic","undefined");
                            objGds.put("desc","undefined");
                            objGds.put("id","-1");
                            objGds.put("type","undefined");
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
                        jsonbuy.put("gds",objGds);
                    }
                    _vals.add(jsonbuy);
                }
            }
            JSONArray arr = new JSONArray();
            List<String> lstStrArgs = (List<String>) (_args.get(_args.size() - 1));
            for(int i = 0;i<lstStrArgs.size();++i)
            {
                arr.add(lstStrArgs.get(i));
            }
            _args.remove(_args.size()-1);
            _args.add(arr);
        }
        return ret;//true;
    }

    /**
     * 向管理员提供用户购物记录的分析图
     * @param _nCmd in 查询指令
     * @param _nLimit in 长度限制
     * @param _x in x坐标列表
     * @param _y in y坐标列表
     * @param _args in/out 额外传递参数
     * @return 逻辑值
     */
    public boolean getBuyListAnalyzeFigForAdmin(int _nCmd, int _nLimit, List<String> _x, List<String> _y, List<Object> _args)
    {
        return m_analDao.buyListAnalyzeFigForAdmin(_nCmd,_nLimit,_x,_y,_args);
    }

    /**
     * 向管理员提供用户购物记录的分析表
     * @param _nCmd in 查询指令
     * @param _nOffset in 偏移量
     * @param _nLimit in 长度限制
     * @param _vals in val列表
     * @param _args in 额外传递参数
     * @return 逻辑值
     */
    public boolean getBuyListAnalyzeValForAdmin(int _nCmd,int _nOffset, int _nLimit, List<Object> _vals,List<Object> _args)
    {
        List<String> valsStr = new CopyOnWriteArrayList<>();
        boolean ret = m_analDao.buyListAnalyzeValForAdmin(_nCmd,_nOffset,_nLimit,valsStr,_args);
        if(!ret)
            return ret;
        if(_nCmd == 13)
        {
            for(int i = 0; i< valsStr.size();++i)
            {
                if(i <= 4)
                    _vals.add(valsStr.get(i));
                else
                {
                    int buyID = Integer.parseInt(valsStr.get(i));
                    Buy buy = m_buyDao.findBuyByBuyID(buyID);
                    JSONObject jsonbuy = new JSONObject();
                    if(buy == null || buy.isEmpty())
                    {
                        jsonbuy.put("buyid","-1");
                        jsonbuy.put("userid","undefined");
                        jsonbuy.put("mngrid","undefined");
                        jsonbuy.put("goodsid","undefined");
                        jsonbuy.put("buycnt","-1");
                        jsonbuy.put("buydate","undefined");
                        jsonbuy.put("gds","undefined");
                    }
                    else
                    {
                        jsonbuy.put("buyid",buy.getBuyID());
                        jsonbuy.put("userid",buy.getUserID());
                        jsonbuy.put("mngrid",buy.getMngrID());
                        jsonbuy.put("goodsid",buy.getGoodsID());
                        jsonbuy.put("buycnt",buy.getBuyCnt());
                        jsonbuy.put("buydate",buy.getBuyDate());
                        int gdsID = buy.getGoodsID();
                        Goods gds = m_goodsDao.findGoodsByGoodsID(gdsID);
                        JSONObject objGds = new JSONObject();
                        if(gds == null || gds.isEmpty())
                        {
                            objGds.put("name","undefined");
                            objGds.put("prc","0.0");
                            objGds.put("cnt","-1");
                            objGds.put("pic","undefined");
                            objGds.put("desc","undefined");
                            objGds.put("id","-1");
                            objGds.put("type","undefined");
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
                        jsonbuy.put("gds",objGds);
                    }
                    _vals.add(jsonbuy);
                }
            }
            JSONArray arr = new JSONArray();
            List<String> lstStrArgs = (List<String>) (_args.get(_args.size() - 1));
            for(int i = 0;i<lstStrArgs.size();++i)
            {
                arr.add(lstStrArgs.get(i));
            }
            _args.remove(_args.size()-1);
            _args.add(arr);
        }
        else if(_nCmd == 14)
        {
            for(int i = 0; i< valsStr.size();++i)
            {
                if(i <= 0)
                    _vals.add(valsStr.get(i));
                else
                {
                    int buyID = Integer.parseInt(valsStr.get(i));
                    Buy buy = m_buyDao.findBuyByBuyID(buyID);
                    JSONObject jsonbuy = new JSONObject();
                    if(buy == null || buy.isEmpty())
                    {
                        jsonbuy.put("buyid","-1");
                        jsonbuy.put("userid","undefined");
                        jsonbuy.put("mngrid","undefined");
                        jsonbuy.put("goodsid","undefined");
                        jsonbuy.put("buycnt","-1");
                        jsonbuy.put("buydate","undefined");
                        jsonbuy.put("gds","undefined");
                    }
                    else
                    {
                        jsonbuy.put("buyid",buy.getBuyID());
                        jsonbuy.put("userid",buy.getUserID());
                        jsonbuy.put("mngrid",buy.getMngrID());
                        jsonbuy.put("goodsid",buy.getGoodsID());
                        jsonbuy.put("buycnt",buy.getBuyCnt());
                        jsonbuy.put("buydate",buy.getBuyDate());
                        int gdsID = buy.getGoodsID();
                        Goods gds = m_goodsDao.findGoodsByGoodsID(gdsID);
                        JSONObject objGds = new JSONObject();
                        if(gds == null || gds.isEmpty())
                        {
                            objGds.put("name","undefined");
                            objGds.put("prc","0.0");
                            objGds.put("cnt","-1");
                            objGds.put("pic","undefined");
                            objGds.put("desc","undefined");
                            objGds.put("id","-1");
                            objGds.put("type","undefined");
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
                        jsonbuy.put("gds",objGds);
                    }
                    _vals.add(jsonbuy);
                }
            }
            JSONArray arr = new JSONArray();
            List<String> lstStrArgs = (List<String>) (_args.get(_args.size() - 1));
            for(int i = 0;i<lstStrArgs.size();++i)
            {
                arr.add(lstStrArgs.get(i));
            }
            _args.remove(_args.size()-1);
            _args.add(arr);
        }
        else if(_nCmd == 15)
        {
            for(int i = 0; i< valsStr.size();++i)
            {
                if(i <= 0)
                    _vals.add(valsStr.get(i));
                else
                {
                    int buyID = Integer.parseInt(valsStr.get(i));
                    Buy buy = m_buyDao.findBuyByBuyID(buyID);
                    JSONObject jsonbuy = new JSONObject();
                    if(buy == null || buy.isEmpty())
                    {
                        jsonbuy.put("buyid","-1");
                        jsonbuy.put("userid","undefined");
                        jsonbuy.put("mngrid","undefined");
                        jsonbuy.put("goodsid","undefined");
                        jsonbuy.put("buycnt","-1");
                        jsonbuy.put("buydate","undefined");
                        jsonbuy.put("gds","undefined");
                    }
                    else
                    {
                        jsonbuy.put("buyid",buy.getBuyID());
                        jsonbuy.put("userid",buy.getUserID());
                        jsonbuy.put("mngrid",buy.getMngrID());
                        jsonbuy.put("goodsid",buy.getGoodsID());
                        jsonbuy.put("buycnt",buy.getBuyCnt());
                        jsonbuy.put("buydate",buy.getBuyDate());
                        int gdsID = buy.getGoodsID();
                        Goods gds = m_goodsDao.findGoodsByGoodsID(gdsID);
                        JSONObject objGds = new JSONObject();
                        if(gds == null || gds.isEmpty())
                        {
                            objGds.put("name","undefined");
                            objGds.put("prc","0.0");
                            objGds.put("cnt","-1");
                            objGds.put("pic","undefined");
                            objGds.put("desc","undefined");
                            objGds.put("id","-1");
                            objGds.put("type","undefined");
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
                        jsonbuy.put("gds",objGds);
                    }
                    _vals.add(jsonbuy);
                }
            }
            JSONArray arr = new JSONArray();
            List<String> lstStrArgs = (List<String>) (_args.get(_args.size() - 1));
            for(int i = 0;i<lstStrArgs.size();++i)
            {
                arr.add(lstStrArgs.get(i));
            }
            _args.remove(_args.size()-1);
            _args.add(arr);
        }
        return ret;//true;
    }

    /**
     * 提供商品分析
     * @param _gds1ID in 商品1的ID
     * @param _gds2ID in 商品2的ID
     * @param _relNum in 1、2商品之间的关联归一化系数
     * @param _gdsID in 商品ID列表
     * @param _buyCnt in 购买数量的列表
     * @param _cntNum in 购买数量的归一化系数
     * @return 逻辑值
     */
    public boolean getGoodsAnalyzeForAdmin(List<Goods> _gds1,List<Goods> _gds2,List<Double> _relNum,List<Goods> _gds,List<Integer> _buyCnt,List<Double> _cntNum)
    {
        List<Integer> gds1ID = new CopyOnWriteArrayList<>();
        List<Integer> gds2ID = new CopyOnWriteArrayList<>();
        List<Double> relNum = new CopyOnWriteArrayList<>();
        List<Integer> gdsID = new CopyOnWriteArrayList<>();
        List<Integer> buyCnt = new CopyOnWriteArrayList<>();
        List<Double> cntNum = new CopyOnWriteArrayList<>();
        m_analDao.goodsAnalyze(gds1ID,gds2ID,relNum,gdsID,buyCnt,cntNum);
        gds1ID.stream().map(gdsid->m_goodsDao.findGoodsByGoodsID(gdsid)).forEach(_gds1::add);
        gds2ID.stream().map(gdsid->m_goodsDao.findGoodsByGoodsID(gdsid)).forEach(_gds2::add);
        relNum.stream().forEach(_relNum::add);
        gdsID.stream().map(gdsid->m_goodsDao.findGoodsByGoodsID(gdsid)).forEach(_gds::add);
        buyCnt.stream().forEach(_buyCnt::add);
        cntNum.stream().forEach(_cntNum::add);
        System.out.println("size"+buyCnt.size());
        return true;
    }

    /**
     * 收银员业绩统计
     * @param _mngr in 收银员ID
     * @param _sale out 收银业绩列表
     * @return 逻辑值
     */
    public boolean getMngrAnalyzeFigForAdmin(List<Mngr> _mngr, List<String> _sale)
    {
        List<String> mngrID = new CopyOnWriteArrayList<>();
        List<String> sale = new CopyOnWriteArrayList<>();
        boolean ret = m_analDao.mngrAnalyzeFig(mngrID, sale);
        mngrID.stream().map(mngrid->m_mngrDao.findMngrByMngrID(mngrid)).forEach(_mngr::add);
        sale.stream().forEach(_sale::add);
        return ret;
    }

}
