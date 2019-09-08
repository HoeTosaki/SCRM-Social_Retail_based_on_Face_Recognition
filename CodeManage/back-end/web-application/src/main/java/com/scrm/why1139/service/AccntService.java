package com.scrm.why1139.service;

import com.scrm.why1139.BioReferenceModule.BioCertificater;
import com.scrm.why1139.dao.AnalDao;
import com.scrm.why1139.dao.BuyDao;
import com.scrm.why1139.dao.GoodsDao;
import com.scrm.why1139.dao.UserDao;
import com.scrm.why1139.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * 面向Mngr的Service类。
 * @author why
 */
@Service
public class AccntService
{
    @Autowired
    private GoodsService m_goodsService;
    @Autowired
    private ManService m_manService;
    @Autowired
    private BuyService m_buyService;

    /**
     * 通过Mngr的个人信息，获取在数据库中的匹配结果
     * @param _strMngrID in MngrID
     * @param _strPassword in Mngr密码
     * @return 标志匹配结果的boolean值
     * @author why
     */
    public boolean hasMatchMngr(String _strMngrID,String _strPassword)
    {
        return m_manService.hasMatchMngr(_strMngrID, _strPassword);
    }

    /**
     * 通过MngrID获取Mngr对象
     * @param _strMngrID in 表示MngrID的字符串
     * @return Mngr对象
     * @author why
     */
    public Mngr findMngrByMngrID(String _strMngrID)
    {
        return m_manService.findMngrByMngrID(_strMngrID);
    }

    /**
     * 通过GoodsID获取Goods对象
     * @param _nGoodsID in GoodsID
     * @return Goods对象
     * @author why
     */
    public Goods findGoodsByGoodsID(int _nGoodsID)
    {
        return m_goodsService.findGoodsByGoodsID(_nGoodsID);
    }

    public Goods findGoodsByGoodsName(String _strGoodsName)
    {
        return m_goodsService.findGoodsByGoodsName(_strGoodsName);
    }

    public List<Goods> findGoodsByGoodsType(String _strGoodsType)
    {
        return m_goodsService.findGoodsByGoodsType(_strGoodsType);
    }

    /**
     * 通过User的个人信息，获取在数据库中的匹配结果
     * @param _strUserID in UserID
     * @param _strPassword in User密码
     * @return 标志匹配结果的boolean值
     * @author why
     */
    public boolean hasMatchUser(String _strUserID,String _strPassword)
    {
        return m_manService.hasMatchUser(_strUserID, _strPassword);
    }

    /**
     * 通过UserID获取User对象
     * used for some bad circumstances where customers failed to provide bio info.
     * @param _strUserID in 表示UserID的字符串 userid,primary key in table t_user, which can deduce a unique user.
     * @return User对象
     * @author why
     */
    public User findUserByUserID(String _strUserID)
    {
        return m_manService.findUserByUserID(_strUserID);
    }

    /**
     * 根据生物特征信息获取User对象
     * @param _btBioRef in BioRef
     * @return User对象
     * @author why
     */
    public List<User> findUserByRecgBio(String _btBioRef)
    {
        return m_manService.findUserByRecgBio(_btBioRef);
    }

    /**
     * 获取购物记录的list
     * @param _user in User对象
     * @return list对象
     * @author why
     */
    public List<Buy> getBuyLogByUser(User _user)
    {
        return m_buyService.getBuyLogByUser(_user);
    }


    /**
     * 添加购物记录
     * @param _strMngrID in MngrID
     * @param _strUserID in UserID
     * @param _nGoodsID in GoodsID
     * @param _nCnt in Goods购买数量
     * @author why
     */
    public void addBuyLog(String _strMngrID,String _strUserID,int _nGoodsID,int _nCnt)
    {
        m_buyService.addBuyLog(_strMngrID, _strUserID, _nGoodsID, _nCnt);
    }

    /**
     * 根据指定Goods类型查询商品
     * @param _strType in Goods类型
     * @return Goods的list对象
     * @author why
     */
    public List<Goods> goodsQuery(String _strType)
    {
        return m_goodsService.findGoodsByGoodsType(_strType);
    }



    /**
     * 创建新的Accnt对象
     * @param _strAccntID in AccntID
     * @param _strPassword in 密码
     * @return 创建是否成功的boolean值
     * @author why
     */
    public boolean creatNewAccnt(String _strAccntID,String _strPassword)
    {
        return m_manService.creatNewAccnt(_strAccntID,_strPassword);
    }

    /**
     * 获取商品推荐
     * @param _user in 当前User对象
     * @return Goods的list对象
     * @author why
     */
    public List<Goods> getGoodsRcmdForUser(User _user)
    {
        return m_goodsService.getGoodsRcmdGeneralForUser(_user,0,ConfigConst.ACCNT_RCMD_LIMIT);
    }

    public List<Order> findOrderByUserID(String _strUserID)
    {
        return m_buyService.findOrderByUserID(_strUserID);
    }

    public Order findOrderByOrderID(int  _nOrderID)
    {
        return m_buyService.findOrderByOrderID(_nOrderID);
    }

    public List<Order> findOrder()
    {
        return m_buyService.findOrder();
    }

    public void removeOrderByOrderID(int _nOrderID)
    {
        m_buyService.removeOrderByOrderID(_nOrderID);
    }
}