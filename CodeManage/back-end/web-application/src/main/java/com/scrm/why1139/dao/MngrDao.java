package com.scrm.why1139.dao;

import com.scrm.why1139.domain.Mngr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用于持久化Mngr信息的Dao类。
 * @author why
 */
@Repository
public class MngrDao
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
     * 通过业务员的用户信息，获取在数据库的匹配数量。
     * @param _strMngrID in 表示业务员ID的字符串
     * @param _strPassword in 表示密码的字符串
     * @return 匹配的结果，匹配失败返回0
     * @author why
     */
    public int getMatchCount(String _strMngrID,String _strPassword)
    {
        String strSql = " SELECT count(*) FROM t_mngr WHERE mngr_id = ? and password = ? ";
        return m_jdbcTemp.queryForObject(strSql,new java.lang.Object[]{_strMngrID,_strPassword},Integer.class);
    }

    /**
     * 通过业务员ID获取Mngr对象。
     * @param _strMngrID 表示业务员ID的字符串
     * @return Mngr对象
     * @author why
     */
    public Mngr findMngrByMngrID(String _strMngrID)
    {
        Mngr mngr = new Mngr();
        m_jdbcTemp.query(" SELECT * FROM t_mngr WHERE mngr_id = ? "
                ,new Object[]{_strMngrID},rs->
                {
                    mngr.setMngrID(rs.getString("mngr_id"));
                    mngr.setMngrType(rs.getInt("mngr_type"));
                    mngr.setPassword(rs.getString("password"));
                });
        return mngr;
    }

    /**
     * 更新mngr信息，新记录将以update形式更新，用于修改已有用户的个人信息。
     * @param _mngr in 待更新的Mngr对象，遵循mngrID readonly的编程假设。
     * @author why
     */
    public void updateMngr(Mngr _mngr)
    {
        m_jdbcTemp.update("UPDATE t_mngr SET mngr_type = ? , password = ? WHERE mngr_id = ? ",
                new Object[]{_mngr.getMngrType(),_mngr.getPassword(),_mngr.getMngrID()});
    }

    /**
     * 更新mngr信息，新记录将以insert形式更新，用于添加新用户。
     * @param _mngr in 待更新的Mngr对象，遵循mngrID unique的编程假设。
     * @author why
     */
    public void insertMngr(Mngr _mngr)
    {
        m_jdbcTemp.update("INSERT INTO t_mngr(mngr_type,mngr_id,password) VALUES(?,?,?)",
                new Object[]{_mngr.getMngrType(),_mngr.getMngrID(),_mngr.getPassword()});
    }

    public void delMngr(Mngr _mngr)
    {
        m_jdbcTemp.update("DELETE FROM t_mngr WHERE mngr_id=?",new Object[]{_mngr.getMngrID()});
    }

}
