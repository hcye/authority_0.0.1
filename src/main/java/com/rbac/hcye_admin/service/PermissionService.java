package com.rbac.hcye_admin.service;

import com.rbac.hcye_admin.entity.*;
import com.rbac.hcye_admin.jpa.JpaResources;
import com.rbac.hcye_admin.jpa.JpaRole2Resources;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service("permission")
public class PermissionService {

    @Autowired
    JpaRole2Resources jpaRole2Resources;
    @Autowired
    JpaResources jpaResources;
    //前端鉴权  var editflag = [[${@permission.getPermi('asm:role:edit')}]];
    private  final static  String disableBtn="layui-btn-disabled";
    private final static String enableBtn="";
    public String getPermi(String permi){
        String btnClass=SecurityUtils.getSubject().isPermitted(permi) ? enableBtn : disableBtn;
        return btnClass;
    }

    public boolean isPermit(String permi){
        boolean btnClass=SecurityUtils.getSubject().isPermitted(permi) ? true : false;
        return btnClass;
    }

    public boolean addPermissionToUser(Employee employee, Resources resources){
        List<User2Role> user2Roles= (List<User2Role>) employee.getUser2RolesById();
        User2Role user2Role=user2Roles.get(0);
        Role role=user2Role.getRoleByRoleId();
        Role2Resources role2Resources=new Role2Resources();
        role2Resources.setRoleByRoleId(role);
        role2Resources.setResourcesByMenusId(resources);
        jpaRole2Resources.saveAndFlush(role2Resources);
        return true;
    }

    public boolean cleanPermission(Resources resources){
        List<Role2Resources> rrs=jpaRole2Resources.findRole2ResourcesByResourcesByMenusId(resources);
        jpaRole2Resources.deleteAll(rrs);
        jpaResources.delete(resources);
        return true;
    }
}
