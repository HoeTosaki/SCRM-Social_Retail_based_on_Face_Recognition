package com.scrm.why1139.DataAnalysisModule.Sched;

import com.scrm.why1139.DataAnalysisModule.entities.Buy;
import com.scrm.why1139.DataAnalysisModule.entities.Goods;
import com.scrm.why1139.DataAnalysisModule.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
//import javax.persistence.EntityManagerFactory;

/**
 * Created by EalenXie on 2018/9/10 14:50.
 * :@EnableBatchProcessing提供用于构建批处理作业的基本配置
 */
@Configuration
@EnableBatchProcessing
public class DataBatchConfiguration {
    private static final Logger log = LoggerFactory.getLogger(DataBatchConfiguration.class);

    @Resource
    private JobBuilderFactory jobBuilderFactory;    //用于构建JOB

    @Resource
    private StepBuilderFactory stepBuilderFactory;  //用于构建Step

    @Resource
    private EntityManagerFactory emf;           //注入实例化Factory 访问数据

    @Resource
    private JobListener jobListener;            //简单的JOB listener

    /**
     * 一个简单基础的Job通常由一个或者多个Step组成
     */
    @Bean
    public Job dataHandleJob() {
        return jobBuilderFactory.get("dataHandleJob").
                incrementer(new RunIdIncrementer()).
                start(handleUserDataStep()).//start是JOB执行的第一个step
                next(handleGoodsDataStep()).
                next(handleBuyDataStep()).
//                ...
        listener(jobListener).      //设置了一个简单JobListener
                build();
    }

    /**
     * 一个简单基础的Step主要分为三个部分
     * ItemReader : 用于读取数据
     * ItemProcessor : 用于处理数据
     * ItemWriter : 用于写数据
     */
    @Bean
    public Step handleUserDataStep() {
        return stepBuilderFactory.get("getUser").
                <User, User>chunk(100).        // <输入,输出> 。chunk通俗的讲类似于SQL的commit; 这里表示处理(processor)100条后写入(writer)一次。
                faultTolerant().retryLimit(3).retry(Exception.class).skipLimit(100).skip(Exception.class). //捕捉到异常就重试,重试100次还是异常,JOB就停止并标志失败
                reader(getUserDataReader()).         //指定ItemReader
                processor(getUserDataProcessor()).   //指定ItemProcessor
                writer(getUserDataWriter()).         //指定ItemWriter
                build();
    }

    @Bean
    public ItemReader<? extends User> getUserDataReader() {
        //读取数据,这里可以用JPA,JDBC,JMS 等方式 读入数据
        JpaPagingItemReader<User> reader = new JpaPagingItemReader<>();
        //这里选择JPA方式读数据 一个简单的 native SQL
        String sqlQuery = "SELECT * FROM t_user";
        try {
            JpaNativeQueryProvider<User> queryProvider = new JpaNativeQueryProvider<>();
            queryProvider.setSqlQuery(sqlQuery);
            queryProvider.setEntityClass(User.class);
            queryProvider.afterPropertiesSet();
            reader.setEntityManagerFactory(emf);
            reader.setPageSize(3);
            reader.setQueryProvider(queryProvider);
            reader.afterPropertiesSet();
            //所有ItemReader和ItemWriter实现都会在ExecutionContext提交之前将其当前状态存储在其中,如果不希望这样做,可以设置setSaveState(false)
            reader.setSaveState(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reader;
    }

    @Bean
    public ItemProcessor<User, User> getUserDataProcessor() {
        return new ItemProcessor<User, User>() {
            @Override
            public User process(User user) throws Exception {
                log.info("processor data : " + user.toString());  //模拟  假装处理数据,这里处理就是打印一下
                return user;
            }
        };
//        lambda也可以写为:
//        return access -> {
//            log.info("processor data : " + access.toString());
//            return access;
//        };
    }

    @Bean
    public ItemWriter<User> getUserDataWriter() {
        return list -> {
            List<String> ret = new CopyOnWriteArrayList<>();
            list.parallelStream().map(user->user.toCSV()).forEach(ret::add);
//            ret.add(0,"user_id");
            FileSave.appendToCSV("t_user.csv",ret);
            for (User user : list) {
                log.info("write data : " + user); //模拟 假装写数据 ,这里写真正写入数据的逻辑
            }
        };
    }

    /**
     * 一个简单基础的Step主要分为三个部分
     * ItemReader : 用于读取数据
     * ItemProcessor : 用于处理数据
     * ItemWriter : 用于写数据
     */
    @Bean
    public Step handleGoodsDataStep() {
        return stepBuilderFactory.get("getGoods").
                <Goods, Goods>chunk(100).        // <输入,输出> 。chunk通俗的讲类似于SQL的commit; 这里表示处理(processor)100条后写入(writer)一次。
                faultTolerant().retryLimit(3).retry(Exception.class).skipLimit(100).skip(Exception.class). //捕捉到异常就重试,重试100次还是异常,JOB就停止并标志失败
                reader(getGoodsDataReader()).         //指定ItemReader
                processor(getGoodsDataProcessor()).   //指定ItemProcessor
                writer(getGoodsDataWriter()).         //指定ItemWriter
                build();
    }

    @Bean
    public ItemReader<? extends Goods> getGoodsDataReader() {
        //读取数据,这里可以用JPA,JDBC,JMS 等方式 读入数据
        JpaPagingItemReader<Goods> reader = new JpaPagingItemReader<>();
        //这里选择JPA方式读数据 一个简单的 native SQL
        String sqlQuery = "SELECT * FROM t_goods";
        try {
            JpaNativeQueryProvider<Goods> queryProvider = new JpaNativeQueryProvider<>();
            queryProvider.setSqlQuery(sqlQuery);
            queryProvider.setEntityClass(Goods.class);
            queryProvider.afterPropertiesSet();
            reader.setEntityManagerFactory(emf);
            reader.setPageSize(3);
            reader.setQueryProvider(queryProvider);
            reader.afterPropertiesSet();
            //所有ItemReader和ItemWriter实现都会在ExecutionContext提交之前将其当前状态存储在其中,如果不希望这样做,可以设置setSaveState(false)
            reader.setSaveState(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reader;
    }

    @Bean
    public ItemProcessor<Goods, Goods> getGoodsDataProcessor() {
        return new ItemProcessor<Goods, Goods>() {
            @Override
            public Goods process(Goods gds) throws Exception {
                log.info("processor data : " + gds.toString());  //模拟  假装处理数据,这里处理就是打印一下
                return gds;
            }
        };
//        lambda也可以写为:
//        return access -> {
//            log.info("processor data : " + access.toString());
//            return access;
//        };
    }

    @Bean
    public ItemWriter<Goods> getGoodsDataWriter() {
        return list -> {
            List<String> ret = new CopyOnWriteArrayList<>();
            list.parallelStream().map(gds->gds.toCSV()).forEach(ret::add);
//            ret.add(0,"goods_id,goods_type,goods_price,goods_cnt");
            FileSave.appendToCSV("t_goods.csv",ret);
            for (Goods gds : list) {
                log.info("write data : " + gds); //模拟 假装写数据 ,这里写真正写入数据的逻辑
            }
        };
    }

    /**
     * 一个简单基础的Step主要分为三个部分
     * ItemReader : 用于读取数据
     * ItemProcessor : 用于处理数据
     * ItemWriter : 用于写数据
     */
    @Bean
    public Step handleBuyDataStep() {
        return stepBuilderFactory.get("getBuy").
                <Buy, Buy>chunk(100).        // <输入,输出> 。chunk通俗的讲类似于SQL的commit; 这里表示处理(processor)100条后写入(writer)一次。
                faultTolerant().retryLimit(3).retry(Exception.class).skipLimit(100).skip(Exception.class). //捕捉到异常就重试,重试100次还是异常,JOB就停止并标志失败
                reader(getBuyDataReader()).         //指定ItemReader
                processor(getBuyDataProcessor()).   //指定ItemProcessor
                writer(getBuyDataWriter()).         //指定ItemWriter
                build();
    }

    @Bean
    public ItemReader<? extends Buy> getBuyDataReader() {
        //读取数据,这里可以用JPA,JDBC,JMS 等方式 读入数据
        JpaPagingItemReader<Buy> reader = new JpaPagingItemReader<>();
        //这里选择JPA方式读数据 一个简单的 native SQL
        String sqlQuery = "SELECT * FROM t_buy";
        try {
            JpaNativeQueryProvider<Buy> queryProvider = new JpaNativeQueryProvider<>();
            queryProvider.setSqlQuery(sqlQuery);
            queryProvider.setEntityClass(Buy.class);
            queryProvider.afterPropertiesSet();
            reader.setEntityManagerFactory(emf);
            reader.setPageSize(3);
            reader.setQueryProvider(queryProvider);
            reader.afterPropertiesSet();
            //所有ItemReader和ItemWriter实现都会在ExecutionContext提交之前将其当前状态存储在其中,如果不希望这样做,可以设置setSaveState(false)
            reader.setSaveState(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reader;
    }

    @Bean
    public ItemProcessor<Buy, Buy> getBuyDataProcessor() {
        return new ItemProcessor<Buy, Buy>() {
            @Override
            public Buy process(Buy buy) throws Exception {
                log.info("processor data : " + buy.toString());  //模拟  假装处理数据,这里处理就是打印一下
                return buy;
            }
        };
//        lambda也可以写为:
//        return access -> {
//            log.info("processor data : " + access.toString());
//            return access;
//        };
    }

    @Bean
    public ItemWriter<Buy> getBuyDataWriter() {
        return list -> {
            List<String> ret = new CopyOnWriteArrayList<>();
            list.parallelStream().map(buy->buy.toCSV()).forEach(ret::add);
//            ret.add(0,"buy_id,user_id,mngr_id,goods_id,buy_cnt,buy_date");
            FileSave.appendToCSV("t_buy.csv",ret);
            for (Buy buy : list) {
                log.info("write data : " + buy); //模拟 假装写数据 ,这里写真正写入数据的逻辑
            }
        };
    }

//    public static void testRun()
//    {
//        System.out.println("batch start");
//
//        try
//        {
//            m_jobLch.run(m_dbc.dataHandleJob(),new JobParameters());
//            m_dbc.dataHandleJob();
//        }
//        catch(Exception e)
//        {
//            System.out.println("job failed.");
//            e.printStackTrace();
//        }
//
//    }


}