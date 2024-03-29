package com.rbac.hcye_admin.controller.role;

import com.rbac.hcye_admin.entity.Role;
import com.rbac.hcye_admin.entity.Role2Resources;
import com.rbac.hcye_admin.jpa.JpaRole;
import com.rbac.hcye_admin.jpa.JpaRole2Resources;
import com.rbac.hcye_admin.shiro.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class RoleController {
    @Autowired
    private JpaRole jpaRole;
    @Autowired
    private JpaRole2Resources jpaRole2Resources;

    @RequiresPermissions("asm:role:add")
    @GetMapping("/role/add")
    public String toAddPage(){
        return "role/add";
    }

    @RequiresPermissions("asm:role:edit")
    @GetMapping("role/edit")
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
        ShiroUtils.clearCachedAuthorizationInfo(); //清除缓存
        return "role/edit";
    }

    @RequiresPermissions("asm:role:delete")
    @GetMapping("/role/delete")
    public String delete(int id){
        Role role=jpaRole.findById(id).get();
        Collection<Role2Resources> role2Resources=role.getRole2ResourcesById();
        jpaRole2Resources.deleteAll(role2Resources);
        jpaRole.delete(role);
        return "role/role";
    }

    @RequiresPermissions("asm:role:view")
    @GetMapping("/role/role")
    public String role(){
        return "role/role";
    }

}
