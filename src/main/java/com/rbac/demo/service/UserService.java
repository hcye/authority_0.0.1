package com.rbac.demo.service;

import com.rbac.demo.entity.*;
import com.rbac.demo.jpa.*;
import com.rbac.demo.tool.ConvertStrForSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserService {
    private static final int pageSize=8;
    @Autowired
    private JpaEmployee jpaEmployee;
    @Autowired
    private JpaGroup jpaGroup;
    @Autowired
    private JpaRole jpaRole;
    @Autowired
    private UserService userService;
    @Autowired
    private JpaAdinfo jpaAdinfo;
    public UserService(){

    }
    public Set<String> getPermissionStrSet(Employee employee){
        Set<String> strings=new HashSet<>();
        List<Role> roles=jpaEmployee.findRoleByEmployee(employee);
        if(roles==null||roles.size()==0){
            return strings;
        }
        List<Resources> resourceList=new ArrayList<>();

        for (Role role:roles){
            //角色停用后无法
            if(role.getAvalible()!=0&&role.getDeleteFlag()!=1){
                resourceList.addAll(jpaRole.findResourcesByRole(role));
            }
        }

        for (Resources resources:resourceList){
            strings.add(resources.getPermission());
        }
        return strings;
    }
    public Set<String> getRoleSStrSet(Employee employee){
        Set<String> stringSet=new HashSet<>();
        List<Role> roles=jpaEmployee.findRoleByEmployee(employee);
        for(Role role:roles){
            if(role.getAvalible()!=0&&role.getDeleteFlag()!=1){
                stringSet.add(role.getRname());
            }
        }
        return stringSet;
    }
    //根据部门id获得部门的用户
    public Page<Employee> getPage(String depid,String pageInputStr,String turnFlag,String keyWord,String refreshFlag)  {
        int pageInput;
        if(pageInputStr.equals("")){
            pageInput=Integer.parseInt("1");
        }else {
            pageInput=Integer.parseInt(pageInputStr);
        }

        pageInput=pageInput-1;   //pageable 的页码序号从0开始
        Page<Employee> employees = null;
        Pageable pageable=PageRequest.of(pageInput,pageSize);


        /**
         *
         * 初始化页面
         *
         * */
        if(turnFlag.equals("")&&keyWord.equals("")&&refreshFlag.equals("")&&depid.equals("")){
            pageable=PageRequest.of(0,pageSize);
            List<SysGroup> groups=jpaGroup.findAllExcludDeleted();
            SysGroup group = groups.get(2);
            employees=jpaEmployee.findEmployeesBySysGroupByGroupId(group,pageable);
            return employees;
        }

        /**
         *
         * 树切换部门
         *
         * */
        if(!depid.equals("")&&turnFlag.equals("")){
            pageable=PageRequest.of(0,pageSize);
            SysGroup group =jpaGroup.findById(Integer.parseInt(depid)).get();
            employees=jpaEmployee.findEmployeesBySysGroupByGroupId(group,pageable);
            return employees;
        }

        /**
         *
         * 刷新列表
         *
         * */
        if(!refreshFlag.equals("")){
            try {
                userService.updateUserTable();
            }catch (NamingException e){

            }

            pageable=PageRequest.of(0,pageSize);

            List<SysGroup> groups=jpaGroup.findAllExcludDeleted();
            SysGroup group =groups.get(2);
            employees=jpaEmployee.findEmployeesBySysGroupByGroupId(group,pageable);
            return employees;
        }



        /**
         * 获得搜索数据
         * */
        if(!keyWord.equals("")&&turnFlag.equals("")){
            pageable=PageRequest.of(0,pageSize);
            keyWord=ConvertStrForSearch.getFormatedString(keyWord);
            employees=jpaEmployee.findEmployeesByEnameLike(keyWord,pageable);
            if(employees.isEmpty()){
                employees=jpaEmployee.findEmployeesByPingyinLike(keyWord,pageable);
            }
            return employees;
        }

        /**
         *
         * 切换页面
         *
         * */

        if(!turnFlag.equals("")){


            if(turnFlag.equals("pre")){
                pageable=pageable.previousOrFirst();
            }else if(turnFlag.equals("next")){
                pageable=pageable.next();
            }
            if(!keyWord.equals("")){
                keyWord=ConvertStrForSearch.getFormatedString(keyWord);
                employees=jpaEmployee.findEmployeesByEnameLike(keyWord,pageable);
                if(employees.isEmpty()){
                    employees=jpaEmployee.findEmployeesByPingyinLike(keyWord,pageable);
                }
            }else if(!depid.equals("")){
                SysGroup group =jpaGroup.findById(Integer.parseInt(depid)).get();
                employees=jpaEmployee.findEmployeesBySysGroupByGroupId(group,pageable);
            }
        }

        return employees;

    }
    /**
     *
     * 同步ad ladp和本地数据库数据
     *
     * */
    public void updateUserTable() throws NamingException {
//        JpaEmployee jpaEmployee,String adip,String adname,String username,String userpwd
        Adinfo adinfo =jpaAdinfo.findAll().get(0);
        UpdateUserDB.updateUserTable(jpaEmployee,adinfo.getAdip(),adinfo.getDc(),adinfo.getDomainadminname(),adinfo.getDomainadminpwd());
    }

    public List<String> getUserNamesByUsersList(List<Employee> employees){
        List<String> names=new ArrayList<>();
        for (Employee employee:employees){
            names.add(employee.getEname());
        }
        return names;
    }

    public Set<Resources> getResourcesByEmployee(Employee e){
        List<Role> roles = jpaEmployee.findRoleByEmployee(e);
        Set<Resources> resourcesSet = new HashSet<>();
        for (Role role : roles) {
            resourcesSet.addAll(jpaRole.findMenusByRole("菜单",role));
        }
        return resourcesSet;
    }

    public List<Role> getRolesByEmployee(Employee e){
        return jpaEmployee.findRoleByEmployee(e);
    }

}
