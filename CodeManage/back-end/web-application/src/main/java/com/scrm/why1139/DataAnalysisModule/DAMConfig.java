package com.scrm.why1139.DataAnalysisModule;

import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * 用于执行ETL的基础控制类
 * @author 王浩宇
 * @date 9.5
 */
@Repository
public class DAMConfig
{
    private boolean m_isDBAccess = false;

    /**
     * 启动数据库的访问，并生成CSV数据
     */
    public void enableDBAccess()
    {
        m_isDBAccess = true;
        moveToAnal();
    }

    /**
     * 终止访问
     */
    public void disableDBAccess()
    {
        m_isDBAccess = false;
    }

    /**
     * 查询可访问状态
     * @return 逻辑值
     */
    public boolean isDBAccess()
    {
        return m_isDBAccess;
    }

    /**
     * 执行CSV数据的转移
     */
    private void moveToAnal()
    {
        Process proc = execPy("Carrier.py");
        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String str = null;
        try
        {
            while((str = br.readLine()) != null)
            {
                System.out.println("Str output[0]:"+str);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 执行python脚本
     * @param _strFilename in 文件名
     * @param args 额外参数
     * @return 回执Process对象
     */
    public Process execPy(String _strFilename,String... args)
    {
        File curPathFile = new File("");
        String strCurPath = null;
        Process proc = null;
        try {
            strCurPath = curPathFile.getCanonicalPath();
            String[] strlst = new String[args.length + 3];
            strlst[0] = "python";
            strlst[1] = strCurPath + "/py/" +_strFilename;
            strlst[2] = strCurPath;
            for(int i = 0;i<args.length;++i)
            {
                strlst[i + 3] = args[i];
            }

            proc = Runtime.getRuntime().exec(strlst);

            System.out.println(_strFilename + " is out with cmd:"+ Arrays.toString(strlst));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return proc;
    }

    /**
     * 获取当前路径
     * @return String
     */
    private String getCurPath()
    {
        File curPathFile = new File("");
        String strCurPath = null;
        try {
            strCurPath = curPathFile.getCanonicalPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strCurPath;
    }

    public static void main(String s[])
    {
        DAMConfig dam = new DAMConfig();
        dam.enableDBAccess();

    }

}
