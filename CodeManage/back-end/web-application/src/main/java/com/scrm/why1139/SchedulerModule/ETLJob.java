package com.scrm.why1139.SchedulerModule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

/**
 * @deprecated  用于提供定时ETL的Quartz框架服务类
 * @author 王浩宇
 * @date 9.3
 */
@Service
public class ETLJob implements Job
{
//    @Autowired
//    BatchLauncher batchLauncher;
//    @Autowired
//    BatchLauncher BatchLauncher;

    /**
     * 定时执行函数
     * @param jobExecutionContext in 执行上下文
     * @throws JobExecutionException in 执行异常
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
//        System.out.println("ETL Job has been executed!"+new Date());
//        batchLauncher.testRun();
//        m_batchLauncher.testRun();
    }


}
