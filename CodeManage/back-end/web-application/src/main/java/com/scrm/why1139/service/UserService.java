package com.scrm.why1139.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.dao.AnalDao;
import com.scrm.why1139.dao.BuyDao;
import com.scrm.why1139.dao.GoodsDao;
import com.scrm.why1139.domain.Buy;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 面向User的Service类。
 * @author why
 */
@Service
public class UserService extends GeneralService
{
    private GoodsDao m_goodsDao;
    private BuyDao m_buyDao;

    @Autowired
    private AnalDao m_analDao;

    /**
     * setter注入
     * @param _goodsDao in
     * @author why
     */
    @Autowired
    public void setGoodsDao(GoodsDao _goodsDao)
    {
        this.m_goodsDao = _goodsDao;
//        this.m_goodsDao = Optional.ofNullable(_goodsDao).orElseThrow();
    }

    /**
     * setter注入
     * @param _buyDao in
     * @author why
     */
    @Autowired
    public void setBuyDao(BuyDao _buyDao)
    {
        this.m_buyDao = _buyDao;
//        this.m_buyDao = Optional.ofNullable(_buyDao).orElseThrow();
    }

    /**
     * 获取商品推荐
     * @param _user in 当前User对象
     * @return Goods的list对象
     * @author why
     */
    public List<Goods> getGoodsRcmdGeneral(User _user,int _nOffSet,int _nLimit)
    {
        List<Goods> ret = m_analDao.getRcmdByUser(_user,_nOffSet,_nLimit);
        return ret;
    }

    public List<Goods> getGoodsRelated(Goods _gds,int _nOffSet,int _nLimit)
    {
        List<Goods> ret = m_analDao.getRcmdByGds(_gds,_nOffSet,_nLimit);
        return ret;
    }

    /**
     * 根据指定Goods类型查询商品
     * @param _strType in Goods类型
     * @return Goods的list对象
     * @author why
     */
    public List<Goods> goodsQueryByType(User _user,String _strType,int _nOffset,int _nLimit)
    {
        //TODO:for XXS, need replacer strategy to do extra work here.
        List<Goods> ret = m_analDao.getRcmdByQuery(_user,_strType,_nOffset,_nLimit);
        return ret;
    }



    /**
     * 获取购物记录的list
     * @param _user in User对象
     * @return list对象
     * @author why
     */
    public List<Buy> getBuyLog(User _user)
    {
        List<Buy> ret = m_buyDao.findBuyByUserID(_user.getUserID(),ConfigConst.BUYLOG_LIMIT);
        return ret;
    }



    /**
     * 通过密码方式创建新用户，该操作将导致数据库中User记录的更新。
     * @param _strUserName in UserName
     * @param _strUserID in UserID
     * @param _strPassword in User密码
     * @return 创建通告结果的boolean值
     * @author why
     */
    public boolean creatNewUserByPasswd(String _strUserName, String _strUserID, String _strPassword)
    {
        if(!checkString(_strUserName) || !checkString(_strUserID) || !checkString(_strPassword))
            return false;
        User newUser = new User();
        newUser.setUserName(_strUserName);
        newUser.setUserID(_strUserID);
        newUser.setPassword(_strPassword);
        m_userDao.insertUser(newUser);
        return true;
    }

    /**
     * 通过人脸识别方式创建新用户，该操作将导致数据库中User记录的更新。
     * TODO:目前由于密码和人脸信息的地位不等价，人脸信息的存储被放置在该方法的外侧
     * @param _strUserID in 用户ID
     * @return 创建成功与否的boolean值
     * @author why
     */
    public boolean creatNewUserByRecgBio(String _strUserID)
    {
        if(!checkString(_strUserID))
            return false;
        User newUser = new User();
        newUser.setUserName("");
        newUser.setUserID(_strUserID);
        newUser.setPassword("");
        m_userDao.insertUser(newUser);
        return true;
    }

    /**
     * 存储用户的生物信息
     * @param _user in user对象
     * @param _btBioRef in 生物信息二进制流
     * @return 存储通告结果的boolean值
     * @author why
     */
    public boolean saveBioRef(User _user,byte[] _btBioRef)
    {
        boolean ret = false;
        _user.setBioRef(_user.getUserID().hashCode()+".bio");
        ret = _user.saveBioRef(_btBioRef);
        return ret;
    }

    public boolean getFig(String _strUserID,int _nCmd, int _nLimit, List<String> _x, List<String> _y, List<Object> _args)
    {
        return m_analDao.buyListAnalyzeFigForUser(_strUserID,_nCmd,_nLimit,_x,_y,_args);
    }

    public boolean getVal(String _strUserID,int _nCmd,int _nOffset, int _nLimit, List<Object> _vals,List<Object> _args)
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
                        Goods gds = m_goodsDao.findGoodsByID(gdsID);
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
                        Goods gds = m_goodsDao.findGoodsByID(gdsID);
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
                        Goods gds = m_goodsDao.findGoodsByID(gdsID);
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



//    public static void main(String s[])
//    {
//        UserService us = new UserService();
//        System.out.println(us.checkString("uname"));
//        System.out.println(us.checkString("100"));
//        System.out.println(us.checkString("1000"));
//
//    }
}
