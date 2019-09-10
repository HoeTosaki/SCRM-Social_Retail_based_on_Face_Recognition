package com.scrm.why1139.service;

import com.scrm.why1139.dao.MonitorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 面向MonitorDao的Service类。
 * @author 王浩宇
 * @date 9.2
 */
@Service
public class MonitorService {
    @Autowired
    private MonitorDao m_monitorDao;

    /**
     * 执行数据抽取
     */
    public void dataExtract()
    {
        m_monitorDao.dataExtract();
    }

    /**
     * 执行数据转移
     */
    public void dataTransfer()
    {
        m_monitorDao.dataTransfer();
    }

    /**
     * 执行数据建模
     * @param _nCmd
     */
    public void dataModeling(int _nCmd)
    {
        m_monitorDao.dataModeling(_nCmd);
    }

    /**
     * 服务器基本信息监控
     * @param _info out 传出信息
     * @return 逻辑值
     */
    public boolean monitorBaseInfo(List<String> _info)
    {
        return m_monitorDao.serverMonitor(18,_info);
    }

    /**
     * 仪表盘信息
     * @param _info out 传出信息
     * @return 逻辑值
     */
    public boolean monitorMach(List<String> _info)
    {
        return m_monitorDao.serverMonitor(19,_info);
    }

    /**
     * 图表信息
     * @param _info out 传出信息
     * @return 逻辑值
     */
    public boolean monitorFig(List<String> _info)
    {
        List<String> lstInfo1 = new CopyOnWriteArrayList<>();
        List<String> lstInfo2 = new CopyOnWriteArrayList<>();
        boolean ret1 = m_monitorDao.serverMonitor(20,lstInfo1);
        boolean ret2= m_monitorDao.serverMonitor(21,lstInfo2);
        lstInfo1.stream().forEachOrdered(_info::add);
        lstInfo2.stream().forEachOrdered(_info::add);
//        StringBuffer sb = new StringBuffer();
//        lstInfo2.stream().map(str->"why$"+str+"$why").forEach(sb::append);
//        System.out.println(sb.toString());
        return ret1 && ret2;
    }



}
