package com.scrm.why1139.web;

public class LoginCommand
{
    private String m_strUserName;
    private String m_strPassword;

    public LoginCommand()
    {
    }

    public LoginCommand(String _strUserName, String _strPassword)
    {
        this.m_strUserName = _strUserName;
        this.m_strPassword = _strPassword;
    }

    public String getUserName()
    {
        return m_strUserName;
    }

    public void setUserName(String _strUserName)
    {
        this.m_strUserName = _strUserName;
    }

    public String getPassword()
    {
        return m_strPassword;
    }

    public void setPassword(String _strPassword)
    {
        this.m_strPassword = _strPassword;
    }
}
