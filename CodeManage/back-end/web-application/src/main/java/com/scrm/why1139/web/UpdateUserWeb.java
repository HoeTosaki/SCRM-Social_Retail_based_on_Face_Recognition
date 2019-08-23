package com.scrm.why1139.web;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

public class UpdateUserWeb
{
    @RequestMapping(
            value = {"/updateRecgBio"}
    )
    public String recgBioCer(HttpServletRequest _req, String imgrecg)
    {
        System.out.println(imgrecg);
        return "undefined";
    }

    @RequestMapping(
            value = {"/updatePasswd"}
    )
    public String updatePasswd(HttpServletRequest _req)
    {
        return "undefined";
    }

    @RequestMapping(
            value = {"/updatePasswdByRecgBio"}
    )
    public String updatePasswdByRecgBio(HttpServletRequest _req)
    {
        return "undefined";
    }

    @RequestMapping(
            value = {"/updateRecgBioByPasswd"}
    )
    public String updateRecgBioByPasswd(HttpServletRequest _req)
    {
        return "undefined";
    }



}
