package com.scrm.why1139.dao;

import com.scrm.why1139.domain.Mngr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MngrDao
{
    private JdbcTemplate m_jdbcTemp;

    @Autowired
    public void setJdbcTemp(JdbcTemplate _jdbcTemp)
    {
        this.m_jdbcTemp = _jdbcTemp;
//        this.m_jdbcTemp = Optional.ofNullable(_jdbcTemp).orElseThrow();
    }

    public int getMatchCount(String _strMngrID,String _strPassword)
    {
        String strSql = " SELECT count(*) FROM t_mngr WHERE mngr_id = ? and password = ? ";
        return m_jdbcTemp.queryForObject(strSql,new java.lang.Object[]{_strMngrID,_strPassword},Integer.class);
    }

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

    public void updateMngr(Mngr _mngr)
    {
        m_jdbcTemp.update("UPDATE t_mngr SET mngr_type = ? , password = ? WHERE mngr_id = ? ",
                new Object[]{_mngr.getMngrType(),_mngr.getPassword(),_mngr.getMngrID()});
    }

    public void insertMngr(Mngr _mngr)
    {
        m_jdbcTemp.update("INSERT INTO t_mngr(mngr_type,mngr_id,password) VALUES(?,?,?)",
                new Object[]{_mngr.getMngrType(),_mngr.getMngrID(),_mngr.getPassword()});
    }

}
