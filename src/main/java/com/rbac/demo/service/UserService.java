package com.rbac.demo.service;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.Menus;
import com.rbac.demo.entity.Resource;
import com.rbac.demo.entity.Role;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.jpa.JpaMenus;
import com.rbac.demo.jpa.JpaRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserService {
    @Autowired
    private JpaEmployee jpaEmployee;
    @Autowired
    private JpaRole jpaRole;
    @Autowired
    private JpaMenus jpaMenus;
    public Set<String> getPermissionStrSet(Employee employee){
        Set<String> strings=new HashSet<>();
        List<Role> roles=jpaEmployee.findRoleByEmployee(employee);
        if(roles==null||roles.size()==0){
            return strings;
        }
        List<Menus> menusList=new ArrayList<>();
        for (Role role:roles){
            List<Menus> menus=jpaRole.findMenusByRole(role);
            menusList.addAll(menus);
        }
        if(menusList.size()==0){
            return strings;
        }
        List<Resource> resources=new ArrayList<>();
        for (Menus menus:menusList){
            List<Resource> resources1=jpaMenus.findMenusByResource(menus);
            resources.addAll(resources1);
        }
        for (Resource resource:resources){
            strings.add(resource.getPermission());
        }
        return strings;
    }
    public Set<String> getRoleSStrSet(Employee employee){
        Set<String> stringSet=new HashSet<>();
        List<Role> roles=jpaEmployee.findRoleByEmployee(employee);
        if(roles==null||roles.size()==0){
            return stringSet;
        }
        for(Role role:roles){
            stringSet.add(role.getRname());
        }
        return stringSet;
    }
}
