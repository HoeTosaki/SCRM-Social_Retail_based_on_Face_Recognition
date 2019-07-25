package com.scrm.why1139.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class IndexWeb
{
    @RequestMapping("/")
    public String index()
    {
        return "index";
    }

}
