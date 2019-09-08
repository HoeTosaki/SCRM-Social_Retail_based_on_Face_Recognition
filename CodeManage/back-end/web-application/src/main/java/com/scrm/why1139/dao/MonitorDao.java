package com.scrm.why1139.dao;

import com.scrm.why1139.DataAnalysisModule.DataProc.DataProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Repository
public class MonitorDao
{
    @Autowired
    private DataProcessor m_dataProcessor;

    public void dataExtract()
    {
        System.out.println("do data extract...");
    }

    public void dataModeling(int _nCmd)
    {
        switch (_nCmd)
        {
            case 0://用户建模
            {
                System.out.println("do data modeling for user...");
                break;
            }
            case 1://商品建模
            {
                System.out.println("do data modeling for goods...");
                break;
            }

        }
    }

    public boolean serverMonitor(int _nCmd, List<String> _info)
    {
        switch (_nCmd)
        {
            case 18:
            {
                _info.add("2019-09-10");
                _info.add("1");
                _info.add("2");
                _info.add("3");
                _info.add("4");
                break;
            }
            case 19:
            {
                _info.add("5");
                _info.add("55");
                _info.add("80");
                break;
            }
            case 20:
            {
                _info.add("5");
                _info.add("100");
                _info.add("800");
                break;
            }
            case 21:
            {
                _info.add("5");
                _info.add("100");
                break;
            }
        }
        return m_dataProcessor.serverMonitor(_nCmd, _info)
    }


}
