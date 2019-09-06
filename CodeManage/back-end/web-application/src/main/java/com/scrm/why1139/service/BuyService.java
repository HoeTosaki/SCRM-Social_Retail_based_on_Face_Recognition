package com.scrm.why1139.service;

import com.scrm.why1139.dao.BuyDao;
import com.scrm.why1139.domain.Buy;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BuyService {
    @Autowired
    private BuyDao m_buyDao;

    /**
     * 获取购物记录的list
     * @param _user in User对象
     * @return list对象
     * @author why
     */
    public List<Buy> getBuyLogByUser(User _user)
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


}
