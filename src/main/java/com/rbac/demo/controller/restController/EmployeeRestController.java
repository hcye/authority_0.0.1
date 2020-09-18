package com.rbac.demo.controller.restController;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.Role;
import com.rbac.demo.entity.SysGroup;
import com.rbac.demo.entity.User2Role;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.jpa.JpaGroup;
import com.rbac.demo.jpa.JpaRole;
import com.rbac.demo.jpa.JpaUser2Role;
import com.rbac.demo.service.UserService;
import com.rbac.demo.shiro.ShiroUtils;
import com.rbac.demo.tool.ConvertStrForSearch;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class EmployeeRestController {

    @Autowired
    private JpaRole jpaRole;
    @Autowired
    private JpaEmployee jpaEmployee;
    @Autowired
    private JpaUser2Role jpaUser2Role;
    @Autowired
    private UserService service;
    @Autowired
    private JpaGroup jpaGroup;
    @PostMapping("/user/findUserByNameLike")
    public List<String> findUsers(String name){
        name= ConvertStrForSearch.getFormatedString(name);
        List<String> names;
        names=jpaEmployee.findEmployeesNameByNameLike(name);
        if(names.size()==0){
            names=jpaEmployee.findEmployeesNameByPinyinLike(name);
        }
        return names;
    }
    @PostMapping("/user/doEdit")
//    id:$("#id").val(),email:$("#email").val(),sex:$("#sex").val(),status:$("#status"),group:$("#group"),pwd:$("#pwd"),roles:$("#roles").val()
    public Map<String,String> editUser(int id,String email,String sex,String status,String group,String pwd,String roles){
        Map<String,String> map=new HashMap<>();

        SysGroup sysGroup =jpaGroup.findSysGroupByGname(group);
        String[] rolesList=roles.split(",");

        Employee employee=jpaEmployee.findById(id).get();
        if(status.equals("启用")){
            employee.setStatus((byte) 0);
        }else {
            employee.setStatus((byte) 1);
        }
        employee.setEmail(email);
        employee.setSex(sex);
        employee.setSysGroupByGroupId(sysGroup);
        if(!pwd.equals("")){
            pwd= ShiroUtils.encryption(pwd, ByteSource.Util.bytes(employee.getPingyin()).toHex());
            employee.setPwd(pwd);
        }
        /**
         * 删除原来的权限
         *
         * */
        List<User2Role> user2RolesBinded=jpaUser2Role.findUser2RolesByEmployeeByUserId(employee);
        jpaUser2Role.deleteAll(user2RolesBinded);
        /**
         * 更新权限
         * */
        for(String str:rolesList){
            if(str.equals("")){
                continue;
            }
            Role role=jpaRole.findById(Integer.parseInt(str)).get();
            User2Role user2Role=new User2Role();
            user2Role.setRoleByRoleId(role);
            user2Role.setEmployeeByUserId(employee);
            jpaUser2Role.save(user2Role);
        }
        jpaEmployee.save(employee);
        map.put("ok","ok");
        return map;
    }

    @PostMapping("/user/getPage")
    public Page<Employee> turn(String depId, String pageInput, String turnFlag, String keyWord, String refreshFlag){
        Page<Employee> page= service.getPage( depId, pageInput, turnFlag,keyWord,refreshFlag);
        return page;
    }
}
