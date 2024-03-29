package com.scrm.why1139.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scrm.why1139.dao.AnalDao;
import com.scrm.why1139.dao.BuyDao;
import com.scrm.why1139.dao.GoodsDao;
import com.scrm.why1139.domain.Buy;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.Order;
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
public class UserService
{
    @Autowired
    private GoodsService m_goodsService;
    @Autowired
    private BuyService m_buyService;
    @Autowired
    private AnalService m_analService;
    @Autowired
    private ManService m_manService;

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

    public List<User> findUserByRecgBio(String _imgrecg)
    {
        return m_manService.findUserByRecgBio(_imgrecg);
    }

    public boolean isUserRecgBioExist(User _user)
    {
        return m_manService.isUserRecgBioExist(_user);
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

    public List<Goods> findGoodsByFuzzy(String _strFuzzy)
    {
        return m_goodsService.findGoodsByFuzzy(_strFuzzy);
    }

    /**
     * 获取商品推荐
     * @param _user in 当前User对象
     * @return Goods的list对象
     * @author why
     */
    public List<Goods> getGoodsRcmdGeneralForUser(User _user, int _nOffSet, int _nLimit)
    {
        return m_goodsService.getGoodsRcmdGeneralForUser(_user, _nOffSet, _nLimit);
    }

    public List<Goods> getGoodsRelatedForUser(Goods _gds,int _nOffSet,int _nLimit)
    {
        return m_goodsService.getGoodsRelatedForUser(_gds, _nOffSet, _nLimit);
    }

    /**
     * 根据指定Goods类型查询商品
     * @param _strType in Goods类型
     * @return Goods的list对象
     * @author why
     */
    public List<Goods> getGoodsRcmdByTypeForUser(User _user,String _strType,int _nOffset,int _nLimit)
    {
        return m_goodsService.getGoodsRcmdByTypeForUser(_user, _strType, _nOffset, _nLimit);
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
     * 获取一个可用的新的UserID
     * @param _nTryLimit in 在获取失败情况下的最大可能尝试次数。
     * @return UserID
     * @author why
     */
    public String createNewUserID(int _nTryLimit)
    {
        return m_manService.createNewUserID(_nTryLimit);
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
        return m_manService.creatNewUserByPasswd(_strUserName, _strUserID, _strPassword);
    }

    /**
     * 通过人脸识别方式创建新用户，该操作将导致数据库中User记录的更新。
     * TODO:目前由于密码和人脸信息的地位不等价，人脸信息的存储被放置在该方法的外侧
     * @param _strUserID in 用户ID
     * @return 创建成功与否的boolean值
     * @author why
     */
    public boolean creatNewUserByRecgBio(String _strUserID,List<String> _imgrecg)
    {
        return m_manService.creatNewUserByRecgBio(_strUserID,_imgrecg);
    }

    public boolean updateUserPasswd(User _user,String _strPasswd)
    {
        return m_manService.updateUserPasswd(_user, _strPasswd);
    }

    public boolean updateUserRecgBio(User _user,List<String> _imgrecg)
    {
        return m_manService.updateUserRecgBio(_user, _imgrecg);
    }

    public boolean updateUserID(User _user,String _strUserID)
    {
        return m_manService.updateUserID(_user, _strUserID);
    }

    public boolean getAnalyzeFigForUser(String _strUserID,int _nCmd, int _nLimit, List<String> _x, List<String> _y, List<Object> _args)
    {
        return m_analService.getAnalyzeFigForUser(_strUserID, _nCmd, _nLimit, _x, _y, _args);
    }

    public boolean getAnalyzeValForUser(String _strUserID,int _nCmd,int _nOffset, int _nLimit, List<Object> _vals,List<Object> _args)
    {
        return m_analService.getAnalyzeValForUser(_strUserID, _nCmd, _nOffset, _nLimit, _vals, _args);
    }

    public boolean addOrderLog(String _strUserID,int _nGoodsID,int _nCnt)
    {
        return m_buyService.addOrderLog(_strUserID, _nGoodsID, _nCnt);
    }

    public void removeOrderByOrderID(int _nOrderID)
    {
        m_buyService.removeOrderByOrderID(_nOrderID);
    }


    public List<Order> findOrderByUserID(String  _strUserID)
    {
        return m_buyService.findOrderByUserID(_strUserID);
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
