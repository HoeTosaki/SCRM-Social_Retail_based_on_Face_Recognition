package com.scrm.why1139.service;

import com.scrm.why1139.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 面向Mngr的Service类。
 * @author 王浩宇
 * @date 9.2
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
     * @author 王浩宇
     * @date 9.2
     */
    public boolean hasMatchMngr(String _strMngrID,String _strPassword)
    {
        return m_manService.hasMatchMngr(_strMngrID, _strPassword);
    }

    /**
     * 通过MngrID获取Mngr对象
     * @param _strMngrID in 表示MngrID的字符串
     * @return Mngr对象
     * @author 王浩宇
     * @date 9.2
     */
    public Mngr findMngrByMngrID(String _strMngrID)
    {
        return m_manService.findMngrByMngrID(_strMngrID);
    }

    /**
     * 通过GoodsID获取Goods对象
     * @param _nGoodsID in GoodsID
     * @return Goods对象
     * @author 王浩宇
     * @date 9.2
     */
    public Goods findGoodsByGoodsID(int _nGoodsID)
    {
        return m_goodsService.findGoodsByGoodsID(_nGoodsID);
    }

    /**
     * 通过商品名查询商品
     * @param _strGoodsName in 商品名
     * @return Goods对象
     */
    public Goods findGoodsByGoodsName(String _strGoodsName)
    {
        return m_goodsService.findGoodsByGoodsName(_strGoodsName);
    }

    /**
     * 通过商品类型查询商品
     * @param _strGoodsType in 商品类型
     * @return Goods列表
     */
    public List<Goods> findGoodsByGoodsType(String _strGoodsType)
    {
        return m_goodsService.findGoodsByGoodsType(_strGoodsType);
    }

    /**
     * 通过User的个人信息，获取在数据库中的匹配结果
     * @param _strUserID in UserID
     * @param _strPassword in User密码
     * @return 标志匹配结果的boolean值
     * @author 王浩宇
     * @date 9.2
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
     * @author 王浩宇
     * @date 9.2
     */
    public User findUserByUserID(String _strUserID)
    {
        return m_manService.findUserByUserID(_strUserID);
    }

    /**
     * 根据生物特征信息获取User对象
     * @param _btBioRef in BioRef
     * @return User对象
     * @author 王浩宇
     * @date 9.2
     */
    public List<User> findUserByRecgBio(String _btBioRef)
    {
        return m_manService.findUserByRecgBio(_btBioRef);
    }

    /**
     * 获取购物记录的list
     * @param _user in User对象
     * @return list对象
     * @author 王浩宇
     * @date 9.2
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
     * @author 王浩宇
     * @date 9.2
     */
    public void addBuyLog(String _strMngrID,String _strUserID,int _nGoodsID,int _nCnt)
    {
        m_buyService.addBuyLog(_strMngrID, _strUserID, _nGoodsID, _nCnt);
    }

    /**
     * 根据指定Goods类型查询商品
     * @param _strType in Goods类型
     * @return Goods的list对象
     * @author 王浩宇
     * @date 9.2
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
     * @author 王浩宇
     * @date 9.2
     */
    public boolean creatNewAccnt(String _strAccntID,String _strPassword)
    {
        return m_manService.creatNewAccnt(_strAccntID,_strPassword);
    }

    /**
     * 获取商品推荐
     * @param _user in 当前User对象
     * @return Goods的list对象
     * @author 王浩宇
     * @date 9.2
     */
    public List<Goods> getGoodsRcmdForUser(User _user)
    {
        return m_goodsService.getGoodsRcmdGeneralForUser(_user,0,ConfigConst.ACCNT_RCMD_LIMIT);
    }

    /**
     * 根据用户ID获取订单
     * @param _strUserID 用户ID
     * @return Order列表
     */
    public List<Order> findOrderByUserID(String _strUserID)
    {
        return m_buyService.findOrderByUserID(_strUserID);
    }

    /**
     * 通过订单号获取用户订单
     * @param _nOrderID in 订单号
     * @return Order对象
     */
    public Order findOrderByOrderID(int  _nOrderID)
    {
        return m_buyService.findOrderByOrderID(_nOrderID);
    }

    /**
     * 获取订单列表
     * @return 订单列表
     */
    public List<Order> findOrder()
    {
        return m_buyService.findOrder();
    }

    /**
     * 根据订单号删除订单，用于管理员处理订单。
     * @param _nOrderID 订单号
     */
    public void removeOrderByOrderIDForAccnt(int _nOrderID)
    {
        m_buyService.removeOrderByOrderIDForAccnt(_nOrderID);
    }
}