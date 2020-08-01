package com.rbac.demo.controller;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.Menus;
import com.rbac.demo.entity.Role;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.jpa.JpaRole;
import com.rbac.demo.realm.UserRealm;
import com.rbac.demo.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class LoginController {
    @Autowired
    private JpaEmployee jpaEmployee;
    @Autowired
    private JpaRole jpaRole;
    @RequestMapping("/")
    public String index(){
        return "login";
    }
    @RequestMapping("/login")
    public String login(String  username, String pwd, Model model){

        UsernamePasswordToken token=new UsernamePasswordToken(username,pwd);
        Subject subject= SecurityUtils.getSubject();
        try {
            subject.login(token);
        }catch (UnknownAccountException|LockedAccountException| IncorrectCredentialsException e){
            return "login";
        }
            Employee e=jpaEmployee.findEmployeesByEname(username).get(0);
            List<Role> roles=jpaEmployee.findRoleByEmployee(e);
            Set<Menus> menus=new HashSet<>();
            if(roles!=null){
                for (Role role:roles){
                    List<Menus> menus1=jpaRole.findMenusByRole(role);
                    menus.addAll(menus1);
                }
            }
            model.addAttribute("menus",menus);
            return "index";

    }
}
