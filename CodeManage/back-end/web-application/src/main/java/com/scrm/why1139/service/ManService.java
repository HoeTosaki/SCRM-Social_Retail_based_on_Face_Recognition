package com.scrm.why1139.service;

import com.scrm.why1139.dao.BioDao;
import com.scrm.why1139.dao.MngrDao;
import com.scrm.why1139.dao.UserDao;
import com.scrm.why1139.domain.Mngr;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 提供人员管理基础的Service类。
 * @author 王浩宇
 */
@Service
public class ManService
{
    protected UserDao m_userDao;
    protected MngrDao m_mngrDao;

    @Autowired
    private BioDao m_bioDao;

    /**
     * setter注入
     * @param _userDao in
     * @author 王浩宇
     */
    @Autowired
    public void setUserDao(UserDao _userDao)
    {
        this.m_userDao = _userDao;
//        this.m_userDao = Optional.ofNullable(_userDao).orElseThrow();
    }

    /**
     * setter注入
     * @param _mngrDao in
     * @author 王浩宇
     */
    @Autowired
    public void setMngrDao(MngrDao _mngrDao)
    {
//        this.m_mngrDao = Optional.ofNullable(_mngrDao).orElseThrow();
        this.m_mngrDao = _mngrDao;
    }

    /**
     * 通过User的个人信息，获取在数据库中的匹配结果
     * @param _strUserID in UserID
     * @param _strPassword in User密码
     * @return 标志匹配结果的boolean值
     * @author 王浩宇
     */
    public boolean hasMatchUser(String _strUserID,String _strPassword)
    {
        int nMatchCnt = m_userDao.getMatchCount(_strUserID,_strPassword);
        return nMatchCnt > 0;
    }

    /**
     * 通过UserID获取User对象
     * used for some bad circumstances where customers failed to provide bio info.
     * @param _strUserID in 表示UserID的字符串 userid,primary key in table t_user, which can deduce a unique user.
     * @return User对象
     * @author 王浩宇
     */
    public User findUserByUserID(String _strUserID)
    {
        return m_userDao.findUserByUserID(_strUserID);
    }

    /**
     * 通过人脸信息识别用户
     * @param _imgrecg in 人脸信息
     * @return 用户列表
     */
    public List<User> findUserByRecgBio(String _imgrecg)
    {
        List<String> lstUserID = m_bioDao.certificateUser(_imgrecg);
        List<User> lstUser = new CopyOnWriteArrayList<>();
        lstUserID.stream().map(userid->m_userDao.findUserByUserID(userid)).forEach(lstUser::add);
        return lstUser;
    }

    /**
     * 通过Mngr的个人信息，获取在数据库中的匹配结果
     * @param _strMngrID in MngrID
     * @param _strPassword in Mngr密码
     * @return 标志匹配结果的boolean值
     * @author 王浩宇
     */
    public boolean hasMatchMngr(String _strMngrID,String _strPassword)
    {
        int nMatchCnt = m_mngrDao.getMatchCount(_strMngrID,_strPassword);
        return nMatchCnt > 0;
    }

    /**
     * 更新用户密码
     * @param _user 用户对象
     * @param _strPasswd 新密码
     * @return 逻辑值
     */
    public boolean updateUserPasswd(User _user,String _strPasswd)
    {
        if(checkString(_strPasswd))
        {
            User user = m_userDao.findUserByUserID(_user.getUserID());
            if(user == null || user.isEmpty())
            {
                return false;
            }
            else
            {
                user.setPassword(_strPasswd);
                m_userDao.updateUser(user);
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * 更新用户人脸信息
     * @param _user in 用户对象
     * @param _imgrecg in 人脸信息
     * @return 逻辑值
     */
    public boolean updateUserRecgBio(User _user,List<String> _imgrecg)
    {
        User user = m_userDao.findUserByUserID(_user.getUserID());
        if(user == null || user.isEmpty())
        {
            return false;
        }
        else
        {
            return m_bioDao.saveUser(_imgrecg,_user.getUserID(),ConfigConst.BIO_ACC);
        }
    }

    /**
     * 更新用户ID
     * @param _user in 用户对象
     * @param _strUserID in 新UserID
     * @return 逻辑值
     */
    public boolean updateUserID(User _user,String _strUserID)
    {
        User user = m_userDao.findUserByUserID(_user.getUserID());
        if(user == null ||user.isEmpty())
        {
            return false;
        }
        else
        {
            m_userDao.updateUser(user,_strUserID);
            m_bioDao.updateUserID(user.getUserID(),_strUserID);
            return true;
        }
    }

    /**
     * 通过MngrID获取Mngr对象
     * @param _strMngrID in 表示MngrID的字符串
     * @return Mngr对象
     * @author 王浩宇
     */
    public Mngr findMngrByMngrID(String _strMngrID)
    {
        return m_mngrDao.findMngrByMngrID(_strMngrID);
    }

    /**
     * 更新Mngr信息
     * @param _mngr in Mngr对象
     * @author 王浩宇
     */
    public void updateMngr(Mngr _mngr)
    {
        m_mngrDao.updateMngr(_mngr);
    }

    /**
     * 删除当前Mngr信息
     * @param _mngr in Mngr对象
     * @author 王浩宇
     */
    public void delMngr(Mngr _mngr)
    {
        m_mngrDao.delMngr(_mngr);
    }

    /**
     * 获取一个可用的新的UserID
     * @param _nTryLimit in 在获取失败情况下的最大可能尝试次数。
     * @return UserID
     * @author 王浩宇
     */
    public String createNewUserID(int _nTryLimit)
    {
        String strUserid = null;
        for(int i = 0;i<_nTryLimit;++i)
        {
            long id = (long)(Math.random()*ConfigConst.ID_RANGE);
            strUserid = "SCRM" + id;
            User user = m_userDao.findUserByUserID(strUserid);
            if(user == null ||user.isEmpty())
            {
                return strUserid;
            }
        }
        return null;
    }

    /**
     * 通过密码方式创建新用户，该操作将导致数据库中User记录的更新。
     * @param _strUserName in UserName
     * @param _strUserID in UserID
     * @param _strPassword in User密码
     * @return 创建通告结果的boolean值
     * @author 王浩宇
     */
    public boolean creatNewUserByPasswd(String _strUserName, String _strUserID, String _strPassword)
    {
        if(!checkString(_strUserName) || !checkString(_strUserID) || !checkString(_strPassword))
            return false;
        User newUser = new User();
        newUser.setUserName(_strUserName);
        newUser.setUserID(_strUserID);
        newUser.setPassword(_strPassword);
        m_userDao.insertUser(newUser);
        return true;
    }

    /**
     * 通过人脸识别方式创建新用户，该操作将导致数据库中User记录的更新。
     * @param _strUserID in 用户ID
     * @return 创建成功与否的boolean值
     * @author 王浩宇
     */
    public boolean creatNewUserByRecgBio(String _strUserID,List<String> _imgrecg)
    {
        if(!checkString(_strUserID))
            return false;
        System.out.println("校验完毕，Man服务层执行人脸注册程序。");
        User newUser = new User();
        newUser.setUserName("");
        newUser.setUserID(_strUserID);
        newUser.setPassword("");
        if(m_bioDao.saveUser(_imgrecg,_strUserID,ConfigConst.BIO_ACC))
        {
            System.out.println("人脸库更新完成，数据库插入用户数据");
            m_userDao.insertUser(newUser);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 创建新的Accnt对象
     * @param _strAccntID in AccntID
     * @param _strPassword in 密码
     * @return 创建是否成功的boolean值
     * @author 王浩宇
     */
    public boolean creatNewAccnt(String _strAccntID,String _strPassword)
    {
        if(!checkString(_strAccntID) || !checkString(_strPassword))
            return false;
        Mngr accnt = new Mngr();
        accnt.setMngrID(_strAccntID);
        accnt.setPassword(_strPassword);
        accnt.setMngrType(2);
        m_mngrDao.insertMngr(accnt);
        return true;
    }

    /**
     * 获取所有收银员
     * @date 9.3
     * @return Mngr列表
     */
    public List<Mngr> getMngrAllForAdmin()
    {
        return m_mngrDao.getMngrAll(ConfigConst.ACCNT_ALL_LIMIT);
    }

    /**
     * 判断用户人脸信息是否存在
     * @param _user in 用户对象
     * @return 逻辑值
     */

    public boolean isUserRecgBioExist(User _user)
    {
        return m_bioDao.isUserExist(_user.getUserID());
    }


    /**
     * 用于检查字符串是否合法。
     * @author 王浩宇
     */
    protected boolean checkString(String _strSrc)
    {
        return !(_strSrc == null || _strSrc.trim().equals(""));
    }



}
