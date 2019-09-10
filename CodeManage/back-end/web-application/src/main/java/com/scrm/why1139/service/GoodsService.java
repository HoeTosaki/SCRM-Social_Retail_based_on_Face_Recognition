package com.scrm.why1139.service;

import com.scrm.why1139.dao.AnalDao;
import com.scrm.why1139.dao.GoodsDao;
import com.scrm.why1139.dao.OrderDao;
import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.Order;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 面向GoodsDao的Service类。
 * @author 王浩宇
 * @date 9.3
 */
@Service
public class GoodsService {

    @Autowired
    private AnalDao m_analDao;
    @Autowired
    private GoodsDao m_goodsDao;
    @Autowired
    private OrderDao m_orderDao;

    /**
     * 获取商品推荐
     * @param _user in 当前User对象
     * @return Goods的list对象
     * @author 王浩宇
     * @date 9.3
     */
    public List<Goods> getGoodsRcmdGeneralForUser(User _user, int _nOffSet, int _nLimit)
    {
        List<Goods> ret = m_analDao.getRcmdByUser(_user,_nOffSet,_nLimit);
        if(ret == null || ret.isEmpty())
        {
            ret = m_goodsDao.getGoodsAll(30);
        }
        return ret;
    }

    /**
     * 获取全部Goods的列表对象。
     * @param _nLimit in 最大可查询的记录数，当数据库内存储记录数大于该参数值时才有效。
     * @return Goods列表
     * @author 王浩宇
     * @date 9.3
     */
    public List<Goods> getGoodsRelatedForUser(Goods _gds,int _nOffSet,int _nLimit)
    {
        List<Goods> ret = m_analDao.getRcmdByGds(_gds,_nOffSet,_nLimit);
        if(ret == null || ret.isEmpty())
        {
            ret = m_goodsDao.getGoodsAll(30);
        }
        return ret;
    }

    /**
     * 根据指定Goods类型查询商品
     * @param _strType in Goods类型
     * @return Goods的list对象
     * @author 王浩宇
     * @date 9.3
     */
    public List<Goods> getGoodsRcmdByTypeForUser(User _user,String _strType,int _nOffset,int _nLimit)
    {
        //TODO:for XXS, need replacer strategy to do extra work here.
        List<Goods> ret = m_analDao.getRcmdByUserAndType(_user,_strType,_nOffset,_nLimit);
        if(ret == null || ret.isEmpty())
        {
            ret = m_goodsDao.getGoodsAll(30);
        }
        return ret;
    }

    /**
     * 通过GoodsID获取Goods对象
     * @param _nGoodsID in GoodsID
     * @return Goods对象
     * @author 王浩宇
     * @date 9.3
     */
    public Goods findGoodsByGoodsID(int _nGoodsID)
    {
        return m_goodsDao.findGoodsByGoodsID(_nGoodsID);
    }

    /**
     * 通过商品名查找指定商品
     * @param _strGoodsName in 商品名
     * @return 商品Goods对象
     */
    public Goods findGoodsByGoodsName(String _strGoodsName)
    {
        return m_goodsDao.findGoodsByName(_strGoodsName);
    }

    /**
     * 根据GoodsType获取Goods列表
     * @param _strGoodsType in 表示商品类型的字符串
     * @return Goods列表
     * @author 王浩宇
     * @date 9.3
     */
    public List<Goods> findGoodsByGoodsType(String _strGoodsType)
    {
        return m_goodsDao.getGoodsByClass(_strGoodsType,ConfigConst.GOODS_LIMIT);
    }

    /**
     * 通过模糊匹配查找商品
     * @param _strFuzzy in 模糊匹配字段
     * @return Goods列表
     */
    public List<Goods> findGoodsByFuzzy(String _strFuzzy)
    {
        return m_goodsDao.findGoodsByFuzzy(_strFuzzy,ConfigConst.GOODS_LIMIT);
    }

    /**
     * 更新商品信息，新记录将以update形式更新，用于修改已有商品的信息。
     * @param _gds in 待更新的Goods对象，遵循GoodsID readonly的编程假设。
     * @author 王浩宇
     * @date 9.3
     */
    public void updateGoods(Goods _gds)
    {
        m_goodsDao.updateGoods(_gds);
    }

    /**
     * 通过商品ID删除商品
     * @param _gds in 商品对象
     */
    public void delGoods(Goods _gds)
    {
        m_goodsDao.delGoodsByGoodsID(_gds.getGoodsID());
    }

    /**
     * 添加商品
     * @param _gds in 待更新的Goods对象
     * @author 王浩宇
     * @date 9.3
     */
    public void addGoods(Goods _gds)
    {
        m_goodsDao.insertGoods(_gds);
    }


}
