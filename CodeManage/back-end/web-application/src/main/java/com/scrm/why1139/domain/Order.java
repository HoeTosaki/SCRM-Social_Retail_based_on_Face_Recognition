package com.scrm.why1139.domain;

public class Order {

    private String m_strUserID;
    private int m_nGoodsID;
    private String m_dtOrderDate;
    private int m_nOrderCnt;
    private int m_nOrderID;

    private boolean m_isEmpty;//描述当前的对象是否被修改过。

    /**
     * 默认构造函数
     * @author why
     */
    public Order()
    {
        m_isEmpty = true;
    }

    /**
     * 完整购物记录初始化对象
     * @param _strUserID in UserID，顾客信息
     * @param _nGoodsID in GoodsID，商品信息
     * @param _dtOrderDate in OrderDate，购买的日期时间
     * @param _nOrderCnt in OrderCount，购买该商品的数量
     * @author why
     */
    public Order(String _strUserID, int _nGoodsID, String _dtOrderDate, int _nOrderCnt)
    {
        m_isEmpty = false;
        setOrderCnt(_nOrderCnt);
        setOrderDate(_dtOrderDate);
        setGoodsID(_nGoodsID);
        setUserID(_strUserID);
    }

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
     * @author why
     */
    public String getUserID()
    {
        return m_strUserID;
    }

    /**
     * 设置UserID
     * @param _strUserID in UserID
     * @author why
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
     * @author why
     */
    public int getGoodsID()
    {
        return m_nGoodsID;
    }

    /**
     * 设置GoodsID
     * @param _nGoodsID in GoodsID
     * @author why
     */
    public void setGoodsID(int _nGoodsID)
    {
        m_isEmpty = false;
        this.m_nGoodsID = _nGoodsID;
//        this.m_nGoodsID = Optional.ofNullable(_nGoodsID).filter(id->id>=0).orElseThrow();
    }

    /**
     * 获取购买日期时间
     * @return 表示购买日期时间的字符串
     * @author why
     */
    public String getOrderDate()
    {
        return m_dtOrderDate;
    }

    /**
     * 设置购买的日期时间
     * @param _dtOrderDate in 购买的日期时间
     * @author why
     */
    public void setOrderDate(String _dtOrderDate)
    {
        m_isEmpty = false;
        this.m_dtOrderDate = _dtOrderDate;
//        this.m_dtOrderDate = Optional.ofNullable(_dtOrderDate).orElseThrow();
    }

    /**
     * 获取购买商品的数量
     * @return 商品数量
     * @author why
     */
    public int getOrderCnt()
    {
        return m_nOrderCnt;
    }

    /**
     * 设置购买商品的数量
     * @param _nOrderCnt in 商品数量
     * @author why
     */
    public void setOrderCnt(int _nOrderCnt)
    {
        m_isEmpty = false;
        this.m_nOrderCnt = _nOrderCnt;
//        this.m_nOrderCnt = Optional.ofNullable(m_nOrderCnt).orElseThrow();
    }

    public boolean isEmpty()
    {
        return m_isEmpty;
    }


    public int getOrderID()
    {
        return m_nOrderID;
    }

    public void setOrderID(int _nOrderID)
    {
        m_isEmpty = false;
        this.m_nOrderID = _nOrderID;
    }
}
