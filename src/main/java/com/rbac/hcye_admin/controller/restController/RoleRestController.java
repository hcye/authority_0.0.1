package com.rbac.hcye_admin.controller.restController;

import com.rbac.hcye_admin.entity.*;
import com.rbac.hcye_admin.jpa.JpaResources;
import com.rbac.hcye_admin.jpa.JpaRole;
import com.rbac.hcye_admin.jpa.JpaRole2Resources;
import com.rbac.hcye_admin.service.Chinese2Eng;
import com.rbac.hcye_admin.service.PermissionService;
import com.rbac.hcye_admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class RoleRestController {
    @Autowired
    private JpaRole2Resources jpaRole2Resources;
    @Autowired
    private JpaRole jpaRole;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private JpaResources jpaResources;
    @Autowired
    private RoleService roleService;
    @PostMapping("/role/getRoles")
    public Page<Role> getRoles(String name, String status, String pre, String next, int pageNow){
        Page<Role> page =roleService.getRolesPage(name,status,pre,next,pageNow);
        return page;

    }
    @PostMapping("/role/addRole")
    public Map<String,String> add(String name, String status, String powers,String remarks){
        Map<String,String > map=new HashMap<>();
        Random random = new Random(100);
        int rand=random.nextInt(1000);
        String py=Chinese2Eng.convertHanzi2Pinyin(name,false);
        String code="role:"+ py+":"+rand;
        Role savedRole=roleService.saveRole(name, code, status, remarks,null);
        if(savedRole==null){
            map.put("error","关键字重复");
            return map;
        }
        //分解获得资源对象id数组
        String[] powersCollection=powers.split("-");
        for (String pow:powersCollection){
            if(pow.length()>0){
                Resources resources =jpaResources.findById(Integer.parseInt(pow)).get();
                Role2Resources role2Resources=new Role2Resources();  //角色资源关联对象
                role2Resources.setRoleByRoleId(savedRole);
                role2Resources.setResourcesByMenusId(resources);
                jpaRole2Resources.saveAndFlush(role2Resources);
            }
        }
        map.put("ok","ok");
        return map;
    }
    @PostMapping("/role/editRole")
    public Map<String,String> edit(int id,String name, String status, String powers,String remarks){
        Map<String,String > map=new HashMap<>();
        Role role=jpaRole.findById(id).get();
        String code = role.getAuthorityCode();
        Role savedRole=roleService.saveRole(name, code, status, remarks,role);
        if(savedRole==null){
            map.put("error","关键字重复");
            return map;
        }
        //分解获得资源对象id数组
        String[] powersCollection=powers.split("-");
        //先清空权限，再更新进去
        List<Role2Resources> role2ResourcesList =jpaRole2Resources.findRole2ResourcesByRoleByRoleId(role);
        jpaRole2Resources.deleteAll(role2ResourcesList);
        for (String pow:powersCollection){
            if(pow.length()>0){
                Resources resources =jpaResources.findById(Integer.parseInt(pow)).get();
                Role2Resources role2Resources=new Role2Resources();  //角色资源关联对象
                role2Resources.setRoleByRoleId(savedRole);
                role2Resources.setResourcesByMenusId(resources);
                jpaRole2Resources.saveAndFlush(role2Resources);
            }
        }
        map.put("ok","ok");
        return map;
    }
    @PostMapping("/role/validForDelete")
    public Map<String,String> valid(int id){
        Map<String,String> map=new HashMap<>();
        Role role=jpaRole.findById(id).get();
        List<User2Role> user2RoleList= (List<User2Role>) role.getUser2RolesById();
        if(user2RoleList.size()>0){
            map.put("error","角色有关联用户");
            return map;
        }else {
            map.put("ok","ok");
            return map;
        }

    }


}
