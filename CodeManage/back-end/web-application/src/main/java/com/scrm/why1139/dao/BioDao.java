package com.scrm.why1139.dao;

import com.scrm.why1139.BioReferenceModule.AIBound;
import com.scrm.why1139.BioReferenceModule.BioCertificater;
import com.scrm.why1139.BioReferenceModule.BioSaver;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BioDao {

    /**
     * 根据人脸信息查询匹配的用户ID列表。
     * @param _strBase64 in 用户人脸信息
     * @return 用户ID的list
     * @author why
     */
    public List<String> certificateUser(String _strBase64)
    {
        return BioCertificater.certificateUser(_strBase64);
    }

    /**
     * 保存当前用户的人脸信息。
     * @param _strlstBase64 in 用户人脸信息的列表
     * @param _strUserID in 用户ID
     * @param _dbAcc in 人脸信息的精确度要求
     * @return 存储结果是否达到精确度要求的boolean值
     * @author why
     */
    public boolean saveUser(List<String> _strlstBase64,String _strUserID,double _dbAcc)
    {
        return BioSaver.saveUser(_strlstBase64, _strUserID, _dbAcc);
    }

    /**
     * 删除用户，同时删除其名下的所有存储的人脸信息
     * @param _strUserID in 用户ID
     * @return 删除成功性的boolean值
     * @author why
     */
    public boolean delUser(String _strUserID)
    {
        return BioSaver.delUser(_strUserID);
    }

    public boolean updateUser(List<String> _strlstBase64,String _strUserID,double _dbAcc)
    {
        return BioSaver.saveUser(_strlstBase64, _strUserID, _dbAcc);
    }

    public boolean updateUserID(String _strUserID, String _strNewUserID)
    {
        return true;
    }

    public boolean isUserExist(String _strUserID)
    {
        return true;
    }
}
