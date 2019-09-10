package com.scrm.why1139.SchedulerModule;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SchedManager
{
    private static Scheduler s_sched;
    private static Map<String, JobDetail> m_mpJob = new ConcurrentHashMap<>();
    private static Map<String, SimpleTrigger> m_mpTrig = new ConcurrentHashMap<>();

    public static boolean startETLJob()
    {
        initBaseService();
        if(m_mpJob.keySet().contains("ETLJob"))
            return true;
        JobDetail buildETLJob = JobBuilder.newJob(ETLJob.class).withIdentity("ETLJob",
                "grp_F").build();
        SimpleTrigger trigETLJob = TriggerBuilder.newTrigger().withIdentity("trig_ETLJob",
                "grp_F").startNow().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5)).build();
        try
        {
            s_sched.scheduleJob(buildETLJob,trigETLJob);
            s_sched.start();
        }
        catch(Exception e)
        {
            System.out.println("ETLJob start false.");
            m_mpJob.remove("ETLJob");
            m_mpTrig.remove("trig_ETLJob");
            return false;
        }
        return true;
    }

    private static boolean initBaseService()
    {
        if(s_sched == null)
        {
            try
            {
                s_sched = StdSchedulerFactory.getDefaultScheduler();
            }
            catch(Exception e)
            {
                System.out.println("Scheduler start false.");
                s_sched = null;
                return false;
            }
            return true;
        }
        return true;
    }

    public static void main(String s[])
    {
        SchedManager.startETLJob();
    }
}
