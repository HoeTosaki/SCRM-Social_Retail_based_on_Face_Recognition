package com.scrm.why1139.service;

import com.scrm.why1139.dao.MonitorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MonitorService {
    @Autowired
    private MonitorDao m_monitorDao;

    public void dataExtract()
    {
        m_monitorDao.dataExtract();
    }

    public void dataModeling(int _nCmd)
    {
        m_monitorDao.dataModeling(_nCmd);
    }

    public boolean monitorBaseInfo(List<String> _info)
    {
        m_monitorDao.serverMonitor(18,_info);
    }

    public boolean monitorMach(List<String> _info)
    {
        m_monitorDao.serverMonitor(19,_info);
    }

    public boolean monitorFig(List<String> _info)
    {
        List<String> lstInfo1 = new CopyOnWriteArrayList<>();
        List<String> lstInfo2 = new CopyOnWriteArrayList<>();
        m_monitorDao.serverMonitor(20,lstInfo1);
        m_monitorDao.serverMonitor(21,lstInfo2);

    }



    public boolean serverMonitor(int _nCmd, )
    {

        switch (_nCmd) {
            case 18:
            {

                break;
            }
            case 19:
            {

                break;
            }
            case 20:
            {

                break;
            }
            case 21:
            {

                break;
            }
        }
        return m_monitorDao.serverMonitor(_nCmd, _infos);
    }

}
