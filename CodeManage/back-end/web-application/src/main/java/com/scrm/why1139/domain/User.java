package com.scrm.why1139.domain;


import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.Optional;

public class User implements Serializable
{
    private String m_strUserName;
    private String m_strUserID;
    private String m_strPassword;
    private String m_pthBioRef;

    private Resource m_resBioRef;

    public static final String RES_PATH="src/main/resources/static/res/bioref/";


    public static void main(String s[])
    {
        User u = new User();
        u.setBioRef("1.jpg");
        byte[] bt = u.loadBioRef();
        System.out.println(bt.length);
        u.setBioRef("2.jpg");
        u.saveBioRef(bt);
    }


    public User()
    {}

    public User(String _strUserName, String _strUserID, String _strPassword, String _pthBioRef)
    {
        setUserName(_strUserName);
        setUserID(_strUserID);
        setPassword(_strPassword);
        setBioRef(_pthBioRef);
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
        return m_pthBioRef;
    }

    public void setBioRef(String _pthBioRef)
    {
        this.m_pthBioRef = _pthBioRef;
//        this.m_strBioRef = Optional.ofNullable(_strBioRef).orElse("undefined.");
    }

    public boolean isBioRefExists()
    {
        return new FileSystemResource(RES_PATH+m_pthBioRef).exists();
    }

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

    public boolean saveBioRef(byte[] _btBioRef)
    {
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
}
