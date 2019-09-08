package com.scrm.why1139.DataAnalysisModule;

import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

@Repository
public class DAMConfig
{
    private boolean m_isDBAccess = false;

    public void enableDBAccess()
    {
        m_isDBAccess = true;
        moveToAnal();
    }

    public void disableDBAccess()
    {
        m_isDBAccess = false;
    }

    public boolean isDBAccess()
    {
        return m_isDBAccess;
    }

    public void exitCompul()
    {

    }

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

    public static void main(String s[])
    {
        DAMConfig dam = new DAMConfig();
        dam.enableDBAccess();

    }

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

}
