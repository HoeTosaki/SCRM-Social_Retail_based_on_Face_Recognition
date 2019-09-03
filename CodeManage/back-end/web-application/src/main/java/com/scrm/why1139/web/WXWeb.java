package com.scrm.why1139.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WXWeb
{
    @ResponseBody
    @RequestMapping(
            value = {"/wx"}
    )
    public String wx(HttpServletRequest _req)
    {
        return "why";
    }

}
