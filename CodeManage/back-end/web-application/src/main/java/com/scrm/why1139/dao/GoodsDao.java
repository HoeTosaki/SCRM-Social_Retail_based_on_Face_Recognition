package com.scrm.why1139.dao;

import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 用于持久化商品信息的Dao类。
 * @author why
 */
@Repository
public class GoodsDao
{
    private JdbcTemplate m_jdbcTemp;

    /**
     * setter注入
     * @param _jdbcTemp in
     * @author why
     */
    @Autowired
    public void setJdbcTemp(JdbcTemplate _jdbcTemp)
    {
        this.m_jdbcTemp = _jdbcTemp;
//        this.m_jdbcTemp = Optional.ofNullable(_jdbcTemp).orElseThrow();
    }

    /**
     * 通过GoodsID获取匹配的商品数量
     * @param _nGoodsID in 商品ID
     * @return 商品数量
     * @author why
     */
    public int getMatchCountByID(int _nGoodsID)
    {
        String strSql = " SELECT count(*) FROM t_goods WHERE goods_id = ? ";
        return m_jdbcTemp.queryForObject(strSql, new java.lang.Object[]{_nGoodsID}, Integer.class);
    }

    /**
     * 通过GoodsID获取匹配的Goods对象
     * @param _nGoodsID in 商品ID
     * @return Goods对象
     * @author why
     */
    public Goods findGoodsByID(int _nGoodsID)
    {
        Goods goods = new Goods();
        m_jdbcTemp.query(" SELECT * FROM t_goods WHERE goods_id = ? "
                , new Object[]{_nGoodsID}, rs ->
                {
                    goods.setPrice(rs.getDouble("goods_price"));
                    goods.setGoodsType(rs.getString("goods_type"));
                    goods.setGoodsName(rs.getString("goods_name"));
                    goods.setGoodsID(rs.getInt("goods_id"));
                    goods.setGoodsDesc(rs.getString("goods_desc"));
                    goods.setGoodsCnt(rs.getInt("goods_cnt"));
                    goods.setPic(rs.getString("goods_pic"));
                });
        return goods;
    }

    /**
     * 更新商品信息，新记录将以update形式更新，用于修改已有商品的信息。
     * @param _goods in 待更新的Goods对象，遵循GoodsID readonly的编程假设。
     * @author why
     */
    public void updateGoods(Goods _goods)
    {
        m_jdbcTemp.update("UPDATE t_goods SET goods_name = ? , goods_type = ? , goods_price = ? , goods_desc = ? , goods_cnt = ? , goods_pic WHERE goods_id = ? ",
                new Object[]{_goods.getGoodsName(), _goods.getGoodsType(), _goods.getPrice(), _goods.getGoodsDesc(), _goods.getGoodsCnt(), _goods.getGoodsID(),_goods.getPic()});
    }

    /**
     * 更新商品信息，新记录将以insert形式更新，用于添加新类型的商品。
     * @param _goods in 待更新的Goods对象，遵循GoodsID unique的编程假设。
     * @author why
     */
    public void insertGoods(Goods _goods)
    {
        m_jdbcTemp.update("INSERT INTO t_goods(goods_name,goods_type,goods_price,goods_desc,goods_cnt,goods_id,goods_pic) VALUES(?,?,?,?,?,?,?)",
                new Object[]{_goods.getGoodsName(), _goods.getGoodsType(), _goods.getPrice(), _goods.getGoodsDesc(), _goods.getGoodsCnt(), _goods.getGoodsID(),_goods.getPic()});
    }



    /**
     * 获取全部Goods的列表对象。
     * @param _nLimit in 最大可查询的记录数，当数据库内存储记录数大于该参数值时才有效。
     * @return Goods列表
     * @author why
     */
    public List<Goods> getGoodsAll(int _nLimit)
    {
        List<Goods> lstGds = new CopyOnWriteArrayList<>();
        m_jdbcTemp.query(" SELECT * FROM t_goods"
                ,new Object[]{},rs->
                {
                    int nHits = 0;
                    do
                    {
                        Goods gds = new Goods();
                        gds.setGoodsID(rs.getInt("goods_id"));
                        gds.setPic(rs.getString("goods_pic"));
                        gds.setGoodsCnt(rs.getInt("goods_cnt"));
                        gds.setGoodsDesc(rs.getString("goods_desc"));
                        gds.setGoodsName(rs.getString("goods_name"));
                        gds.setGoodsType(rs.getString("goods_type"));
                        gds.setPrice(rs.getDouble("goods_price"));
                        lstGds.add(gds);
                        ++nHits;
                    }
                    while(nHits < _nLimit && rs.next());
                });
        return lstGds;

    }

    /**
     * 根据GoodsType获取Goods列表
     * @param _strGoodsType in 表示商品类型的字符串
     * @param _nLimit in 最大可查询的记录数，当数据库内存储记录数大于该参数值时才有效。
     * @return Goods列表
     * @author why
     */
    public List<Goods> getGoodsByClass(String _strGoodsType,int _nLimit)
    {
        List<Goods> lstGds = new CopyOnWriteArrayList<>();
        m_jdbcTemp.query(" SELECT * FROM t_goods WHERE goods_type = "+_strGoodsType
                ,new Object[]{},rs->
                {
                    int nHits = 0;
                    do
                    {
                        Goods gds = new Goods();
                        gds.setGoodsID(rs.getInt("goods_id"));
                        gds.setPic(rs.getString("goods_pic"));
                        gds.setGoodsCnt(rs.getInt("goods_cnt"));
                        gds.setGoodsDesc(rs.getString("goods_desc"));
                        gds.setGoodsName(rs.getString("goods_name"));
                        gds.setGoodsType(rs.getString("goods_type"));
                        gds.setPrice(rs.getDouble("goods_price"));
                        lstGds.add(gds);
                        ++nHits;
                    }
                    while(nHits < _nLimit && rs.next());
                });
        return lstGds;
    }

}
