package com.scrm.why1139.domain;


import java.io.Serializable;
import java.util.Optional;

public class User implements Serializable
{
    private String m_strUserName;
    private String m_strUserID;
    private String m_strPassword;
    private String m_strBioRef;

    public User()
    {}

    public User(String _strUserName, String _strUserID, String _strPassword, String _strBioRef)
    {
        setUserName(_strUserName);
        setUserID(_strUserID);
        setPassword(_strPassword);
        setBioRef(_strBioRef);
    }

    public String getUserName()
    {
        return m_strUserName;
    }

    public void setUserName(String _strUserName)
    {
//        this.m_strUserName = Optional.ofNullable(_strUserName).orElseThrow();
        this.m_strUserName = _strUserName;
    }

    public String getUserID()
    {
        return m_strUserID;
    }

    public void setUserID(String _strUserID)
    {
//        this.m_strUserID = Optional.ofNullable(_strUserID).orElseThrow();
        this.m_strUserID = _strUserID;
    }

    public String getPassword()
    {
        return m_strPassword;
    }

    public void setPassword(String _strPassword)
    {
//        this.m_strPassword = Optional.ofNullable(_strPassword).orElseThrow();
        this.m_strPassword = _strPassword;
    }

    public String getBioRef()
    {
        return m_strBioRef;
    }

    public void setBioRef(String _strBioRef)
    {
        this.m_strBioRef = _strBioRef;
//        this.m_strBioRef = Optional.ofNullable(_strBioRef).orElse("undefined.");
    }
}
