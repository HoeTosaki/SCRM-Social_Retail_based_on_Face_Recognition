package com.scrm.why1139.domain;

/**
 * 用于描述Order信息的Domain类。
 * @author 王浩宇
 * @date 9.3
 */
public class Order {

    private String m_strUserID;
    private int m_nGoodsID;
    private String m_dtOrderDate;
    private int m_nOrderCnt;
    private int m_nOrderID;

    private boolean m_isEmpty;//描述当前的对象是否被修改过。

    /**
     * 默认构造函数
     * @author 王浩宇
     * @date 9.3
     */
    public Order()
    {
        m_isEmpty = true;
    }

    /**
     * 完整Order初始化对象
     * @param _strUserID in UserID，顾客信息
     * @param _nGoodsID in GoodsID，商品信息
     * @param _dtOrderDate in OrderDate，购买的日期时间
     * @param _nOrderCnt in OrderCount，购买该商品的数量
     * @author 王浩宇
     * @date 9.3
     */
    public Order(String _strUserID, int _nGoodsID, String _dtOrderDate, int _nOrderCnt)
    {
        m_isEmpty = false;
        setOrderCnt(_nOrderCnt);
        setOrderDate(_dtOrderDate);
        setGoodsID(_nGoodsID);
        setUserID(_strUserID);
    }

    /**
     * 补充OrderID的初始化对象
     * @param _strUserID in UserID，顾客信息
     * @param _nGoodsID in GoodsID，商品信息
     * @param _dtOrderDate in OrderDate，购买的日期时间
     * @param _nOrderCnt in OrderCount，购买该商品的数量
     * @param _nOrderID in 订单号
     */
    public Order(String _strUserID, int _nGoodsID, String _dtOrderDate, int _nOrderCnt,int _nOrderID)
    {
        m_isEmpty = false;
        setOrderCnt(_nOrderCnt);
        setOrderDate(_dtOrderDate);
        setGoodsID(_nGoodsID);
        setUserID(_strUserID);
        setOrderID(_nOrderID);
    }

    /**
     * 获取UserID
     * @return UserID
     * @author 王浩宇
     * @date 9.3
     */
    public String getUserID()
    {
        return m_strUserID;
    }

    /**
     * 设置UserID
     * @param _strUserID in UserID
     * @author 王浩宇
     * @date 9.3
     */
    public void setUserID(String _strUserID)
    {
        m_isEmpty = false;
        this.m_strUserID = _strUserID;
//        this.m_strUserID = Optional.ofNullable(_strUserID).orElseThrow();
    }

    /**
     * 获取GoodsID
     * @return GoodsID
     * @author 王浩宇
     * @date 9.3
     */
    public int getGoodsID()
    {
        return m_nGoodsID;
    }

    /**
     * 设置GoodsID
     * @param _nGoodsID in GoodsID
     * @author 王浩宇
     * @date 9.3
     */
    public void setGoodsID(int _nGoodsID)
    {
        m_isEmpty = false;
        this.m_nGoodsID = _nGoodsID;
//        this.m_nGoodsID = Optional.ofNullable(_nGoodsID).filter(id->id>=0).orElseThrow();
    }

    /**
     * 获取预约日期时间
     * @return 表示预约日期时间的字符串
     * @author 王浩宇
     * @date 9.3
     */
    public String getOrderDate()
    {
        return m_dtOrderDate;
    }

    /**
     * 设置预约的日期时间
     * @param _dtOrderDate in 预约的日期时间
     * @author 王浩宇
     * @date 9.3
     */
    public void setOrderDate(String _dtOrderDate)
    {
        m_isEmpty = false;
        this.m_dtOrderDate = _dtOrderDate;
//        this.m_dtOrderDate = Optional.ofNullable(_dtOrderDate).orElseThrow();
    }

    /**
     * 获取预约商品的数量
     * @return 商品数量
     * @author 王浩宇
     * @date 9.3
     */
    public int getOrderCnt()
    {
        return m_nOrderCnt;
    }

    /**
     * 设置预约商品的数量
     * @param _nOrderCnt in 商品数量
     * @author 王浩宇
     * @date 9.3
     */
    public void setOrderCnt(int _nOrderCnt)
    {
        m_isEmpty = false;
        this.m_nOrderCnt = _nOrderCnt;
//        this.m_nOrderCnt = Optional.ofNullable(m_nOrderCnt).orElseThrow();
    }

    /**
     * 判断当前对象是否为空
     * @return 逻辑值
     */

    public boolean isEmpty()
    {
        return m_isEmpty;
    }

    /**
     * 获取订单号
     * @return 订单号
     */
    public int getOrderID()
    {
        return m_nOrderID;
    }

    /**
     * 设置订单号
     * @param _nOrderID in 订单号
     */
    public void setOrderID(int _nOrderID)
    {
        m_isEmpty = false;
        this.m_nOrderID = _nOrderID;
    }
}
