package com.rbac.demo.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@Controller
public class CommonController {
    @GetMapping("/common/{uri}")
    public String getPage(@PathVariable("uri") String pageUri){

        return pageUri;
    }
}
