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

    public boolean getAnalyzeFigForUser(String _strUserID, int _nCmd, int _nLimit, List<String> _x, List<String> _y, List<Object> _args)
    {
        return m_analDao.buyListAnalyzeFigForUser(_strUserID,_nCmd,_nLimit,_x,_y,_args);
    }

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

    public boolean getBuyListAnalyzeFigForAdmin(int _nCmd, int _nLimit, List<String> _x, List<String> _y, List<Object> _args)
    {
        return m_analDao.buyListAnalyzeFigForAdmin(_nCmd,_nLimit,_x,_y,_args);
    }

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
