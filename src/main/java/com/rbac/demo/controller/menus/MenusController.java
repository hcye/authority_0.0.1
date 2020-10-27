package com.rbac.demo.controller.menus;

import com.rbac.demo.entity.Resources;
import com.rbac.demo.jpa.JpaResources;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MenusController {
    @Autowired
    JpaResources jpaResources;
    @RequiresPermissions("asm:menu:view")
    @GetMapping("/menus/menus")
    public String menus(){
        return "menus/menus";
    }
    @RequiresPermissions("asm:menus:add")
    @GetMapping("/menus/add")
    public String add(){
        return "menus/add";
    }


    @GetMapping("/menus/selectUpperMenu")
    public String getLeader(){
        return "menus/layer_select_upperMenu";
    }

    @RequiresPermissions("asm:menus:edit")
    @GetMapping("/menus/edit")
    public String edit(int id, Model model){
        Resources resources =jpaResources.findById(id).get();
        String type=resources.getType();
        List<String> types=new ArrayList<>();
        if(type.equals("按钮")){
            types.add("按钮");
            types.add("菜单");
        }else {
            types.add("菜单");
            types.add("按钮");
        }
        model.addAttribute("types",types);
        model.addAttribute("res",resources);
        return "menus/edit";
    }
}
