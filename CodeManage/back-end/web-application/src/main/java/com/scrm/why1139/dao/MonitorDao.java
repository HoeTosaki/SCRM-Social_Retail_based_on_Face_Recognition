package com.scrm.why1139.dao;

import com.scrm.why1139.DataAnalysisModule.DAMConfig;
import com.scrm.why1139.DataAnalysisModule.DataProc.DataProcessor;
import com.scrm.why1139.DataAnalysisModule.Rcmd.Rcmder;
import com.scrm.why1139.DataAnalysisModule.Sched.BatchLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 用于持久化Monitor信息的Dao类。
 * @author 王浩宇
 * @date 8.29
 */
@Repository
public class MonitorDao
{
    @Autowired
    private DataProcessor m_dataProcessor;
    @Autowired
    private BatchLauncher m_batchLauncher;
    @Autowired
    private DAMConfig m_damConfig;
    @Autowired
    private Rcmder m_rcmder;

    /**
     * 执行数据抽取
     */
    public void dataExtract()
    {
        System.out.println("do data extract...");
        m_batchLauncher.testRun();
    }

    /**
     * 执行数据转移
     */
    public void dataTransfer()
    {
        System.out.println("do data transfer...");
        m_damConfig.enableDBAccess();
    }

    /**
     * 执行数据建模
     * @param _nCmd
     */
    public void dataModeling(int _nCmd)
    {
        switch (_nCmd)
        {
            case 0://用户建模
            {
                System.out.println("do data modeling for user...");
                m_rcmder.doModelingUser();
                break;
            }
            case 1://商品建模
            {
                System.out.println("do data modeling for goods...");
                m_rcmder.doModelingGds();
                break;
            }
        }
    }

    /**
     * 执行系统状态检测
     * @param _nCmd in 指令
     * @param _info out 信息列表
     * @return 逻辑值
     */
    public boolean serverMonitor(int _nCmd, List<String> _info)
    {
//        switch (_nCmd)
//        {
//            case 18:
//            {
//                _info.add("2019-09-10");
//                _info.add("1");
//                _info.add("2");
//                _info.add("3");
//                _info.add("4");
//                break;
//            }
//            case 19:
//            {
//                _info.add("5");
//                _info.add("55");
//                _info.add("80");
//                break;
//            }
//            case 20:
//            {
//                _info.add("5");
//                _info.add("100");
//                _info.add("800");
//                break;
//            }
//            case 21:
//            {
//                _info.add("5");
//                _info.add("100");
//                break;
//            }
//        }
        return m_dataProcessor.serverMonitor(_nCmd, _info);
    }


}
