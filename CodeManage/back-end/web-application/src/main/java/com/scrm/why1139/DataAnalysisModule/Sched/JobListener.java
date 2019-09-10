package com.scrm.why1139.DataAnalysisModule.Sched;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * JOB监听器类
 * @author why
 */
@Component
public class JobListener implements JobExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private long startTime;

    /**
     * Job执行前的处理方法
     * @param jobExecution in jobExecution
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
        log.info("job before " + jobExecution.getJobParameters());
    }

    /**
     * Job执行后的处理方法
     * @param jobExecution in jobExecution
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("JOB STATUS : {}", jobExecution.getStatus());
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("JOB FINISHED");
            threadPoolTaskExecutor.destroy();
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.info("JOB FAILED");
        }
        log.info("Job Cost Time : {}ms" , (System.currentTimeMillis() - startTime));
    }
}