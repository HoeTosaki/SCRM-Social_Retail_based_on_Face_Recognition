package com.scrm.why1139.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 管理界面的Controller类。
 * @author why
 */
@RestController
public class AdminWeb
{
    @RequestMapping(
            value = {"/admin"}
    )
    public ModelAndView testAnal()
    {
        return new ModelAndView("Web/admin/html/admin.html");
    }



}
