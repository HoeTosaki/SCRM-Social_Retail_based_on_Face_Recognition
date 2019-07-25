package com.scrm.why1139.dao;

import com.scrm.why1139.domain.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GoodsDao {
    private JdbcTemplate m_jdbcTemp;

    @Autowired
    public void setJdbcTemp(JdbcTemplate _jdbcTemp)
    {
        this.m_jdbcTemp = _jdbcTemp;
//        this.m_jdbcTemp = Optional.ofNullable(_jdbcTemp).orElseThrow();
    }

    public int getMatchCountByID(int _nGoodsID) {
        String strSql = " SELECT count(*) FROM t_goods WHERE goods_id = ? ";
        return m_jdbcTemp.queryForObject(strSql, new java.lang.Object[]{_nGoodsID}, Integer.class);
    }

    public Goods findGoodsByID(int _nGoodsID) {
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
                });
        return goods;
    }

    public void updateGoods(Goods _goods) {
        m_jdbcTemp.update("UPDATE t_goods SET goods_name = ? , goods_type = ? goods_price = ? , goods_desc = ? , goods_cnt = ? WHERE goods_id = ? ",
                new Object[]{_goods.getGoodsName(), _goods.getGoodsType(), _goods.getPrice(), _goods.getGoodsDesc(), _goods.getGoodsCnt(), _goods.getGoodsID()});
    }
}
