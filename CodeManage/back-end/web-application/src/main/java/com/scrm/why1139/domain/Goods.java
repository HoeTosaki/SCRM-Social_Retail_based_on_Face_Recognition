package com.scrm.why1139.domain;

import java.io.Serializable;
import java.util.Optional;

public class Goods implements Serializable
{
    private int m_nGoodsID;
    private String m_strGoodsType;
    private String m_strGoodsName;
    private double m_dbPrice;
    private int m_nGoodsCnt;
    private String m_strGoodsDesc;

    public Goods()
    {}

    public Goods(int _nGoodsID, String _strGoodsType, String _strGoodsName, double _dbPrice, int _nGoodsCnt, String _strGoodsDesc)
    {
        setGoodsCnt(_nGoodsCnt);
        setGoodsDesc(_strGoodsDesc);
        setGoodsID(_nGoodsID);
        setGoodsName(_strGoodsName);
        setGoodsType(_strGoodsType);
        setPrice(_dbPrice);
    }

    public int getGoodsID()
    {
        return m_nGoodsID;
    }

    public void setGoodsID(int _nGoodsID)
    {
        m_nGoodsID = _nGoodsID;
//        this.m_nGoodsID = Optional.ofNullable(_nGoodsID).filter(id->id>=0).orElseThrow();
    }

    public String getGoodsType()
    {
        return m_strGoodsType;
    }

    public void setGoodsType(String _strGoodsType)
    {
        this.m_strGoodsType = _strGoodsType;
//        this.m_strGoodsType = Optional.ofNullable(_strGoodsType).orElseThrow();
    }

    public String getGoodsName()
    {
        return m_strGoodsName;
    }

    public void setGoodsName(String _strGoodsName)
    {
        m_strGoodsName = _strGoodsName;
//        this.m_strGoodsName = Optional.ofNullable(_strGoodsName).orElseThrow();
    }

    public double getPrice()
    {
        return m_dbPrice;
    }

    public void setPrice(double _dbPrice)
    {
        m_dbPrice = _dbPrice;
//        this.m_dbPrice = Optional.ofNullable(_dbPrice).filter(prc->prc>0).orElseThrow();
    }

    public int getGoodsCnt()
    {
        return m_nGoodsCnt;
    }

    public void setGoodsCnt(int _nGoodsCnt)
    {
        this.m_nGoodsCnt = _nGoodsCnt;
//        this.m_nGoodsCnt = Optional.ofNullable(_nGoodsCnt).filter(cnt->cnt>=0).orElse(0);
    }

    public String getGoodsDesc()
    {
        return m_strGoodsDesc;
    }

    public void setGoodsDesc(String _strGoodsDesc)
    {
        this.m_strGoodsDesc = _strGoodsDesc;
//        this.m_strGoodsDesc = Optional.ofNullable(_strGoodsDesc).orElse("undefined.");
    }
}
