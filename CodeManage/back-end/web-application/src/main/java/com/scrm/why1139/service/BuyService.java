package com.scrm.why1139.service;

import com.scrm.why1139.dao.BuyDao;
import com.scrm.why1139.dao.GoodsDao;
import com.scrm.why1139.dao.OrderDao;
import com.scrm.why1139.domain.Buy;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.Order;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 面向BuyDao的Service类。
 * @author 王浩宇
 * @date 9.5
 */
@Service
public class BuyService {
    @Autowired
    private BuyDao m_buyDao;
    @Autowired
    private OrderDao m_orderDao;
    @Autowired
    private GoodsDao m_goodsDao;

    /**
     * 获取购物记录的list
     * @param _user in User对象
     * @return list对象
     * @author 王浩宇
     * @date 9.5
     */
    public List<Buy> getBuyLogByUser(User _user)
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
     * @author 王浩宇
     * @date 9.5
     */
    public void addBuyLog(String _strMngrID,String _strUserID,int _nGoodsID,int _nCnt)
    {
        Buy newBuy = new Buy();
        newBuy.setUserID(_strUserID);
        newBuy.setGoodsID(_nGoodsID);
        newBuy.setBuyCnt(_nCnt);
        newBuy.setMngrID(_strMngrID);
        newBuy.setBuyDate(new Date().toString());
        Goods gds = m_goodsDao.findGoodsByGoodsID(newBuy.getGoodsID());
        int nGdsCnt = gds.getGoodsCnt() - newBuy.getBuyCnt();
        if(nGdsCnt >= 0)
        {
            gds.setGoodsCnt(nGdsCnt);
        }
        else
        {
            gds.setGoodsCnt(0);
        }
        m_buyDao.updateBuy(newBuy);
        m_goodsDao.updateGoods(gds);
    }

    /**
     * 通过用户ID获取订单
     * @param _strUserID in 用户ID
     * @return Order列表
     */
    public List<Order> findOrderByUserID(String  _strUserID)
    {
        return m_orderDao.findOrderByUserID(_strUserID,ConfigConst.ORDER_LIMIT);
    }

    /**
     * 通过订单号获取订单
     * @param _nOrderID in 订单号
     * @return Order对象
     */
    public Order findOrderByOrderID(int  _nOrderID)
    {
        return m_orderDao.findOrderByOrderID(_nOrderID);
    }

    /**
     * 为指定用户获取订单
     * @return Order列表
     */
    public List<Order> findOrder()
    {
        User user = new User();
        return m_orderDao.findOrderWithUser(user,ConfigConst.ORDER_LIMIT);
    }

    /**
     * 用户删除订单
     * @param _nOrderID in 订单号
     */
    public void removeOrderByOrderID(int _nOrderID)
    {
        Order order = new Order();
        order.setOrderID(_nOrderID);
        m_orderDao.delOrder(order);
    }

    /**
     * 收银员删除订单
     * @param _nOrderID in 订单号
     */
    public void removeOrderByOrderIDForAccnt(int _nOrderID)
    {
        Order order = new Order();
        order.setOrderID(_nOrderID);
        m_orderDao.delOrder(order);
    }

    /**
     * 添加订单
     * @param _strUserID in 用户ID
     * @param _nGoodsID in 商品ID
     * @param _nCnt in 订单数量
     * @return 逻辑值
     */
    public boolean addOrderLog(String _strUserID,int _nGoodsID,int _nCnt)
    {
        Order newOrder = new Order();
        newOrder.setUserID(_strUserID);
        newOrder.setGoodsID(_nGoodsID);
        newOrder.setOrderCnt(_nCnt);
        newOrder.setOrderDate(new Date().toString());
        m_orderDao.updateOrder(newOrder);
        return true;
        //TODO:dead lock.
    }
}
