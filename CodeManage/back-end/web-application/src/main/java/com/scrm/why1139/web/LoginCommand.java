package com.scrm.why1139.web;

/**
 * @deprecated 处理登录的Command类，适于jsp驱动的前后端通信，已废弃。使用JSONProc以代替。
 * @author why
 */
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
