package com.rbac.hcye_admin.service;
import com.rbac.hcye_admin.entity.Resources;
import com.rbac.hcye_admin.entity.Role;
import com.rbac.hcye_admin.entity.Role2Resources;
import com.rbac.hcye_admin.jpa.JpaRole;
import com.rbac.hcye_admin.jpa.JpaRole2Resources;
import com.rbac.hcye_admin.tool.ConvertStrForSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Repository
public class RoleService {
    @Autowired
    private JpaRole jpaRole;
    @Autowired
    private JpaRole2Resources jpaRole2Resources;
    private final static int pageSize = 8;

    public Page<Role> getRolesPage(String name, String status, String pre, String next, int pageNow) {
        //初始化页面
  /*      if(name.equals("")&&pre.equals("")&&next.equals("")){
            Pageable pageable= PageRequest.of(0,pageSize);
            Page<Role> roles=jpaRole.findRolesByPage(pageable);
            return roles;

        }*/
        //初始化页面
        //搜索
        Byte avalible = 0;
        Pageable pageable;
        if (pageNow != 0) {
            pageNow = pageNow - 1;  //Page 对象下标从0开始,所以从前端返回数据要减一
            if (!pre.equals("")&&pageNow>0) {
                pageNow = pageNow - 1;

            } else if (!next.equals("")) {
                pageNow = pageNow + 1;
            }
        }
        pageable = PageRequest.of(pageNow, pageSize);
        Page<Role> roles;
            if (status.equals("全部")) {
                if (name.equals("")) {
                    roles = jpaRole.findRolesByPage(pageable);
                } else {
                    name=ConvertStrForSearch.getFormatedString(name);
                    roles = jpaRole.findRolesByRnameLike(name, pageable);
                }
            } else {
                if (status.equals("正常")) {
                    avalible = 1;
                } else if (status.equals("禁用")) {
                    avalible = 0;
                }
                if (name.equals("")) {
                    roles = jpaRole.findRolesByAvalible(avalible, pageable);
                } else {
                    name=ConvertStrForSearch.getFormatedString(name);
                    roles = jpaRole.findRolesByRnameLikeAndAvalible(name, avalible, pageable);
                }

            }

            //超级管理员不可管理
            Iterator<Role> roleIterator =roles.iterator();
            while (roleIterator.hasNext()){
                Role role=roleIterator.next();
                if(role.getAuthorityCode().equals("administrator")){
                    roleIterator.remove();
                    break;
                }
            }

            return roles;
        //搜索




    }
    public Role saveRole(String name, String code, String status,String remarks,Role role1){

        if(jpaRole.findRoleByRname(name)!=null||jpaRole.findRoleByAuthorityCode(code)!=null){
            if(role1==null||!role1.getRname().equals(jpaRole.findRoleByRname(name).getRname())||
                    !role1.getAuthorityCode().equals(jpaRole.findRoleByAuthorityCode(code).getAuthorityCode())){
                return null;
            }
        }
        Role role;
        if(role1==null){
            role=new Role();
        }else {
            role=role1;
        }

        role.setRname(name);
        role.setAuthorityCode(code);
        role.setRemarks(remarks);
        role.setCreateTime(new Timestamp(new Date().getTime()));
        if(status.equals("禁用")){
            role.setAvalible((byte) 0);
        }else if(status.equals("可用")){
            role.setAvalible((byte) 1);
        }
        Role savedRole=jpaRole.saveAndFlush(role);
        return role;
    }
    //为超级管理员删除权限
    public  void adminRemoveMenu(Resources resources){
        Role role=jpaRole.findRoleByAuthorityCode("administrator");
        List<Role2Resources>  role2ResourcesList= (List<Role2Resources>) role.getRole2ResourcesById();
        for (Role2Resources role2Resources:role2ResourcesList){
            Resources resources1=role2Resources.getResourcesByMenusId();
            if (resources.getId()==resources1.getId()){
                jpaRole2Resources.delete(role2Resources);
                break;
            }
        }
    }

    //为超级管理员添加新增权限
    public void adminAddMenu(Resources resources){
        Role role=jpaRole.findRoleByAuthorityCode("administrator");
        Role2Resources role2Resources=new Role2Resources();
        role2Resources.setResourcesByMenusId(resources);
        role2Resources.setRoleByRoleId(role);
        jpaRole2Resources.saveAndFlush(role2Resources);
    }
}
