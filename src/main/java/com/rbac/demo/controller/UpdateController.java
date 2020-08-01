package com.rbac.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UpdateController {
    @RequestMapping("/update")
    public String update(){
        return "update";
    }
}
