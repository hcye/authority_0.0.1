package com.rbac.demo.controller.menus;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenusController {
    @GetMapping("/menus/menus")
    public String menus(){
        return "/menus/menus";
    }
    @GetMapping("/menus/add")
    public String add(){
        return "/menus/add";
    }


    @GetMapping("/menus/selectUpperMenu")
    public String getLeader(){
        return "/menus/layer_select_upperMenu";
    }
}
