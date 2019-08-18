package com.scrm.why1139.domain;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;

public class Goods implements Serializable
{
    private int m_nGoodsID;
    private String m_strGoodsType;
    private String m_strGoodsName;
    private double m_dbPrice;
    private int m_nGoodsCnt;
    private String m_strGoodsDesc;
    private String m_pthPic;

    private Resource m_resPic;

    public static final String RES_PATH="src/main/resources/static/res/gdspic/";

    public static void main(String s[])
    {
        Goods g = new Goods();
        g.setPic("1.jpg");
        byte[] bt = g.loadPic();
        System.out.println(bt.length);
        g.setPic("2.jpg");
        g.savePic(bt);
    }


    public Goods()
    {}

    public Goods(int _nGoodsID, String _strGoodsType, String _strGoodsName, double _dbPrice, int _nGoodsCnt, String _strGoodsDesc,String _pthPic)
    {
        setGoodsCnt(_nGoodsCnt);
        setGoodsDesc(_strGoodsDesc);
        setGoodsID(_nGoodsID);
        setGoodsName(_strGoodsName);
        setGoodsType(_strGoodsType);
        setPrice(_dbPrice);
        setPic(_pthPic);
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

    public String getPic()
    {
        return m_pthPic;
    }

    public void setPic(String _pthPic)
    {
        this.m_pthPic = _pthPic;
    }

    public boolean isPicExists()
    {
        return new FileSystemResource(RES_PATH+m_pthPic).exists();
    }

    public byte[] loadPic()
    {
        byte[] ret = null;
        ByteArrayOutputStream baos = null;
        try
        {
            baos = new ByteArrayOutputStream();
            BufferedInputStream bos = new BufferedInputStream(new FileInputStream(RES_PATH+m_pthPic));
            int b = bos.read();
            while(b != -1)
            {
                baos.write(b);
                b = bos.read();
            }
//            baos.write(bos.readAllBytes());
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

    public boolean savePic(byte[] _btPic)
    {
        boolean ret = false;
//        PrintWriter pw = null;
        BufferedOutputStream bos = null;
        try
        {
            bos = new BufferedOutputStream(new FileOutputStream(RES_PATH+m_pthPic));
            for(int i = 0;i<_btPic.length;++i)
                bos.write(_btPic[i]);
//            pw = new PrintWriter("res/"+m_pthPic);
//            Arrays.asList(_btPic).forEach(System.out::print);
//            pw.flush();
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
