package com.rbac.demo.controller.network;

import com.rbac.demo.entity.SwFirm;
import com.rbac.demo.entity.SwSwitch;
import com.rbac.demo.jpa.JpaSwFirm;
import com.rbac.demo.jpa.JpaSwSwitch;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NetworkController {
    @Autowired
    private JpaSwSwitch jpaSwSwitch;
    @Autowired
    JpaSwFirm jpaSwFirm;
    @RequiresPermissions("asm:network:view")
    @GetMapping("/asm/network")
    public String sw(Model model){
        List<SwFirm> firms=jpaSwFirm.findAll();
        SwFirm swFirm=new SwFirm();
        swFirm.setFname("全部");
        firms.add(0,swFirm);
        model.addAttribute("firms",firms);
        return "network/switch";
    }
    @GetMapping("/sw/add")
    public String add(Model model){
        List<SwFirm> firmList=jpaSwFirm.findAll();
        model.addAttribute("firms",firmList);
        return "network/add";
    }

    @GetMapping("/sw/edit")
    public String add(int id, Model model){

       SwSwitch swSwitch=jpaSwSwitch.findById(id).get();
       List<SwFirm> firmList=jpaSwFirm.findAll();
        SwFirm cuFirm=swSwitch.getSwFirmByFirm();
       for (SwFirm swFirm:firmList){
           if (swFirm.getFname()==cuFirm.getFname()){
               firmList.remove(swFirm);
               break;
           }
       }
       firmList.add(0,cuFirm);
       model.addAttribute("firms",firmList);
       model.addAttribute("sw",swSwitch);
       return "network/edit";
    }
    @GetMapping("/sw/del")
    public String del(int id){
        jpaSwSwitch.deleteById(id);
        return "redirect:/asm/network";
    }
}
