package com.scrm.why1139.domain;

import java.io.Serializable;
import java.util.Optional;

/**
 * 用于描述Mngr信息的Domain类。
 * @author 王浩宇
 * @date 9.4
 */
public class Mngr implements Serializable
{
    private int m_nMngrType;
    private String m_strMngrID;
    private String m_strPassword;

    private boolean m_isEmpty;//描述当前对象是否被修改过。

    /**
     * Mngr的默认构造函数
     * @author 王浩宇
     * @date 9.4
     */
    public Mngr()
    {
        m_isEmpty = true;
    }

    /**
     * 根据完整的Mngr信息构造Mngr对象
     * @param _nMngrType in Mngr类型
     * @param _strMngrID in MngrID
     * @param _strPassword in Mngr密码
     * @author 王浩宇
     * @date 9.4
     */
    public Mngr(int _nMngrType, String _strMngrID, String _strPassword)
    {
        m_isEmpty = false;
        setMngrType(_nMngrType);
        setMngrID(_strMngrID);
        setPassword(_strPassword);
    }

    /**
     * 获取Mngr类型。
     * 0 - 超级管理员
     * 1 - 管理员
     * 2 - 收银员
     * 3 - 无权限用户
     * @return 表示Mngr类型的int值
     * @author 王浩宇
     * @date 9.4
     */
    public int getMngrType()
    {
        return m_nMngrType;
    }

    /**
     * 设置Mngr类型。
     * 0 - 超级管理员
     * 1 - 管理员
     * 2 - 收银员
     * 3 - 无权限用户
     * @param _nMngrType in 表示Mngr类型的int值
     * @author 王浩宇
     * @date 9.4
     */
    public void setMngrType(int _nMngrType)
    {
        m_isEmpty = false;
//        this.m_nMngrType = Optional.ofNullable(_nMngrType).filter(tp->tp>=0 && tp<=2).orElse(3);
        this.m_nMngrType = _nMngrType;
    }

    /**
     * 获取MngrID
     * @return MngrID
     * @author 王浩宇
     * @date 9.4
     */
    public String getMngrID()
    {
        return m_strMngrID;
    }

    /**
     * 设置MngrID
     * @param _MngrID in MngrID
     * @author 王浩宇
     * @date 9.4
     */
    public void setMngrID(String _MngrID)
    {
        m_isEmpty = false;
        this.m_strMngrID = _MngrID;
//        this.m_strMngrID = Optional.ofNullable(_MngrID).orElseThrow();
    }

    /**
     * 获取Mngr密码
     * @return Mngr密码
     * @author 王浩宇
     * @date 9.4
     */
    public String getPassword()
    {
        return m_strPassword;
    }

    /**
     * 设置Mngr密码
     * @param _strPassword in Mngr密码
     * @author 王浩宇
     * @date 9.4
     */
    public void setPassword(String _strPassword)
    {
        m_isEmpty = false;
        this.m_strPassword = _strPassword;
//        this.m_strPassword = Optional.ofNullable(_strPassword).orElseThrow();
    }

    /**
     * 判断当前对象是否为空
     * @return 逻辑值
     */
    public boolean isEmpty()
    {
        return m_isEmpty;
    }
}
