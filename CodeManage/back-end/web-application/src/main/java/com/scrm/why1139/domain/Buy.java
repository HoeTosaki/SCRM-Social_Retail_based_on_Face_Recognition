package com.scrm.why1139.domain;


import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

/**
 * 用于描述购物记录的Domain类。
 * @author why
 */
public class Buy implements Serializable
{
    private String m_strUserID;
    private String m_strMngrID;
    private int m_nGoodsID;
    private String m_dtBuyDate;
    private int m_nBuyCnt;
    private int m_nBuyID;

    private boolean m_isEmpty;//描述当前的对象是否被修改过。

    /**
     * 默认构造函数
     * @author why
     */
    public Buy()
    {
        m_isEmpty = true;
    }

    /**
     * 完整购物记录初始化对象
     * @param _strUserID in UserID，顾客信息
     * @param _strMngrID in MngrID，收银员信息
     * @param _nGoodsID in GoodsID，商品信息
     * @param _dtBuyDate in BuyDate，购买的日期时间
     * @param _nBuyCnt in BuyCount，购买该商品的数量
     * @author why
     */
    public Buy(String _strUserID, String _strMngrID, int _nGoodsID, String _dtBuyDate, int _nBuyCnt)
    {
        m_isEmpty = false;
        setBuyCnt(_nBuyCnt);
        setBuyDate(_dtBuyDate);
        setGoodsID(_nGoodsID);
        setMngrID(_strMngrID);
        setUserID(_strUserID);
    }

    public Buy(String _strUserID, String _strMngrID, int _nGoodsID, String _dtBuyDate, int _nBuyCnt,int _nBuyID)
    {
        m_isEmpty = false;
        setBuyCnt(_nBuyCnt);
        setBuyDate(_dtBuyDate);
        setGoodsID(_nGoodsID);
        setMngrID(_strMngrID);
        setUserID(_strUserID);
        setBuyID(_nBuyID);
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
     * 获取MngrID
     * @return MngrID
     * @author why
     */
    public String getMngrID()
    {
        return m_strMngrID;
    }

    /**
     * 设置MngrID
     * @param _strMngrID in MngrID
     * @author why
     */
    public void setMngrID(String _strMngrID)
    {
        m_isEmpty = false;
        this.m_strMngrID = _strMngrID;
//        this.m_strMngrID = Optional.ofNullable(_strMngrID).orElseThrow();
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
    public String getBuyDate()
    {
        return m_dtBuyDate;
    }

    /**
     * 设置购买的日期时间
     * @param _dtBuyDate in 购买的日期时间
     * @author why
     */
    public void setBuyDate(String _dtBuyDate)
    {
        m_isEmpty = false;
        this.m_dtBuyDate = _dtBuyDate;
//        this.m_dtBuyDate = Optional.ofNullable(_dtBuyDate).orElseThrow();
    }

    /**
     * 获取购买商品的数量
     * @return 商品数量
     * @author why
     */
    public int getBuyCnt()
    {
        return m_nBuyCnt;
    }

    /**
     * 设置购买商品的数量
     * @param _nBuyCnt in 商品数量
     * @author why
     */
    public void setBuyCnt(int _nBuyCnt)
    {
        m_isEmpty = false;
        this.m_nBuyCnt = _nBuyCnt;
//        this.m_nBuyCnt = Optional.ofNullable(m_nBuyCnt).orElseThrow();
    }

    public boolean isEmpty()
    {
        return m_isEmpty;
    }


    public int getBuyID()
    {
        return m_nBuyID;
    }

    public void setBuyID(int _nBuyID)
    {
        m_isEmpty = false;
        this.m_nBuyID = _nBuyID;
    }
}
