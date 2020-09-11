package com.rbac.demo.controller.restController;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.Role;
import com.rbac.demo.entity.User2Role;
import com.rbac.demo.jpa.JpaEmployee;
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
    @PostMapping("/user/findUserByNameLike")
    public List<String> findUsers(String name){
        name= ConvertStrForSearch.getFormatedString(name);
        List<Employee> employeeList=jpaEmployee.findEmployeesByEnameLike(name);
        if(employeeList.isEmpty()){
            employeeList=jpaEmployee.findEmployeesByPingyinLike(name);
        }
        List<String> names=service.getUserNamesByUsersList(employeeList);
        System.out.println(names.size());
        return names;
    }
    @PostMapping("/user/doEdit")
//    id:$("#id").val(),email:$("#email").val(),sex:$("#sex").val(),status:$("#status"),group:$("#group"),pwd:$("#pwd"),roles:$("#roles").val()
    public Map<String,String> editUser(int id,String email,String sex,String status,String group,String pwd,String roles){
        Map<String,String> map=new HashMap<>();
        Set<String> selectedRoles=new HashSet<>();
        String[] rolesList=roles.split(",");
        for (String str:rolesList){
            int i=0;
            for (String str1:rolesList){
                if(str.equals(str1)){
                    i++;
                }
            }
            if(i%2!=0){
                selectedRoles.add(str);
                System.out.println(str);
            }
        }
        Employee employee=jpaEmployee.findById(id).get();
        if(status.equals("启用")){
            employee.setStatus((byte) 0);
        }else {
            employee.setStatus((byte) 1);
        }
        employee.setEmail(email);
        employee.setSex(sex);
        employee.setEdepart(group);
        pwd= ShiroUtils.encryption(pwd, ByteSource.Util.bytes(employee.getPingyin()).toHex());
        employee.setPwd(pwd);
        Collection<User2Role> user2Roles=new ArrayList<>();
        Iterator<String> roleIterator=selectedRoles.iterator();
        while (roleIterator.hasNext()){
            String roleID=roleIterator.next();
            if(roleID.length()>0){
                int roleId=Integer.parseInt(roleID);
                Role role=jpaRole.findById(roleId).get();
                User2Role user2Role=new User2Role();
                user2Role.setEmployeeByUserId(employee);
                user2Role.setRoleByRoleId(role);
                user2Roles.add(user2Role);
            }
        }
        jpaUser2Role.saveAll(user2Roles);
        jpaEmployee.saveAndFlush(employee);
        map.put("ok","ok");
        return map;
    }

    @PostMapping("/user/getPage")
    public Page<Employee> turn(int depId, int pageInput, String turnFlag, String keyWord, String refreshFlag){
        Page<Employee> page= service.getPage( depId, pageInput, turnFlag,keyWord,refreshFlag);
        return page;
    }
}
