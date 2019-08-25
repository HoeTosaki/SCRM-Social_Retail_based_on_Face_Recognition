package com.scrm.why1139.service;

import com.scrm.why1139.dao.BuyDao;
import com.scrm.why1139.dao.GoodsDao;
import com.scrm.why1139.domain.Buy;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 面向User的Service类。
 * @author why
 */
@Service
public class UserService extends GeneralService
{
    private GoodsDao m_goodsDao;
    private BuyDao m_buyDao;

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
    public List<Goods> getGoodsRcmd(User _user)
    {
        List<Goods> ret = m_goodsDao.getGoodsByRcmd(_user);
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

//    public static void main(String s[])
//    {
//        UserService us = new UserService();
//        System.out.println(us.checkString("uname"));
//        System.out.println(us.checkString("100"));
//        System.out.println(us.checkString("1000"));
//
//    }
}
