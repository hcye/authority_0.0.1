package com.rbac.demo.controller.network;

import com.rbac.demo.entity.SwFirm;
import com.rbac.demo.entity.SwOidTemp;
import com.rbac.demo.entity.SwSwitch;
import com.rbac.demo.jpa.JpaSwFirm;
import com.rbac.demo.jpa.JpaSwOidTemp;
import com.rbac.demo.jpa.JpaSwSwitch;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Serializable;
import java.util.List;

@Controller
public class NetworkController {
    @Autowired
    private JpaSwSwitch jpaSwSwitch;
    @Autowired
    private JpaSwFirm jpaSwFirm;
    @Autowired
    private JpaSwOidTemp jpaSwOidTemp;
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

    @GetMapping("/oid/add")
    public String oid_add(Model model){
        List<SwFirm> firmList=jpaSwFirm.findAll();
        model.addAttribute("firms",firmList);
        return "network/oid/oid_add";
    }
    @GetMapping("/firm/add")
    public String firm_add(){
        return "network/firm/firm_add";
    }

    @GetMapping("/sw/edit")
    public String edit(int id, Model model){

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

    @GetMapping("/oid/edit")
    public String oid_edit(int id, Model model){

        SwOidTemp swOidTemp=jpaSwOidTemp.findById(id).get();
        List<SwFirm> firmList=jpaSwFirm.findAll();
        SwFirm cuFirm=swOidTemp.getSwFirmBySwFirm();
        for (SwFirm swFirm:firmList){
            if (swFirm.getFname()==cuFirm.getFname()){
                firmList.remove(swFirm);
                break;
            }
        }
        firmList.add(0,cuFirm);
        model.addAttribute("firms",firmList);
        model.addAttribute("oid",swOidTemp);
        return "network/oid/oid_edit";
    }
    @GetMapping("/firm/edit")
    public String firm_edit(int id, Model model){

        SwFirm swFirm=jpaSwFirm.findById(id).get();
        model.addAttribute("firm",swFirm);
        return "network/firm/firm_edit";
    }

    @GetMapping("/sw/del")
    public String del(int id,Model model){
        try {
            jpaSwSwitch.deleteById(id);
        }catch (Exception e){
            model.addAttribute("err",e);
            return "/network/error";
        }
        return "redirect:/asm/network";
    }
    @GetMapping("/firm/del")
    public String firm_del(int id,Model model){
        try {
            jpaSwFirm.deleteById(id);
        }catch (Exception e){
            model.addAttribute("err",e);
            return "/network/error";
        }
        return "redirect:/network/firm_mgmt";
    }

    @GetMapping("/oid/del")
    public String oid_del(int id,Model model){
        try{
            jpaSwOidTemp.deleteById(id);
        }catch (Exception e){
            model.addAttribute("err",e);
            return "/network/error";
        }

        return "redirect:/network/oid_mgmt";
    }

    @RequiresPermissions("asm:oid:view")
    @GetMapping("/network/oid_mgmt")
    public String oid(Model model){
        List<SwFirm> firms=jpaSwFirm.findAll();
        SwFirm swFirm=new SwFirm();
        swFirm.setFname("全部");
        firms.add(0,swFirm);
        model.addAttribute("firms",firms);
        return "network/oid/oid_temp";
    }
    @RequiresPermissions("asm:firm:view")
    @GetMapping("/network/firm_mgmt")
    public String firm(){
        return "network/firm/firm";
    }
}
