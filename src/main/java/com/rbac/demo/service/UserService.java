package com.rbac.demo.service;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.Resources;
import com.rbac.demo.entity.Role;
import com.rbac.demo.entity.SysGroup;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.jpa.JpaGroup;
import com.rbac.demo.jpa.JpaResources;
import com.rbac.demo.jpa.JpaRole;
import com.rbac.demo.tool.ConvertStrForSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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
            resourceList.addAll(jpaRole.findResourcesByRole(role));
            System.out.println(role.getRname());
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
            stringSet.add(role.getRname());
        }
        return stringSet;
    }
    //根据部门id获得部门的用户
    public Page<Employee> getPage(int depid,int pageInput,String turnFlag,String keyWord,String refreshFlag){
        pageInput=pageInput-1;   //pageable 的页码序号从0开始
        Page<Employee> employees=null;

        /**
         *
         * 刷新用户列表
         *
         * */
        if(refreshFlag!=null&&refreshFlag.equals("refresh")){
            userService.updateUserTable();
            Pageable pageable=PageRequest.of(0,pageSize);
            /**
             * //7是综合管理部的id，这里采用硬编码，不运行中动态获得，以后再改
             *
             * */
            SysGroup group =jpaGroup.findById(7).get();
            employees=jpaEmployee.findEmployeesBySysGroupByGroupId(group,pageable);
            return employees;
        }
        /**
         *
         * 搜索关键字不为空
         *
         * */

        if(keyWord!=null&&keyWord.length()>0&&turnFlag.equals("noAction")){
            Pageable pageable=PageRequest.of(0,pageSize);
            keyWord=ConvertStrForSearch.getFormatedString(keyWord);
            employees= jpaEmployee.findEmployeesByEname(keyWord,pageable);
            if(employees.isEmpty()){
                employees= jpaEmployee.findEmployeesByPingyin(keyWord,pageable);
            }
            return employees;
        }else if(keyWord!=null&&keyWord.length()>0&&!turnFlag.equals("noAction")){
            Pageable pageable=PageRequest.of(pageInput,pageSize);
            keyWord=ConvertStrForSearch.getFormatedString(keyWord);
            if(turnFlag.equals("pre")){
                if(pageable.hasPrevious()){
                    pageable=PageRequest.of(pageInput-1,pageSize);
                }
            }else if(turnFlag.equals("next")){
                pageable=pageable.next();
            }
            employees= jpaEmployee.findEmployeesByEname(keyWord,pageable);
            if(employees.isEmpty()){
                employees=jpaEmployee.findEmployeesByPingyin(keyWord,pageable);
            }
            return employees;
        }
        /**-------------------------------------------------------------------------------------**/

        /**
         *
         * 关键字为空
         *
         * */
        SysGroup group =jpaGroup.findById(depid).get();
        Pageable pageable= PageRequest.of(pageInput,pageSize);
        if(turnFlag.equals("pre")){
            if(pageable.hasPrevious()){
                pageable=PageRequest.of(pageInput-1,pageSize);
            }
        }else if(turnFlag.equals("next")){
            pageable=pageable.next();
        }
        employees=jpaEmployee.findEmployeesBySysGroupByGroupId(group,pageable);
        return employees;
    }
    /**
     *
     * 同步ad ladp和本地数据库数据
     *
     * */
    public void updateUserTable(){
//        JpaEmployee jpaEmployee,String adip,String adname,String username,String userpwd
        UpdateUserDB.updateUserTable(jpaEmployee,"192.168.100.10","hsaecd","yehangcheng@hsaecd.com","Yhc142536.");
    }

    public List<String> getUserNamesByUsersList(List<Employee> employees){
        List<String> names=new ArrayList<>();
        for (Employee employee:employees){
            names.add(employee.getEname());
        }
        return names;
    }

}
