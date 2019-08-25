package com.scrm.why1139.service;

import com.scrm.why1139.BioReferenceModule.BioCertificater;
import com.scrm.why1139.dao.BuyDao;
import com.scrm.why1139.dao.GoodsDao;
import com.scrm.why1139.dao.UserDao;
import com.scrm.why1139.domain.Buy;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.Mngr;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * 面向Mngr的Service类。
 * @author why
 */
@Service
public class AccntService extends GeneralService
{
    private GoodsDao m_goodsDao;
    private BuyDao m_buyDao;

    /**
     * setter注入
     * @param _goodsDao in
     * @author why
     */
    @Autowired
    public void setGoodsDao(GoodsDao _goodsDao)
    {
        this.m_goodsDao = _goodsDao;
//        this.m_goodsDao = Optional.ofNullable(_goodsDao).orElseThrow();
    }

    /**
     * setter注入
     * @param _buyDao in
     * @author why
     */
    @Autowired
    public void setBuyDao(BuyDao _buyDao)
    {
        this.m_buyDao = _buyDao;
//        this.m_buyDao = Optional.ofNullable(_buyDao).orElseThrow();
    }

    /**
     * setter注入
     * @param _userDao in
     * @author why
     */
    @Autowired
    public void setUserDao(UserDao _userDao)
    {
        this.m_userDao = _userDao;
    }



    /**
     * 通过GoodsID获取Goods对象
     * @param _nGoodsID in GoodsID
     * @return Goods对象
     * @author why
     */
    public Goods findGoodsByGoodsID(int _nGoodsID)
    {
        return m_goodsDao.findGoodsByID(_nGoodsID);
    }

    /**
     * 根据生物特征信息获取User对象
     * @param _btBioRef in BioRef
     * @return User对象
     * @author why
     */
    public User findUserByBioRef(String _btBioRef)
    {
        //TODO: need impl in future based on Face-Reg modules or some newest tech, ehh, you know.
        List<String> strlstUserID = BioCertificater.certificateUser(_btBioRef);
        if(strlstUserID.size() == 0)
            return new User();
        else
        {
            User userBestFit = m_userDao.findUserByUserID(strlstUserID.get(0));
            return userBestFit;
        }
    }

    /**
     * 获取购物记录的list
     * @param _user in User对象
     * @return list对象
     * @author why
     */
    public List<Buy> getBuyLog(User _user)
    {
        List<Buy> ret = m_buyDao.findBuyByUserID(_user.getUserID(),ConfigConst.BUYLOG_LIMIT);
        return ret;
    }

    /**
     * 添加购物记录
     * @param _strMngrID in MngrID
     * @param _strUserID in UserID
     * @param _nGoodsID in GoodsID
     * @param _nCnt in Goods购买数量
     * @author why
     */
    public void addBuyLog(String _strMngrID,String _strUserID,int _nGoodsID,int _nCnt)
    {
        Buy newBuy = new Buy();
        newBuy.setUserID(_strUserID);
        newBuy.setGoodsID(_nGoodsID);
        newBuy.setBuyCnt(_nCnt);
        newBuy.setMngrID(_strMngrID);
        newBuy.setBuyDate(new Date().toString());
        m_buyDao.updateBuy(newBuy);
    }

    /**
     * 获取商品推荐
     * @param _user in 当前User对象
     * @return Goods的list对象
     * @author why
     */
    public List<Goods> getGoodsRcmd(User _user)
    {
        List<Goods> ret = m_goodsDao.getGoodsByRcmd(_user);
        return ret;
    }

    /**
     * 根据指定Goods类型查询商品
     * @param _strType in Goods类型
     * @return Goods的list对象
     * @author why
     */
    public List<Goods> goodsQuery(String _strType)
    {
        //TODO:for XXS, need replacer strategy to do extra work here.
        List<Goods> ret = m_goodsDao.getGoodsByClass(_strType,ConfigConst.GOODS_LIMIT);
        return ret;
    }

    private static boolean generateImage(String imgStr,String _strID)
    {
        // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        else
            System.out.println(imgStr);
//        BASE64Decoder decoder = new BASE64Decoder();
        try {
// Base64解码
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] b = decoder.decode(imgStr);
//            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
// 生成jpeg图片
            String imgFilePath = _strID+".png";// 新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将Base64格式存储到图片中
     * @deprecated 当前版本无效。
     * @param _strSrcBase64 in Base64格式的用户人脸信息
     * @return 存储是否成功的boolean值
     * @author why
     */
    private static boolean genBase64(String _strSrcBase64)
    {
        String name = "why";
        if(!generateImage(_strSrcBase64,"why"))
            return false;
        ByteArrayOutputStream baos = null;
        try
        {
            baos = new ByteArrayOutputStream();
            BufferedInputStream bos = new BufferedInputStream(new FileInputStream("why.png"));
            int b = bos.read();
            while(b != -1)
            {
                baos.write(b);
                b = bos.read();
            }
//            ret = baos.toByteArray();
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
        return false;
    }

    /**
     * 创建新的Accnt对象
     * @param _strAccntID in AccntID
     * @param _strPassword in 密码
     * @return 创建是否成功的boolean值
     * @author why
     */
    public boolean creatNewAccnt(String _strAccntID,String _strPassword)
    {
        if(!checkString(_strAccntID) || !checkString(_strPassword))
            return false;
        Mngr accnt = new Mngr();
        accnt.setMngrID(_strAccntID);
        accnt.setPassword(_strPassword);
        accnt.setMngrType(2);
        m_mngrDao.insertMngr(accnt);
        return true;
    }
}