package com.rbac.hcye_admin.controller.network;

import com.rbac.hcye_admin.entity.SwFirm;
import com.rbac.hcye_admin.entity.SwGateway;
import com.rbac.hcye_admin.entity.SwOidTemp;
import com.rbac.hcye_admin.entity.SwSwitch;
import com.rbac.hcye_admin.jpa.JpaGateway;
import com.rbac.hcye_admin.jpa.JpaSwFirm;
import com.rbac.hcye_admin.jpa.JpaSwOidTemp;
import com.rbac.hcye_admin.jpa.JpaSwSwitch;
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
    private JpaSwFirm jpaSwFirm;
    @Autowired
    private JpaSwOidTemp jpaSwOidTemp;
    @Autowired
    private JpaGateway jpaGateway;
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
    @RequiresPermissions("network:sw:add")
    @GetMapping("/sw/add")
    public String add(Model model){
        List<SwFirm> firmList=jpaSwFirm.findAll();
        model.addAttribute("firms",firmList);
        return "network/add";
    }
    @RequiresPermissions("network:oid:add")
    @GetMapping("/oid/add")
    public String oid_add(Model model){
        List<SwFirm> firmList=jpaSwFirm.findAll();
        model.addAttribute("firms",firmList);
        return "network/oid/oid_add";
    }
    @RequiresPermissions("network:firm:add")
    @GetMapping("/firm/add")
    public String firm_add(){
        return "network/firm/firm_add";
    }

    @RequiresPermissions("network:vlan:add")
    @GetMapping("/gateway/add")
    public String gateway_add(){
        return "network/gateway/gateway_add";
    }
    @RequiresPermissions("network:sw:edit")
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
    @RequiresPermissions("network:oid:edit")
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
    @RequiresPermissions("network:firm:edit")
    @GetMapping("/firm/edit")
    public String firm_edit(int id, Model model){

        SwFirm swFirm=jpaSwFirm.findById(id).get();
        model.addAttribute("firm",swFirm);
        return "network/firm/firm_edit";
    }
    @RequiresPermissions("network:vlan:edit")
    @GetMapping("/gateway/edit")
    public String gateway_edit(int id, Model model){

        SwGateway swGateway=jpaGateway.findById(id).get();
        model.addAttribute("gateway",swGateway);
        return "network/gateway/gateway_edit";
    }
    @RequiresPermissions("network:sw:delete")
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
    @RequiresPermissions("network:firm:delete")
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
    @RequiresPermissions("network:vlan:delete")
    @GetMapping("/gateway/del")
    public String gateway_del(int id,Model model){
        try {
            jpaGateway.deleteById(id);
        }catch (Exception e){
            model.addAttribute("err",e);
            return "/network/error";
        }
        return "redirect:/network/gateway_mgmt";
    }
    @RequiresPermissions("network:oid:delete")
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

    @RequiresPermissions("network:oid:view")
    @GetMapping("/network/oid_mgmt")
    public String oid(Model model){
        List<SwFirm> firms=jpaSwFirm.findAll();
        SwFirm swFirm=new SwFirm();
        swFirm.setFname("全部");
        firms.add(0,swFirm);
        model.addAttribute("firms",firms);
        return "network/oid/oid_temp";
    }
    @RequiresPermissions("network:firm:view")
    @GetMapping("/network/firm_mgmt")
    public String firm(){
        return "network/firm/firm";
    }

    @RequiresPermissions("network:gateway:view")
    @GetMapping("/network/gateway_mgmt")
    public String gateway(){
        return "network/gateway/gateway";
    }

    @RequiresPermissions("network:net_query:view")
    @GetMapping("/network/info_query")
    public String query(){
        return "network/query/query";
    }

    @RequiresPermissions("network:network:doc")
    @GetMapping("/asm/network/doc")
    public String network_doc(){
        return "network/basicDoc";
    }
}
