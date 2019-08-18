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

@Service
public class MngrService
{
    private MngrDao m_mngrDao;
    private GoodsDao m_goodsDao;
    private BuyDao m_buyDao;
    private UserDao m_userDao;

    @Autowired
    public void setMngrDao(MngrDao _mngrDao)
    {
//        this.m_mngrDao = Optional.ofNullable(_mngrDao).orElseThrow();
         this.m_mngrDao = _mngrDao;
    }

    @Autowired
    public void setGoodsDao(GoodsDao _goodsDao)
    {
        this.m_goodsDao = _goodsDao;
//        this.m_goodsDao = Optional.ofNullable(_goodsDao).orElseThrow();
    }

    @Autowired
    public void setBuyDao(BuyDao _buyDao)
    {
        this.m_buyDao = _buyDao;
//        this.m_buyDao = Optional.ofNullable(_buyDao).orElseThrow();
    }

    @Autowired
    public void setUserDao(UserDao _userDao)
    {
        this.m_userDao = _userDao;
    }


    public boolean hasMatchMngr(String _strMngrID,String _strPassword)
    {
        int nMatchCnt = m_mngrDao.getMatchCount(_strMngrID,_strPassword);
        return nMatchCnt > 0;
    }

    public Mngr findMngrByMngrID(String _strMngrID)
    {
        return m_mngrDao.findMngrByMngrID(_strMngrID);
    }

    /**
     * used for some bad circumstances where customers failed to provide bio info.
     * @param _strUserID in-param userid,primary key in table t_user, which can deduce a unique user.
     * @return User Obj
     */
    public User findUserByUserID(String _strUserID)
    {
        return m_userDao.findUserByUserID(_strUserID);
    }

    public Goods findGoodsByGoodsID(int _nGoodsID)
    {
        return m_goodsDao.findGoodsByID(_nGoodsID);
    }



    public User findUserByBioRef(String _btBioRef)
    {
        //TODO: need impl in future based on Face-Reg modules or some newest tech, ehh, you know.
        return m_userDao.findUserByUserID("why");
    }

    public List<Buy> getBuyLog(User _user)
    {
        List<Buy> ret = m_buyDao.findBuyByUserID(_user.getUserID(),ConfigConst.BUYLOG_LIMIT);
        return ret;
    }

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

    public List<Goods> getGoodsRcmd(User _user)
    {
        List<Goods> ret = m_goodsDao.getGoodsByRcmd(_user);
        return ret;
    }

    public List<Goods> goodsQuery(String _strType)
    {
        //TODO:for XXS, need replacer strategy to do extra work here.
        List<Goods> ret = m_goodsDao.getGoodsByClass(_strType,ConfigConst.GOODS_LIMIT);
        return ret;
    }


}