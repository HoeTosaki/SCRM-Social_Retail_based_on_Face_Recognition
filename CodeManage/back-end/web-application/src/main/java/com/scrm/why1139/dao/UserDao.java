package com.scrm.why1139.dao;

import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用于持久化User信息的Dao类。
 * @author why
 */
@Repository
public class UserDao
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
//        this.m_jdbcTemp = Optional.ofNullable(_jdbcTemp).orElseThrow();
        this.m_jdbcTemp = _jdbcTemp;
    }

    /**
     * 通过User的个人信息，获取在数据库的匹配数量。
     * @param _strUserID in 表示UserID的字符串
     * @param _strPassword in 表示密码的字符串
     * @return 匹配的结果，匹配失败返回0
     * @author why
     */
    public int getMatchCount(String _strUserID,String _strPassword)
    {
        String strSql = " SELECT count(*) FROM t_user WHERE user_id = ? and password = ? ";
        return m_jdbcTemp.queryForObject(strSql, new java.lang.Object[]{_strUserID, _strPassword}, Integer.class);
    }

    /**
     * 通过业务员ID获取User对象。
     * @param _strUserID 表示UserID的字符串
     * @return User对象
     * @author why
     */
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

    /**
     * 更新user信息，新记录将以update形式更新，用于修改已有用户的个人信息。
     * @param _user in 待更新的user对象，遵循userID readonly的编程假设。
     * @author why
     */
    public void updateUser(User _user)
    {
        m_jdbcTemp.update("UPDATE t_user SET user_name = ? , password = ? , bio_ref = ? WHERE user_id = ? ",
                new Object[]{_user.getUserName(),_user.getPassword(),_user.getBioRef(),_user.getUserID()});
    }

    /**
     * 更新user信息，新记录将以insert形式更新，用于添加新用户。
     * @param _user in 待更新的user对象，遵循userID unique的编程假设。
     * @author why
     */
    public void insertUser(User _user)
    {
        m_jdbcTemp.update("INSERT INTO t_user(user_name,user_id,password,bio_ref) VALUES(?,?,?,?)",
                new Object[]{_user.getUserName(),_user.getUserID(),_user.getPassword(),_user.getBioRef()});
    }

}
