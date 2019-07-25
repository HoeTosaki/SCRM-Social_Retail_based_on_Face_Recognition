package com.scrm.why1139.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;


@RestController
public class IndexWeb
{
    @RequestMapping(
            value = {"/","/index","/index.html"}
    )
    public ModelAndView index()
    {
        return new ModelAndView("Web/html/index.html");
    }

    @RequestMapping(
            value = {"/login"}
    )
    public ModelAndView login()
    {
        return new ModelAndView("Web/html/login.html");
    }

    @RequestMapping(
            value = {"/rcmd"}
    )
    public ModelAndView rcmd()
    {
        return new ModelAndView("Web/html/rcmd.html");
    }

    @RequestMapping(
            value = {"/accnt"}
    )
    public ModelAndView accnt()
    {
        return new ModelAndView("Web/html/accnt.html");
    }


}
