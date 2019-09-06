package com.scrm.why1139.service;

import com.scrm.why1139.dao.AnalDao;
import com.scrm.why1139.dao.GoodsDao;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private AnalDao m_analDao;
    @Autowired
    private GoodsDao m_goodsDao;

    /**
     * 获取商品推荐
     * @param _user in 当前User对象
     * @return Goods的list对象
     * @author why
     */
    public List<Goods> getGoodsRcmdGeneralForUser(User _user, int _nOffSet, int _nLimit)
    {
        List<Goods> ret = m_analDao.getRcmdByUser(_user,_nOffSet,_nLimit);
        return ret;
    }

    public List<Goods> getGoodsRelatedForUser(Goods _gds,int _nOffSet,int _nLimit)
    {
        List<Goods> ret = m_analDao.getRcmdByGds(_gds,_nOffSet,_nLimit);
        return ret;
    }

    /**
     * 根据指定Goods类型查询商品
     * @param _strType in Goods类型
     * @return Goods的list对象
     * @author why
     */
    public List<Goods> getGoodsRcmdByTypeForUser(User _user,String _strType,int _nOffset,int _nLimit)
    {
        //TODO:for XXS, need replacer strategy to do extra work here.
        List<Goods> ret = m_analDao.getRcmdByUserAndType(_user,_strType,_nOffset,_nLimit);
        return ret;
    }

    /**
     * 通过GoodsID获取Goods对象
     * @param _nGoodsID in GoodsID
     * @return Goods对象
     * @author why
     */
    public Goods findGoodsByGoodsID(int _nGoodsID)
    {
        return m_goodsDao.findGoodsByGoodsID(_nGoodsID);
    }

    public Goods findGoodsByGoodsName(String _strGoodsName)
    {
        return m_goodsDao.findGoodsByName(_strGoodsName);
    }

    public List<Goods> findGoodsByGoodsType(String _strGoodsType)
    {
        return m_goodsDao.getGoodsByClass(_strGoodsType,ConfigConst.GOODS_LIMIT);
    }

    public void updateGoods(Goods _gds)
    {
        m_goodsDao.updateGoods(_gds);
    }

    public void delGoods(Goods _gds)
    {
        m_goodsDao.delGoodsByGoodsID(_gds.getGoodsID());
    }

    public void addGoods(Goods _gds)
    {
        m_goodsDao.insertGoods(_gds);
    }

}
