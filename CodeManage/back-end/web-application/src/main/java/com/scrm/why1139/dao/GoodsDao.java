package com.scrm.why1139.dao;

import com.scrm.why1139.domain.Goods;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class GoodsDao {
    private JdbcTemplate m_jdbcTemp;

    @Autowired
    public void setJdbcTemp(JdbcTemplate _jdbcTemp)
    {
        this.m_jdbcTemp = _jdbcTemp;
//        this.m_jdbcTemp = Optional.ofNullable(_jdbcTemp).orElseThrow();
    }

    public int getMatchCountByID(int _nGoodsID)
    {
        String strSql = " SELECT count(*) FROM t_goods WHERE goods_id = ? ";
        return m_jdbcTemp.queryForObject(strSql, new java.lang.Object[]{_nGoodsID}, Integer.class);
    }


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

    public void updateGoods(Goods _goods)
    {
        m_jdbcTemp.update("UPDATE t_goods SET goods_name = ? , goods_type = ? , goods_price = ? , goods_desc = ? , goods_cnt = ? , goods_pic WHERE goods_id = ? ",
                new Object[]{_goods.getGoodsName(), _goods.getGoodsType(), _goods.getPrice(), _goods.getGoodsDesc(), _goods.getGoodsCnt(), _goods.getGoodsID(),_goods.getPic()});
    }

    public void insertGoods(Goods _goods)
    {
        m_jdbcTemp.update("INSERT INTO t_goods(goods_name,goods_type,goods_price,goods_desc,goods_cnt,goods_id,goods_pic) VALUES(?,?,?,?,?,?,?)",
                new Object[]{_goods.getGoodsName(), _goods.getGoodsType(), _goods.getPrice(), _goods.getGoodsDesc(), _goods.getGoodsCnt(), _goods.getGoodsID(),_goods.getPic()});
    }

    public List<Goods> getGoodsByRcmd(User _user)
    {
        //TODO: need to return real rcmd by analysisDao.
        List<Goods> ret = getGoodsAll(100);
        return ret;
    }

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
