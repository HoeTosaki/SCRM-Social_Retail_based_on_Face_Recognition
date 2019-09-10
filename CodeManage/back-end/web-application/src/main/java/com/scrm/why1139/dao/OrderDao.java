package com.scrm.why1139.dao;

import com.scrm.why1139.domain.Order;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 用于持久化Order信息的Dao类。
 * @author 王浩宇
 * @date 9.2
 */
@Repository
public class OrderDao {
    private JdbcTemplate m_jdbcTemp;

    /**
     * setter注入
     * @param _jdbcTemp in
     * @author 王浩宇
     * @date 9.2
     */
    @Autowired
    public void setJdbcTemp(JdbcTemplate _jdbcTemp)
    {
        this.m_jdbcTemp = _jdbcTemp;
//        this.m_jdbcTemp = Optional.ofNullable(_jdbcTemp).orElseThrow();
    }

    /**
     * 通过用户ID获取订单
     * @param _strUserID in 用户ID
     * @param _nLimit in 长度限制
     * @return Order列表
     */
    public List<Order> findOrderByUserID(String _strUserID, int _nLimit)
    {

        List<Order> lstOrder = new CopyOnWriteArrayList<>();
        m_jdbcTemp.query(" SELECT * FROM t_order WHERE user_id = ?"
                ,new Object[]{_strUserID},rs->
                {
                    int nHits = 0;
                    do
                    {
                        Order order = new Order();
                        order.setUserID(rs.getString("user_id"));
                        order.setGoodsID(rs.getInt("goods_id"));
                        order.setOrderDate(rs.getString("order_date"));
                        order.setOrderCnt(rs.getInt("order_cnt"));
                        order.setOrderID(rs.getInt("order_id"));
                        lstOrder.add(order);
                        ++nHits;
                    }
                    while(nHits < _nLimit && rs.next());
                });
        return lstOrder;
    }

    /**
     * 通过GoodID获取购买记录
     * @param _nGoodsID in 商品ID
     * @param _nLimit in 最大可查询的记录数，当数据库内存储记录数大于该参数值时才有效。
     * @return 购物记录Order的列表
     * @author 王浩宇
     * @date 9.2
     */
    public List<Order> findOrderByGoodsID(int _nGoodsID,int _nLimit)
    {

        List<Order> lstOrder = new CopyOnWriteArrayList<>();
        m_jdbcTemp.query(" SELECT * FROM t_order WHERE goods_id = ?"
                ,new Object[]{_nGoodsID},rs->
                {
                    int nHits = 0;
                    do
                    {
                        Order order = new Order();
                        order.setUserID(rs.getString("user_id"));
                        order.setGoodsID(rs.getInt("goods_id"));
                        order.setOrderDate(rs.getString("order_date"));
                        order.setOrderCnt(rs.getInt("order_cnt"));
                        order.setOrderID(rs.getInt("order_id"));
                        lstOrder.add(order);
                        ++nHits;
                    }
                    while(nHits < _nLimit && rs.next());
                });
        return lstOrder;
    }

    /**
     * 为指定用户获取订单
     * @param _user in 用户ID
     * @param _nLimit in 长度限制
     * @return Order列表
     */
    public List<Order> findOrderWithUser(User _user,int _nLimit)
    {
        m_jdbcTemp.query(" SELECT * FROM t_order LIMIT 1"
                ,new Object[]{},rs->
                {
                    _user.setUserID(rs.getString("user_id"));
                });

        return findOrderByUserID(_user.getUserID(),_nLimit);
    }

    /**
     * 通过订单号获取订单
     * @param _nOrderID in 订单号
     * @return Order对象
     */
    public Order findOrderByOrderID(int _nOrderID)
    {
        Order order = new Order();
        m_jdbcTemp.query(" SELECT * FROM t_order WHERE order_id = ? "
                , new Object[]{_nOrderID}, rs ->
                {
                    order.setOrderDate(rs.getString("order_date"));
                    order.setOrderCnt(rs.getInt("order_cnt"));
                    order.setGoodsID(rs.getInt("goods_id"));
                    order.setUserID(rs.getString("user_id"));
                    order.setOrderID(rs.getInt("order_id"));
                });
        return order;
    }

    /**
     * 更新当前的购物记录。新记录将以insert形式更新。
     * @param _order in 每个Order记录遵循时间戳unique的编程假设。
     * @author 王浩宇
     * @date 9.2
     */
    public void updateOrder(Order _order)
    {
        m_jdbcTemp.update("INSERT INTO t_order(user_id,goods_id,order_date,order_cnt) VALUES(?,?,?,?)",
                new Object[]{_order.getUserID(),_order.getGoodsID(),_order.getOrderDate(),_order.getOrderCnt()});
    }

    /**
     * 删除订单
     * @param _order in 订单Order对象
     */
    public void delOrder(Order _order)
    {
        System.out.println("del order:\t"+_order.getOrderID());
        m_jdbcTemp.update("DELETE FROM t_order WHERE order_id=?",new Object[]{_order.getOrderID()});
    }
}
