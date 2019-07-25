package com.scrm.why1139.dao;

import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDao
{
    private JdbcTemplate m_jdbcTemp;

    @Autowired
    public void setJdbcTemp(JdbcTemplate _jdbcTemp)
    {
//        this.m_jdbcTemp = Optional.ofNullable(_jdbcTemp).orElseThrow();
        this.m_jdbcTemp = _jdbcTemp;
    }

    public int getMatchCount(String _strUserID,String _strPassword)
    {
        String strSql = " SELECT count(*) FROM t_user WHERE user_id = ? and password = ? ";
        return m_jdbcTemp.queryForObject(strSql,new java.lang.Object[]{_strUserID,_strPassword},Integer.class);
    }

    public User findUserByUserID(String _strUserID)
    {
        User user = new User();
        m_jdbcTemp.query(" SELECT * FROM t_user WHERE user_id = ? "
                ,new Object[]{_strUserID},rs->
                {
                    user.setUserID(rs.getString("user_id"));
                    user.setUserName(rs.getString("user_name"));
                    user.setPassword(rs.getString("password"));
                    user.setBioRef(rs.getString("bio_ref"));
                });
        return user;
    }

    public void updateUser(User _user)
    {
        m_jdbcTemp.update("UPDATE t_user SET user_name = ? , password = ? , bio_ref = ? WHERE user_id = ? ",
                new Object[]{_user.getUserName(),_user.getPassword(),_user.getBioRef(),_user.getUserID()});
    }
}
