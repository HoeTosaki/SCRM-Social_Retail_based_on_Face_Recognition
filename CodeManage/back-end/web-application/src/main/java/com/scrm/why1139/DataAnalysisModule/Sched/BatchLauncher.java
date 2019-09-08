package com.scrm.why1139.DataAnalysisModule.Sched;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
//@EnableScheduling
@Configuration
public class BatchLauncher
{
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    Job dataHandleJob;

//    @Scheduled(cron = "*/5 * *  * * * ")
    public void testRun()
    {
        System.out.println("batch start");
        try
        {
            FileSave.cleanCSV("t_user.csv");
            FileSave.cleanCSV("t_goods.csv");
            FileSave.cleanCSV("t_buy.csv");
//            JobParameters jobParameters = new JobParametersBuilder()
//                    .addDate("date", new Date())
//                    .toJobParameters();
//            jobLauncher.run(dataHandleJob, jobParameters);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
