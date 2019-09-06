package com.scrm.why1139.service;

import com.scrm.why1139.BioReferenceModule.BioCertificater;
import com.scrm.why1139.dao.MngrDao;
import com.scrm.why1139.dao.UserDao;
import com.scrm.why1139.domain.Mngr;
import com.scrm.why1139.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;
import java.util.Random;

/**
 * 提供人员管理基础的Service类。
 * @author why
 */
@Service
public class ManService
{
    protected UserDao m_userDao;
    protected MngrDao m_mngrDao;

    /**
     * setter注入
     * @param _userDao in
     * @author why
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
     * @author why
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
     * @author why
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
     * @author why
     */
    public User findUserByUserID(String _strUserID)
    {
        return m_userDao.findUserByUserID(_strUserID);
    }

    /**
     * 通过Mngr的个人信息，获取在数据库中的匹配结果
     * @param _strMngrID in MngrID
     * @param _strPassword in Mngr密码
     * @return 标志匹配结果的boolean值
     * @author why
     */
    public boolean hasMatchMngr(String _strMngrID,String _strPassword)
    {
        int nMatchCnt = m_mngrDao.getMatchCount(_strMngrID,_strPassword);
        return nMatchCnt > 0;
    }

    /**
     * 通过MngrID获取Mngr对象
     * @param _strMngrID in 表示MngrID的字符串
     * @return Mngr对象
     * @author why
     */
    public Mngr findMngrByMngrID(String _strMngrID)
    {
        return m_mngrDao.findMngrByMngrID(_strMngrID);
    }

    /**
     * 更新Mngr信息
     * @param _mngr in Mngr对象
     * @author why
     */
    public void updateMngr(Mngr _mngr)
    {
        m_mngrDao.updateMngr(_mngr);
    }

    /**
     * 删除当前Mngr信息
     * @param _mngr in Mngr对象
     * @author why
     */
    public void delMngr(Mngr _mngr)
    {
        m_mngrDao.delMngr(_mngr);
    }

    /**
     * 获取一个可用的新的UserID
     * @param _nTryLimit in 在获取失败情况下的最大可能尝试次数。
     * @return UserID
     * @author why
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
     * @author why
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
     * TODO:目前由于密码和人脸信息的地位不等价，人脸信息的存储被放置在该方法的外侧
     * @param _strUserID in 用户ID
     * @return 创建成功与否的boolean值
     * @author why
     */
    public boolean creatNewUserByRecgBio(String _strUserID)
    {
        if(!checkString(_strUserID))
            return false;
        User newUser = new User();
        newUser.setUserName("");
        newUser.setUserID(_strUserID);
        newUser.setPassword("");
        m_userDao.insertUser(newUser);
        return true;
    }

    /**
     * 根据生物特征信息获取User对象
     * @param _btBioRef in BioRef
     * @return User对象
     * @author why
     */
    public User findUserByBioRef(String _btBioRef)
    {
        //TODO: need impl in future based on Face-Reg modules or some newest tech, ehh, you know.
        List<String> strlstUserID = BioCertificater.certificateUser(_btBioRef);
        if(strlstUserID.size() == 0)
            return new User();
        else
        {
            User userBestFit = m_userDao.findUserByUserID(strlstUserID.get(0));
            return userBestFit;
        }
    }

    /**
     * 创建新的Accnt对象
     * @param _strAccntID in AccntID
     * @param _strPassword in 密码
     * @return 创建是否成功的boolean值
     * @author why
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

    public List<Mngr> getMngrAllForAdmin()
    {
        return m_mngrDao.getMngrAll(ConfigConst.ACCNT_ALL_LIMIT);
    }


    /**
     * 用于检查字符串是否合法。
     * @author why
     */
    protected boolean checkString(String _strSrc)
    {
        return !(_strSrc == null || _strSrc.trim().equals(""));
    }



}
