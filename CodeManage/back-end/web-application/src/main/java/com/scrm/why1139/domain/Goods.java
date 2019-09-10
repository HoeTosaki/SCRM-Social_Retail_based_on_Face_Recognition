package com.scrm.why1139.domain;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;

/**
 * 用于描述Goods信息的Domain类。
 * @author 王浩宇
 * @date 9.3
 */
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

    private boolean m_isEmpty;//描述当前对象是否被修改过。

    /**
     * Goods存储的简易测试类。
     * @author 王浩宇
     * @date 9.3
     */
    public static void main(String s[])
    {
        Goods g = new Goods();
        g.setPic("1.jpg");
        byte[] bt = g.loadPic();
        System.out.println(bt.length);
        g.setPic("2.jpg");
        g.savePic(bt);
    }

    /**
     * Goods的默认构造函数
     * @author 王浩宇
     * @date 9.3
     */
    public Goods()
    {
        m_isEmpty = true;
    }

    /**
     * 根据完整的商品信息构造Goods对象
     * @param _nGoodsID in GoodsID
     * @param _strGoodsType in Goods类型
     * @param _strGoodsName in Goods名称
     * @param _dbPrice in Goods售价
     * @param _nGoodsCnt in Goods库存数量
     * @param _strGoodsDesc in Goods描述
     * @param _pthPic in Goods图片展示
     * @author 王浩宇
     * @date 9.3
     */
    public Goods(int _nGoodsID, String _strGoodsType, String _strGoodsName, double _dbPrice, int _nGoodsCnt, String _strGoodsDesc,String _pthPic)
    {
        m_isEmpty = false;
        setGoodsCnt(_nGoodsCnt);
        setGoodsDesc(_strGoodsDesc);
        setGoodsID(_nGoodsID);
        setGoodsName(_strGoodsName);
        setGoodsType(_strGoodsType);
        setPrice(_dbPrice);
        setPic(_pthPic);
    }

    /**
     * 获取GoodsID
     * @return GoodsID
     * @author 王浩宇
     * @date 9.3
     */
    public int getGoodsID()
    {
        return m_nGoodsID;
    }

    /**
     * 设置GoodsID
     * @param _nGoodsID in GoodsID
     * @author 王浩宇
     * @date 9.3
     */
    public void setGoodsID(int _nGoodsID)
    {
        m_isEmpty = false;
        m_nGoodsID = _nGoodsID;
//        this.m_nGoodsID = Optional.ofNullable(_nGoodsID).filter(id->id>=0).orElseThrow();
    }

    /**
     * 获取Goods类型
     * @return Goods类型
     * @author 王浩宇
     * @date 9.3
     */
    public String getGoodsType()
    {
        return m_strGoodsType;
    }

    /**
     * 设置Goods类型
     * @param _strGoodsType in Goods类型
     * @author 王浩宇
     * @date 9.3
     */
    public void setGoodsType(String _strGoodsType)
    {
        m_isEmpty = false;
        this.m_strGoodsType = _strGoodsType;
//        this.m_strGoodsType = Optional.ofNullable(_strGoodsType).orElseThrow();
    }

    /**
     * 获取Goods名称
     * @return Goods名称
     * @author 王浩宇
     * @date 9.3
     */
    public String getGoodsName()
    {
        return m_strGoodsName;
    }

    /**
     * 设置Goods名称
     * @param _strGoodsName in Goods名称
     * @author 王浩宇
     * @date 9.3
     */
    public void setGoodsName(String _strGoodsName)
    {
        m_isEmpty = false;
        m_strGoodsName = _strGoodsName;
//        this.m_strGoodsName = Optional.ofNullable(_strGoodsName).orElseThrow();
    }

    /**
     * 获取Goods价格
     * @return Goods价格
     * @author 王浩宇
     * @date 9.3
     */
    public double getPrice()
    {
        return m_dbPrice;
    }

    /**
     * 设置Goods价格
     * @param _dbPrice in Goods
     * @author 王浩宇
     * @date 9.3
     */
    public void setPrice(double _dbPrice)
    {
        m_isEmpty = false;
        m_dbPrice = _dbPrice;
//        this.m_dbPrice = Optional.ofNullable(_dbPrice).filter(prc->prc>0).orElseThrow();
    }

    /**
     * 获取Goods库存数量
     * @return Goods数量
     * @author 王浩宇
     * @date 9.3
     */
    public int getGoodsCnt()
    {
        return m_nGoodsCnt;
    }

    /**
     * 设置Goods库存数量
     * @param _nGoodsCnt in Goods库存数量
     * @author 王浩宇
     * @date 9.3
     */
    public void setGoodsCnt(int _nGoodsCnt)
    {
        m_isEmpty = false;
        this.m_nGoodsCnt = _nGoodsCnt;
//        this.m_nGoodsCnt = Optional.ofNullable(_nGoodsCnt).filter(cnt->cnt>=0).orElse(0);
    }

    /**
     * 获取Goods描述
     * @return Goods描述
     * @author 王浩宇
     * @date 9.3
     */
    public String getGoodsDesc()
    {
        return m_strGoodsDesc;
    }

    /**
     * 设置Goods描述
     * @param _strGoodsDesc in Goods描述
     * @author 王浩宇
     * @date 9.3
     */
    public void setGoodsDesc(String _strGoodsDesc)
    {
        m_isEmpty = false;
        this.m_strGoodsDesc = _strGoodsDesc;
//        this.m_strGoodsDesc = Optional.ofNullable(_strGoodsDesc).orElse("undefined.");
    }

    /**
     * 获取Goods图片路径
     * @return Goods图片路径
     * @author 王浩宇
     * @date 9.3
     */
    public String getPic()
    {
        return m_pthPic;
    }

    /**
     * 设置Goods图片路径
     * @param _pthPic in Goods图片路径
     * @author 王浩宇
     * @date 9.3
     */
    public void setPic(String _pthPic)
    {
        m_isEmpty = false;
        this.m_pthPic = _pthPic;
    }

    /**
     * 判断当前指定路径的图片文件是否存在。
     * @return 文件存在性的boolean值
     * @author 王浩宇
     * @date 9.3
     */
    public boolean isPicExists()
    {
        return new FileSystemResource(RES_PATH+m_pthPic).exists();
    }

    /**
     * 加载Goods图片内容到内存
     * @return Goods图片的二进制流
     * @author 王浩宇
     * @date 9.3
     */
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

    /**
     * 存储内存中的图片流到硬盘
     * @param _btPic in 二进制图片流
     * @return 存储通告结果的boolean值
     * @author 王浩宇
     * @date 9.3
     */
    public boolean savePic(byte[] _btPic)
    {
        m_isEmpty = false;
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

    /**
     * 判断当前对象是否为空
     * @return 逻辑值
     */
    public boolean isEmpty()
    {
        return m_isEmpty;
    }

}
