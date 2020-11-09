package com.rbac.demo.controller.network;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NetworkController {
    @GetMapping("/sw/sw")
    public String sw(){
        return "network/switch";
    }

    @GetMapping("/sw/add")
    public String add(){
        return "network/add";
    }

    @GetMapping("/sw/edit")
    public String add(int id){



        return "network/edit";
    }

}
