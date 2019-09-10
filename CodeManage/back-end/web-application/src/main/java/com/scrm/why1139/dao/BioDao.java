package com.scrm.why1139.dao;

import com.scrm.why1139.BioReferenceModule.BioCertificater;
import com.scrm.why1139.BioReferenceModule.BioCertificaterRR;
import com.scrm.why1139.BioReferenceModule.BioSaver;
import com.scrm.why1139.BioReferenceModule.BioSaverRR;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用于持久化Bio信息的Dao类。
 * @author 王浩宇
 * @date 9.6
 */
@Repository
public class BioDao {

    /**
     * 根据人脸信息查询匹配的用户ID列表。
     * @param _strBase64 in 用户人脸信息
     * @return 用户ID的list
     * @author 王浩宇
     * @date 9.6
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
     * @author 王浩宇
     * @date 9.6
     */
    public boolean saveUser(List<String> _strlstBase64,String _strUserID,double _dbAcc)
    {
        return BioSaver.saveUser(_strlstBase64, _strUserID, _dbAcc);
    }

    /**
     * 删除用户，同时删除其名下的所有存储的人脸信息
     * @param _strUserID in 用户ID
     * @return 删除成功性的boolean值
     * @author 王浩宇
     * @date 9.6
     */
    public boolean delUser(String _strUserID)
    {
        return BioSaver.delUser(_strUserID);
    }

    /**
     * 更新用户信息
     * @param _strlstBase64 in base64图片编码
     * @param _strUserID in 用户ID
     * @param _dbAcc 要求图片辨认的精确度
     * @return 逻辑值
     */
    public boolean updateUser(List<String> _strlstBase64,String _strUserID,double _dbAcc)
    {
        return BioSaver.saveUser(_strlstBase64, _strUserID, _dbAcc);
    }

    /**
     * 更新用户ID
     * @param _strUserID in 原来的用户ID
     * @param _strNewUserID in 新的用户ID
     * @return
     */
    public boolean updateUserID(String _strUserID, String _strNewUserID)
    {
        return true;
    }

    /**
     * 用户是否存在
     * @param _strUserID in 用户ID
     * @return 逻辑值
     */
    public boolean isUserExist(String _strUserID)
    {
        return true;
    }
}
