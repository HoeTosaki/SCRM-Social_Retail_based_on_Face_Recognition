package com.scrm.why1139.dao;

import com.scrm.why1139.domain.Buy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 用于持久化购物记录的Dao类。
 * @author 王浩宇
 * @date 9.5
 */
@Repository
public class BuyDao
{
    private JdbcTemplate m_jdbcTemp;

    /**
     * setter注入
     * @param _jdbcTemp in
     * @author 王浩宇
     * @date 9.5
     */
    @Autowired
    public void setJdbcTemp(JdbcTemplate _jdbcTemp)
    {
        this.m_jdbcTemp = _jdbcTemp;
//        this.m_jdbcTemp = Optional.ofNullable(_jdbcTemp).orElseThrow();
    }

    /**
     * 通过UserID获取购物记录。
     * @param _strUserID in UserID字符串
     * @param _nLimit in 最大可查询的记录数，当数据库内存储记录数大于该参数值时才有效。
     * @return 购物记录Buy的列表
     * @author 王浩宇
     * @date 9.5
     */
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

    /**
     * 通过GoodID获取购买记录
     * @param _nGoodsID in 商品ID
     * @param _nLimit in 最大可查询的记录数，当数据库内存储记录数大于该参数值时才有效。
     * @return 购物记录Buy的列表
     * @author 王浩宇
     * @date 9.5
     */
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

    /**
     * 通过buyID查询购物记录
     * @param _nBuyID in buyID
     * @return 购物记录Buy对象
     */
    public Buy findBuyByBuyID(int _nBuyID)
    {
        Buy buy = new Buy();
        m_jdbcTemp.query(" SELECT * FROM t_buy WHERE buy_id = ? "
                , new Object[]{_nBuyID}, rs ->
                {
                    buy.setBuyDate(rs.getString("buy_date"));
                    buy.setMngrID(rs.getString("mngr_id"));
                    buy.setBuyCnt(rs.getInt("buy_cnt"));
                    buy.setGoodsID(rs.getInt("goods_id"));
                    buy.setUserID(rs.getString("user_id"));
                    buy.setBuyID(rs.getInt("buy_id"));
                });
        return buy;
    }

    /**
     * 更新当前的购物记录。新记录将以insert形式更新。
     * @param _buy in 每个buy记录遵循时间戳unique的编程假设。
     * @author 王浩宇
     * @date 9.5
     */
    public void updateBuy(Buy _buy)
    {
        m_jdbcTemp.update("INSERT INTO t_buy(user_id,mngr_id,goods_id,buy_date,buy_cnt) VALUES(?,?,?,?,?)",
                new Object[]{_buy.getUserID(),_buy.getMngrID(),_buy.getGoodsID(),_buy.getBuyDate(),_buy.getBuyCnt()});
    }

}

