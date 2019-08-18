package com.scrm.why1139.service;

import com.scrm.why1139.dao.BuyDao;
import com.scrm.why1139.dao.GoodsDao;
import com.scrm.why1139.dao.UserDao;
import com.scrm.why1139.domain.Buy;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService
{
    private UserDao m_userDao;
    private GoodsDao m_goodsDao;
    private BuyDao m_buyDao;

    @Autowired
    public void setUserDao(UserDao _userDao)
    {
        this.m_userDao = _userDao;
//        this.m_userDao = Optional.ofNullable(_userDao).orElseThrow();
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

    public boolean hasMatchUser(String _strUserID,String _strPassword)
    {
        int nMatchCnt = m_userDao.getMatchCount(_strUserID,_strPassword);
        return nMatchCnt > 0;
    }

    public User findUserByUserID(String _strUserID)
    {
        return m_userDao.findUserByUserID(_strUserID);
    }


    public List<Goods> getGoodsRcmd(User _user)
    {
        List<Goods> ret = m_goodsDao.getGoodsByRcmd(_user);
        return ret;
    }

    public List<Buy> getBuyLog(User _user)
    {
        List<Buy> ret = m_buyDao.findBuyByUserID(_user.getUserID(),ConfigConst.BUYLOG_LIMIT);
        return ret;
    }

    public List<Goods> goodsQuery(String _strType)
    {
        //TODO:for XXS, need replacer strategy to do extra work here.
        List<Goods> ret = m_goodsDao.getGoodsByClass(_strType,ConfigConst.GOODS_LIMIT);
        return ret;
    }



    public boolean creatNewUser(String _strUserName, String _strUserID,String _strPassword)
    {
        if(!checkString(_strUserName) || !checkString(_strUserID) || checkString(_strPassword))
            return false;
        User newUser = new User();
        newUser.setUserName(_strUserName);
        newUser.setUserID(_strUserID);
        newUser.setPassword(_strPassword);
        m_userDao.insertUser(newUser);
        return true;
    }

    public boolean saveBioRef(User _user,byte[] _btBioRef)
    {
        boolean ret = false;
        _user.setBioRef(_user.getUserID().hashCode()+".bio");
        ret = _user.saveBioRef(_btBioRef);
        return ret;
    }

    private boolean checkString(String _strSrc)
    {
        return !(_strSrc == null || _strSrc.trim().equals(""));
    }
//    public

}
