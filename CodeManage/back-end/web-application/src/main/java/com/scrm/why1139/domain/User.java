package com.scrm.why1139.domain;


import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.Optional;

/**
 * 用于描述User信息的Domain类。
 * @author 王浩宇
 * @date 9.3
 */
public class User implements Serializable
{
    private String m_strUserName;
    private String m_strUserID;
    private String m_strPassword;
    private String m_pthBioRef;

    private Resource m_resBioRef;


    private String m_imgrecg;

    public static final String RES_PATH="src/main/resources/static/res/bioref/";

    private boolean m_isEmpty;//描述当前对象是否被修改过。

    /**
     * User存储的简易测试类。
     * @author 王浩宇
     * @date 9.3
     */
    public static void main(String s[])
    {
        User u = new User();
        u.setBioRef("1.jpg");
        byte[] bt = u.loadBioRef();
        System.out.println(bt.length);
        u.setBioRef("2.jpg");
        u.saveBioRef(bt);
    }

    /**
     * User的默认构造函数
     * @author 王浩宇
     * @date 9.3
     */
    public User()
    {
        m_isEmpty = true;
    }

    /**
     * 根据完整的User信息构造User对象
     * @param _strUserName in UserName
     * @param _strUserID in UserID
     * @param _strPassword in User密码
     * @param _pthBioRef in User生物特征信息的存储路径
     * @author 王浩宇
     * @date 9.3
     */
    public User(String _strUserName, String _strUserID, String _strPassword, String _pthBioRef)
    {
        m_isEmpty = false;
        setUserName(_strUserName);
        setUserID(_strUserID);
        setPassword(_strPassword);
        setBioRef(_pthBioRef);
    }

    /**
     * 获取UserName
     * @return UserName
     * @author 王浩宇
     * @date 9.3
     */
    public String getUserName()
    {
        return m_strUserName;
    }

    /**
     * 设置UserName
     * @param _strUserName in UserName
     * @author 王浩宇
     * @date 9.3
     */
    public void setUserName(String _strUserName)
    {
        m_isEmpty = false;
//        this.m_strUserName = Optional.ofNullable(_strUserName).orElseThrow();
        this.m_strUserName = _strUserName;
    }

    /**
     * 获取UserID
     * @return UserID
     * @author 王浩宇
     * @date 9.3
     */
    public String getUserID()
    {
        return m_strUserID;
    }

    /**
     * 设置UserID
     * @param _strUserID in UserID
     * @author 王浩宇
     * @date 9.3
     */
    public void setUserID(String _strUserID)
    {
        m_isEmpty = false;
//        this.m_strUserID = Optional.ofNullable(_strUserID).orElseThrow();
        this.m_strUserID = _strUserID;
    }

    /**
     * 获取User密码
     * @return User密码
     * @author 王浩宇
     * @date 9.3
     */
    public String getPassword()
    {
        return m_strPassword;
    }

    /**
     * 设置User密码
     * @param _strPassword in User密码
     * @author 王浩宇
     * @date 9.3
     */
    public void setPassword(String _strPassword)
    {
        m_isEmpty = false;
//        this.m_strPassword = Optional.ofNullable(_strPassword).orElseThrow();
        this.m_strPassword = _strPassword;
    }

    /**
     * 获取User生物信息存储路径
     * @return 存储路径的字符串
     * @author 王浩宇
     * @date 9.3
     */
    public String getBioRef()
    {
        return m_pthBioRef;
    }

    /**
     * 设置User的生物信息路径
     * @param _pthBioRef in 生物信息路径
     * @author 王浩宇
     * @date 9.3
     */
    public void setBioRef(String _pthBioRef)
    {
        m_isEmpty = false;
        this.m_pthBioRef = _pthBioRef;
//        this.m_strBioRef = Optional.ofNullable(_strBioRef).orElse("undefined.");
    }

    /**
     * 判断当前指定路径的生物信息文件是否存在。
     * @return 文件存在性的boolean值
     * @author 王浩宇
     * @date 9.3
     */
    public boolean isBioRefExists()
    {
        return new FileSystemResource(RES_PATH+m_pthBioRef).exists();
    }

    /**
     * 加载User生物信息内容到内存
     * @return 生物信息二进制流
     * @author 王浩宇
     * @date 9.3
     */
    public byte[] loadBioRef()
    {

        byte[] ret = null;
        ByteArrayOutputStream baos = null;
        try
        {
            baos = new ByteArrayOutputStream();
            BufferedInputStream bos = new BufferedInputStream(new FileInputStream(RES_PATH+m_pthBioRef));
            int b = bos.read();
            while(b != -1)
            {
                baos.write(b);
                b = bos.read();
            }
            ret = baos.toByteArray();
        } catch (IOException e)
        { }
        finally
        {
            try
            {
                if(baos!=null)
                    baos.close();
            }
            catch (IOException e)
            { }
        }
        return ret;
    }

    /**
     * 存储内存中的生物信息流到硬盘
     * @param _btBioRef in 二进制流
     * @return 存储通告结果的boolean值
     * @author 王浩宇
     * @date 9.3
     */
    public boolean saveBioRef(byte[] _btBioRef)
    {
        m_isEmpty = false;
        boolean ret = false;
        BufferedOutputStream bos = null;
        try
        {
            bos = new BufferedOutputStream(new FileOutputStream(RES_PATH+m_pthBioRef));
            for(int i = 0;i<_btBioRef.length;++i)
                bos.write(_btBioRef[i]);
            ret = true;
        }
        catch (IOException e)
        {}
        finally
        {
            try
            {
                if(bos!=null)
                    bos.close();
            } catch (IOException e)
            {}
        }
        return ret;
    }

    /**
     * 判断当前对象是否为空
     * @return 逻辑值
     */
    public boolean isEmpty()
    {
        return m_isEmpty;
    }

    /**
     * 获取人脸信息
     * @return String
     */
    public String getM_imgrecg()
    {
        return m_imgrecg;
    }

    /**
     * 设置人脸信息
     * @param m_imgrecg in 人脸信息
     */
    public void setM_imgrecg(String m_imgrecg)
    {
        m_isEmpty = false;
        this.m_imgrecg = m_imgrecg;
    }
}
