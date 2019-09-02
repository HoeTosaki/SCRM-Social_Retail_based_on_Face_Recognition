package com.scrm.why1139.SchedulerModule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

@Service
public class ETLJob implements Job
{
//    @Autowired
//    BatchLauncher batchLauncher;
//    @Autowired
//    BatchLauncher BatchLauncher;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
//        System.out.println("ETL Job has been executed!"+new Date());
//        batchLauncher.testRun();
//        m_batchLauncher.testRun();
    }


}
