package com.scrm.why1139.web;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 管理并更新用户信息的Controller类
 * @author why
 */
public class UpdateUserWeb
{
    /**
     * 映射到updateRecgBio的Controller方法
     * @return 请求响应String
     * @author why
     */
    @RequestMapping(
            value = {"/updateRecgBio"}
    )
    public String recgBioCer(HttpServletRequest _req, String imgrecg)
    {
        System.out.println(imgrecg);
        return "undefined";
    }

    /**
     * 映射到updateRecgBio的Controller方法
     * @return 请求响应String
     * @author why
     */
    @RequestMapping(
            value = {"/updatePasswd"}
    )
    public String updatePasswd(HttpServletRequest _req)
    {
        return "undefined";
    }

    /**
     * 映射到updatePasswdByRecgBio的Controller方法
     * @return 请求响应String
     * @author why
     */
    @RequestMapping(
            value = {"/updatePasswdByRecgBio"}
    )
    public String updatePasswdByRecgBio(HttpServletRequest _req)
    {
        return "undefined";
    }

    /**
     * 映射到updateRecgBioByPasswd的Controller方法
     * @return 请求响应String
     * @author why
     */
    @RequestMapping(
            value = {"/updateRecgBioByPasswd"}
    )
    public String updateRecgBioByPasswd(HttpServletRequest _req)
    {
        return "undefined";
    }



}
