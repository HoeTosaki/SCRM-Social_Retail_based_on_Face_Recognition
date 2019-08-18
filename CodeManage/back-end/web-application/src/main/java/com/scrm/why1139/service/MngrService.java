package com.scrm.why1139.service;

import com.scrm.why1139.dao.BuyDao;
import com.scrm.why1139.dao.GoodsDao;
import com.scrm.why1139.dao.MngrDao;
import com.scrm.why1139.dao.UserDao;
import com.scrm.why1139.domain.Buy;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.Mngr;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 面向Mngr的Service类。
 * @author why
 */
@Service
public class MngrService
{
    private MngrDao m_mngrDao;
    private GoodsDao m_goodsDao;
    private BuyDao m_buyDao;
    private UserDao m_userDao;

    /**
     * setter注入
     * @param _mngrDao in
     * @author why
     */
    @Autowired
    public void setMngrDao(MngrDao _mngrDao)
    {
//        this.m_mngrDao = Optional.ofNullable(_mngrDao).orElseThrow();
         this.m_mngrDao = _mngrDao;
    }

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
     * setter注入
     * @param _userDao in
     * @author why
     */
    @Autowired
    public void setUserDao(UserDao _userDao)
    {
        this.m_userDao = _userDao;
    }

    /**
     * 通过Mngr的个人信息，获取在数据库中的匹配结果
     * @param _strMngrID in MngrID
     * @param _strPassword in Mngr密码
     * @return 标志匹配结果的boolean值
     * @author why
     */
    public boolean hasMatchMngr(String _strMngrID,String _strPassword)
    {
        int nMatchCnt = m_mngrDao.getMatchCount(_strMngrID,_strPassword);
        return nMatchCnt > 0;
    }

    /**
     * 通过MngrID获取Mngr对象
     * @param _strMngrID in 表示MngrID的字符串
     * @return Mngr对象
     * @author why
     */
    public Mngr findMngrByMngrID(String _strMngrID)
    {
        return m_mngrDao.findMngrByMngrID(_strMngrID);
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
        return m_userDao.findUserByUserID(_strUserID);
    }

    /**
     * 通过GoodsID获取Goods对象
     * @param _nGoodsID in GoodsID
     * @return Goods对象
     * @author why
     */
    public Goods findGoodsByGoodsID(int _nGoodsID)
    {
        return m_goodsDao.findGoodsByID(_nGoodsID);
    }

    /**
     * 根据生物特征信息获取User对象
     * @param _btBioRef in BioRef
     * @return User对象
     * @author why
     */
    public User findUserByBioRef(String _btBioRef)
    {
        //TODO: need impl in future based on Face-Reg modules or some newest tech, ehh, you know.
        return m_userDao.findUserByUserID("why");
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
     * 添加购物记录
     * @param _strMngrID in MngrID
     * @param _strUserID in UserID
     * @param _nGoodsID in GoodsID
     * @param _nCnt in Goods购买数量
     * @author why
     */
    public void addBuyLog(String _strMngrID,String _strUserID,int _nGoodsID,int _nCnt)
    {
        Buy newBuy = new Buy();
        newBuy.setUserID(_strUserID);
        newBuy.setGoodsID(_nGoodsID);
        newBuy.setBuyCnt(_nCnt);
        newBuy.setMngrID(_strMngrID);
        newBuy.setBuyDate(new Date().toString());
        m_buyDao.updateBuy(newBuy);
    }

    /**
     * 获取商品推荐
     * @param _user in 当前User对象
     * @return Goods的list对象
     * @author why
     */
    public List<Goods> getGoodsRcmd(User _user)
    {
        List<Goods> ret = m_goodsDao.getGoodsByRcmd(_user);
        return ret;
    }

    /**
     * 根据指定Goods类型查询商品
     * @param _strType in Goods类型
     * @return Goods的list对象
     * @author why
     */
    public List<Goods> goodsQuery(String _strType)
    {
        //TODO:for XXS, need replacer strategy to do extra work here.
        List<Goods> ret = m_goodsDao.getGoodsByClass(_strType,ConfigConst.GOODS_LIMIT);
        return ret;
    }

}