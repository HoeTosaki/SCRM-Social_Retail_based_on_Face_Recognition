package com.scrm.why1139.BioReferenceModule;

import java.util.List;

/**
 * @deprecated  使用百度API的用户人脸信息识别的边界类，现已被自行编写的人脸识别模块取代
 * @author why
 * @date 9.1
 */
public class BioCertificaterRR
{
    /**
     * 根据人脸信息查询匹配的用户ID列表。
     * @param _strBase64 in 用户人脸信息
     * @return 用户ID的list
     * @author why
     */
    public static List<String> certificateUser(String _strBase64)
    {
        return AIBound.FaceQuery(_strBase64);
    }
}
