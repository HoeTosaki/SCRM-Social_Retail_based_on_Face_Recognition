package com.scrm.why1139.DataAnalysisModule.Sched;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class FileSave
{
    public static void cleanCSV(String _filename)
    {
        System.out.println("del file:\t"+_filename);
        File file = new File("anal_data/"+_filename);
        System.out.println("del res:\t" + file.delete());
    }

    public static boolean isCSVExist(String _filename)
    {
        File file = new File("anal_data/"+_filename);
        return file.exists();
    }

    public static boolean appendToCSV(String _filename, List<String> _lstDataRow)
    {
        boolean isSucc = false;
        String ret = "";
        for(String row : _lstDataRow)
            ret += row + "\n";
        System.out.println("写到文件"+_filename+",内容是：\t"+ret);
        BufferedWriter bw = null;
        try
        {
            bw = new BufferedWriter(new FileWriter("anal_data/"+_filename,true));
            bw.write(ret);
            bw.flush();
            isSucc = true;
        }
        catch(Exception e)
        {
            isSucc = false;
        }
        finally
        {
            if(bw != null)
            {
                try
                {
                    bw.close();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return isSucc;
    }
}
