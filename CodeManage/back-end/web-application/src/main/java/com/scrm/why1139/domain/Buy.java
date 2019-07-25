package com.scrm.why1139.domain;


import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

public class Buy implements Serializable
{
    private String m_strUserID;
    private String m_strMngrID;
    private int m_nGoodsID;
    private Date m_dtBuyDate;
    private int m_nBuyCnt;

    public Buy()
    {}

    public Buy(String _strUserID, String _strMngrID, int _nGoodsID, Date _dtBuyDate, int _nBuyCnt)
    {
        setBuyCnt(_nBuyCnt);
        setBuyDate(_dtBuyDate);
        setGoodsID(_nGoodsID);
        setMngrID(_strMngrID);
        setUserID(_strUserID);
    }

    public String getUserID()
    {
        return m_strUserID;
    }

    public void setUserID(String _strUserID)
    {
        this.m_strUserID = _strUserID;
//        this.m_strUserID = Optional.ofNullable(_strUserID).orElseThrow();
    }

    public String getMngrID()
    {
        return m_strMngrID;
    }

    public void setMngrID(String _strMngrID)
    {
        this.m_strMngrID = _strMngrID;
//        this.m_strMngrID = Optional.ofNullable(_strMngrID).orElseThrow();
    }

    public int getGoodsID()
    {
        return m_nGoodsID;
    }

    public void setGoodsID(int _nGoodsID)
    {
        this.m_nGoodsID = _nGoodsID;
//        this.m_nGoodsID = Optional.ofNullable(_nGoodsID).filter(id->id>=0).orElseThrow();
    }

    public Date getBuyDate()
    {
        return m_dtBuyDate;
    }

    public void setBuyDate(Date _dtBuyDate)
    {
        this.m_dtBuyDate = _dtBuyDate;
//        this.m_dtBuyDate = Optional.ofNullable(_dtBuyDate).orElseThrow();
    }

    public int getBuyCnt()
    {
        return m_nBuyCnt;
    }

    public void setBuyCnt(int _nBuyCnt)
    {
        this.m_nBuyCnt = _nBuyCnt;
//        this.m_nBuyCnt = Optional.ofNullable(m_nBuyCnt).orElseThrow();
    }
}
