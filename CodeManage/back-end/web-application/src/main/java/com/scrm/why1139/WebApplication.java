package com.scrm.why1139;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * 应用于Spring框架的项目启动类
 * @author why
 */
@EnableTransactionManagement
@SpringBootApplication
public class WebApplication extends SpringBootServletInitializer
{
    /**
     * 运行入口
     * @author why
     */
    public static void main(String[] args)
    {
        SpringApplication.run(WebApplication.class, args);

    }

    /**
     * 实施应用程序构造配置
     * @param _application in app
     * @return 配置后的Spring构造器
     * @author why
     */
    protected SpringApplicationBuilder configure(SpringApplicationBuilder _application)
    {
        return _application.sources(WebApplication.class);
    }

}
