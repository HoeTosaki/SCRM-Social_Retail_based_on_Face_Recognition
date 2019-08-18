package com.scrm.why1139.dao;

import com.scrm.why1139.domain.Buy;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class BuyDao
{
    private JdbcTemplate m_jdbcTemp;

    @Autowired
    public void setJdbcTemp(JdbcTemplate _jdbcTemp)
    {
        this.m_jdbcTemp = _jdbcTemp;
//        this.m_jdbcTemp = Optional.ofNullable(_jdbcTemp).orElseThrow();
    }

    public List<Buy> findBuyByUserID(String _strUserID,int _nLimit)
    {

        List<Buy> lstBuy = new CopyOnWriteArrayList<>();
        m_jdbcTemp.query(" SELECT * FROM t_buy WHERE user_id = ?"
                ,new Object[]{_strUserID},rs->
                {
                    int nHits = 0;
                    do
                    {
                        Buy buy = new Buy();
                        buy.setUserID(rs.getString("user_id"));
                        buy.setMngrID(rs.getString("mngr_id"));
                        buy.setGoodsID(rs.getInt("goods_id"));
                        buy.setBuyDate(rs.getString("buy_date"));
                        buy.setBuyCnt(rs.getInt("buy_cnt"));
                        lstBuy.add(buy);
                        ++nHits;
                    }
                    while(nHits < _nLimit && rs.next());
                });
        return lstBuy;
    }

    public List<Buy> findBuyByGoodsID(int _nGoodsID,int _nLimit)
    {

        List<Buy> lstBuy = new CopyOnWriteArrayList<>();
        m_jdbcTemp.query(" SELECT * FROM t_buy WHERE goods_id = ?"
                ,new Object[]{_nGoodsID},rs->
                {
                    int nHits = 0;
                    do
                    {
                        Buy buy = new Buy();
                        buy.setUserID(rs.getString("user_id"));
                        buy.setMngrID(rs.getString("mngr_id"));
                        buy.setGoodsID(rs.getInt("goods_id"));
                        buy.setBuyDate(rs.getString("buy_date"));
                        buy.setBuyCnt(rs.getInt("buy_cnt"));
                        lstBuy.add(buy);
                        ++nHits;
                    }
                    while(nHits < _nLimit && rs.next());
                });
        return lstBuy;
    }

    public void updateBuy(Buy _buy)
    {
        m_jdbcTemp.update("INSERT INTO t_buy(user_id,mngr_id,goods_id,buy_date,buy_cnt) VALUES(?,?,?,?,?)",
                new Object[]{_buy.getUserID(),_buy.getMngrID(),_buy.getGoodsID(),_buy.getBuyDate(),_buy.getBuyCnt()});
    }



}

