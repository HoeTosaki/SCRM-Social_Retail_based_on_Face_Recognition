package com.scrm.why1139.domain;

import java.io.Serializable;
import java.util.Optional;

public class Mngr implements Serializable
{
    private int m_nMngrType;
    private String m_strMngrID;
    private String m_strPassword;

    public Mngr()
    {}

    public Mngr(int _nMngrType, String _strMngrID, String _strPassword)
    {
        setMngrType(_nMngrType);
        setMngrID(_strMngrID);
        setPassword(_strPassword);
    }

    public int getMngrType()
    {
        return m_nMngrType;
    }

    public void setMngrType(int _nMngrType)
    {
//        this.m_nMngrType = Optional.ofNullable(_nMngrType).filter(tp->tp>=0 && tp<=2).orElse(3);
        this.m_nMngrType = _nMngrType;
    }

    public String getMngrID()
    {
        return m_strMngrID;
    }

    public void setMngrID(String _MngrID)
    {
        this.m_strMngrID = _MngrID;
//        this.m_strMngrID = Optional.ofNullable(_MngrID).orElseThrow();
    }

    public String getPassword()
    {
        return m_strPassword;
    }

    public void setPassword(String _strPassword)
    {
        this.m_strPassword = _strPassword;
//        this.m_strPassword = Optional.ofNullable(_strPassword).orElseThrow();
    }
}
