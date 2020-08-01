package com.rbac.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AddController {
    @RequestMapping("/add")
    public String add(){
        return "add";
    }
}
