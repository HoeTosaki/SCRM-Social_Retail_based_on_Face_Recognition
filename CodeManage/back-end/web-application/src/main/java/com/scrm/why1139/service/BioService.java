package com.scrm.why1139.service;

import com.scrm.why1139.dao.BioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;

@Service
public class BioService {
    @Autowired
    private BioDao m_bioDao;

    private static boolean generateImage(String imgStr,String _strID)
    {
        // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        else
            System.out.println(imgStr);
//        BASE64Decoder decoder = new BASE64Decoder();
        try {
// Base64解码
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] b = decoder.decode(imgStr);
//            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
// 生成jpeg图片
            String imgFilePath = _strID+".png";// 新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将Base64格式存储到图片中
     * @deprecated 当前版本无效。
     * @param _strSrcBase64 in Base64格式的用户人脸信息
     * @return 存储是否成功的boolean值
     * @author why
     */
    private static boolean genBase64(String _strSrcBase64)
    {
        String name = "why";
        if(!generateImage(_strSrcBase64,"why"))
            return false;
        ByteArrayOutputStream baos = null;
        try
        {
            baos = new ByteArrayOutputStream();
            BufferedInputStream bos = new BufferedInputStream(new FileInputStream("why.png"));
            int b = bos.read();
            while(b != -1)
            {
                baos.write(b);
                b = bos.read();
            }
//            ret = baos.toByteArray();
        } catch (IOException e)
        { }
        finally
        {
            try
            {
                if(baos!=null)
                    baos.close();
            }
            catch (IOException e)
            { }
        }
        return false;
    }
}
