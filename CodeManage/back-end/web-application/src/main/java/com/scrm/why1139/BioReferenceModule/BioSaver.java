package com.scrm.why1139.BioReferenceModule;

import java.util.List;

/**
 * 用户人脸信息的存取的边界类
 * @author why
 */
public class BioSaver
{
    /**
     * 保存当前用户的人脸信息。
     * @param _strlstBase64 in 用户人脸信息的列表
     * @param _strUserid in 用户ID
     * @param _dbAcc in 人脸信息的精确度要求
     * @return 存储结果是否达到精确度要求的boolean值
     * @author why
     */
    public static boolean saveUser(List<String> _strlstBase64,String _strUserid,double _dbAcc)
    {
        double acc = 0;
        int suc = 0;
        for(int i = 0;i<_strlstBase64.size();++i)
        {
            if(AIBound.updateUserFace(_strUserid,_strlstBase64.get(i)))
                ++suc;
        }
        acc = suc / _strlstBase64.size();
        return acc >= _dbAcc;
    }

    /**
     * 删除用户，同时删除其名下的所有存储的人脸信息
     * @param _strUserid in 用户ID
     * @return 删除成功性的boolean值
     * @author why
     */
    public static boolean delUser(String _strUserid)
    {
        return AIBound.delUser(_strUserid);
    }

}
