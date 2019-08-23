package com.scrm.why1139.BioReferenceModule;

import java.util.List;

public class BioSaver
{
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

    public static boolean delUser(String _strUserid)
    {
        return AIBound.delUser(_strUserid);
    }
}
