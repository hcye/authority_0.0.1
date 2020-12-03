package com.rbac.demo.controller.restController;

import com.rbac.demo.entity.*;
import com.rbac.demo.jpa.*;
import com.rbac.demo.service.UpdateUserDB;
import com.rbac.demo.service.UserService;
import com.rbac.demo.shiro.ShiroUtils;
import com.rbac.demo.tool.ConvertStrForSearch;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;
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
    @Autowired
    private JpaAdinfo jpaAdinfo;
    @PostMapping("/user/findUserByNameLike")
    public List<String> findUsers(String name){
        List<String> names=new ArrayList<>();
        name= ConvertStrForSearch.getFormatedString(name);
        List<Employee> employees;
        employees=jpaEmployee.findEmployeesByEnameLike(name);
        if(employees.size()==0){
            employees=jpaEmployee.findEmployeesByPingyinLike(name);
        }
        String str="";
        for (Employee employee:employees){
            str=employee.getEname()+"-"+employee.getLoginName();
            names.add(str);
        }
        return names;
    }

    @PostMapping("/user/findUserByNameLikeForReturn")
    public List<String> findUsersForReturn(String name){
        List<String> names=new ArrayList<>();
        name= ConvertStrForSearch.getFormatedString(name);
        List<Employee> employees;
        employees=jpaEmployee.findEmployeesByEnameLikeForReturn(name);
        if(employees.size()==0){
            employees=jpaEmployee.findEmployeesByPingyinLikeForReturn(name);
        }
        String str="";
        for (Employee employee:employees){
            str=employee.getEname()+"-"+employee.getLoginName();
            names.add(str);
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
    public Page<Employee> turn(String depId, String pageInput, String turnFlag, String keyWord, String refreshFlag) throws NamingException {
        Page<Employee> page= service.getPage( depId, pageInput, turnFlag,keyWord,refreshFlag);
        return page;
    }

    @PostMapping("/user/checkAdinfo")
    public Map<String,String> check(String dc,String dc_ip,String uname,String pwd)  {
        Map<String,String> map=new HashMap<>();
        Adinfo adinfo =jpaAdinfo.findAll().get(0);
        try {
            if(dc==null){
                NamingEnumeration<SearchResult> res= UpdateUserDB.getNamingEnumeration( adinfo.getAdip(), adinfo.getDc(), adinfo.getDomainadminname(),adinfo.getDomainadminpwd());
                if(!res.hasMoreElements()){
                    throw new RuntimeException("加载ad资源出错");
                }
            }else if(dc!=null){
                NamingEnumeration<SearchResult> res= UpdateUserDB.getNamingEnumeration( dc_ip, dc, uname,pwd);
                if(!res.hasMoreElements()){
                    throw new RuntimeException("加载ad资源出错");
                }else {
                    adinfo.setAdip(dc_ip);
                    adinfo.setDc(dc);
                    adinfo.setDomainadminname(uname);
                    adinfo.setDomainadminpwd(pwd);
                    jpaAdinfo.save(adinfo);
                }
            }

        }catch (RuntimeException | NamingException e){
            map.put("error","ad域信息验证失败！");
            return map;
        }
        map.put("ok","ad域信息验证成功！");
        return map;
    }

    @PostMapping("/user/getAdinfo")
    public Map<String,Adinfo> get()  {
        Map<String,Adinfo> map=new HashMap<>();
        Adinfo adinfo =jpaAdinfo.findAll().get(0);
        map.put("ok",adinfo);
        return map;
    }




    @PostMapping("/user/reset_password")
    public Map<String,String> turn(String oldPwd,String newPwd,String pwdConf){
        Map<String,String> map=new HashMap<>();

        Employee employee= (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
        String dbPwd=employee.getPwd();
        String encryptPwd=ShiroUtils.encryption(oldPwd,ByteSource.Util.bytes(employee.getPingyin()).toHex());

        if(!dbPwd.equals(encryptPwd)){
            map.put("error","旧密码不匹配");
            return map;
        }
        if(newPwd.length()>8){
            if(newPwd.equals(pwdConf)){
                String newp=ShiroUtils.encryption(newPwd,ByteSource.Util.bytes(employee.getPingyin()).toHex());
                employee.setPwd(newp);
                jpaEmployee.save(employee);
                map.put("success","修改成功");
                return map;
            }
        }else {
            map.put("error","修改失败");
            return map;
        }
        return map;
    }
}
