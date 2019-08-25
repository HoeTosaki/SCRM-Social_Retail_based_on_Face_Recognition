package com.scrm.why1139.web;

/**
 * @deprecated 处理登录的Command类，适于jsp驱动的前后端通信，已废弃。使用JSONProc以代替。
 * @author why
 */
public class LoginCommand
{
    private String m_strUserName;
    private String m_strPassword;

    /**
     * 默认构造方法
     * @author why
     */
    public LoginCommand()
    {
    }

    /**
     * 通过用户名米密码的构造方法
     * @param _strUserName in 用户名
     * @param _strPassword in 密码
     * @author why
     */
    public LoginCommand(String _strUserName, String _strPassword)
    {
        this.m_strUserName = _strUserName;
        this.m_strPassword = _strPassword;
    }

    /**
     * 获取用户名
     * @return String
     * @author why
     */
    public String getUserName()
    {
        return m_strUserName;
    }

    /**
     * 设置用户名
     * @param _strUserName in 用户名
     * @author why
     */
    public void setUserName(String _strUserName)
    {
        this.m_strUserName = _strUserName;
    }

    /**
     * 获取密码
     * @return String格式密码
     * @author why
     */
    public String getPassword()
    {
        return m_strPassword;
    }

    /**
     * 设置密码
     * @param _strPassword in 密码
     * @author why
     */
    public void setPassword(String _strPassword)
    {
        this.m_strPassword = _strPassword;
    }
}
