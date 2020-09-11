package com.rbac.demo.controller.role;

import com.rbac.demo.entity.Role;
import com.rbac.demo.entity.Role2Resources;
import com.rbac.demo.entity.User2Role;
import com.rbac.demo.jpa.JpaRole;
import com.rbac.demo.jpa.JpaRole2Resources;
import com.rbac.demo.jpa.JpaUser2Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;

@Controller
public class RoleController {
    @Autowired
    private JpaRole jpaRole;
    @Autowired
    private JpaRole2Resources jpaRole2Resources;
    @RequestMapping("/role/add")
    public String toAddPage(){
        return "/role/add";
    }

    @RequestMapping("/role/edit")
    public String edit(int id, Model model){
        Role role=jpaRole.findById(id).get();

        String[] avalible=new String[2];
        if(role.getAvalible()==1){
            avalible[0]="可用";
            avalible[1]="禁用";
        }else {
            avalible[1]="可用";
            avalible[0]="禁用";
        }
        model.addAttribute("role",role);
        model.addAttribute("avalible",avalible);
        return "/role/edit";
    }
    @GetMapping("/role/delete")
    public String delete(int id){
        Role role=jpaRole.findById(id).get();
        Collection<Role2Resources> role2Resources=role.getRole2ResourcesById();
        jpaRole2Resources.deleteAll(role2Resources);
        jpaRole.delete(role);
        return "/role/role";
    }
    @GetMapping("/role/role")
    public String role(){
        return "/role/role";
    }

}
