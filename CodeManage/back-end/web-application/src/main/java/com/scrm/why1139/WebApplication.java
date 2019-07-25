package com.scrm.why1139;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@SpringBootApplication
public class WebApplication extends SpringBootServletInitializer
{
    public static void main(String[] args)
    {
        SpringApplication.run(WebApplication.class, args);

    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder _application)
    {
        return _application.sources(WebApplication.class);
    }

}
