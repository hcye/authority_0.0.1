package com.rbac.demo.controller.user;
import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.Resources;
import com.rbac.demo.entity.Role;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.jpa.JpaGroup;
import com.rbac.demo.jpa.JpaRole;
import com.rbac.demo.service.UserService;
import com.rbac.demo.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.Validate;

import java.util.*;


@Controller
public class UserController {
    @Autowired
    private JpaEmployee jpaEmployee;
    @Autowired
    private JpaRole jpaRole;
    @Autowired
    private JpaGroup jpaGroup;
    @Autowired
    private UserService userService;
    @GetMapping("/user/undefined")
    public String add(){
        return "basicDoc";
    }
    @GetMapping("/delete")
    public String delete(){
        return "delete";
    }
    //编辑用户
    @GetMapping("/user/edit")
    public String turnToEditPage(int id, Model model){
        Employee employee=jpaEmployee.findById(id).get();
        List<Role> rolesUserHas= userService.getRolesByEmployee(employee);
        List<String> groupsName=jpaGroup.getDistinctGourpName();
        List<Role> roles=jpaRole.findAllRole();
        //普通用户不能成为超级管理员
        roles.remove(jpaRole.findById(1).get());
        String gname=employee.getSysGroupByGroupId().getGname();
        List<String> status=new ArrayList<>();
        List<String> sex=new ArrayList<>();
        int statu=employee.getStatus();
        if(employee.getSex().equals("男")){
            sex.add("男");
            sex.add("女");
        }else {
            sex.add("女");
            sex.add("男");
        }
        status.add("启用");
        status.add("禁用");
        if(statu==1){
            Collections.reverse(status);   //数组翻转
        }
        groupsName.remove(gname);
        groupsName.add(0,gname);
        model.addAttribute("employee",employee);
        model.addAttribute("status",status);
        model.addAttribute("group",groupsName);
        model.addAttribute("roles",roles);
        model.addAttribute("rolesBind",rolesUserHas);
        return "user/edit";
    }
    //进入登录页面
    @GetMapping("/")
    public String login(){
        return "login.html";
    }
    //登录
    @RequestMapping("/index")
    public String login(String  username, String pwd, Model model) {
        /**
         * 刷新是判断session是否有值。如果没有则认为用户在执行登录，进行密码校验，
         * 如果没值则认为用户执行刷新。
         *
         * */


        Employee loginUser;
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        ShiroUtils.clearCachedAuthorizationInfo();
        Set<Resources> resourcesSet;
        if (session.getAttribute("user") == null) {
            if(username==null){
                //session过期
                return "login";
            }
            UsernamePasswordToken token = new UsernamePasswordToken(username, pwd);
            try {
                subject.login(token);
            } catch (UnknownAccountException b) {
                System.out.println("用户名错误");
                return "login";
            } catch (LockedAccountException b) {
                System.out.println("账户锁定");
                return "login";
            } catch (IncorrectCredentialsException b) {
                System.out.println("密码错误");
                return "login";
            }
            Employee employee= jpaEmployee.findEmployeeByLoginName(username);
            resourcesSet=userService.getResourcesByEmployee(employee);
            subject.hasRole("超级管理员");
            session.setAttribute("user", employee);
            loginUser=employee;

        }else {

            Employee employee= (Employee) session.getAttribute("user");
            loginUser=employee;
            resourcesSet=userService.getResourcesByEmployee(employee);
        }
        model.addAttribute("user",loginUser);
        model.addAttribute("resources", resourcesSet);
        return "index";
    }

    @RequiresPermissions("asm:user:view")
    @GetMapping("/user/user")
    public String user(){
        return "user/user";
    }


    @GetMapping("/user/personal_center")
    public String personal_center(Model model){

        Employee employee= (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
        model.addAttribute("user",employee);
        List<String> sexs=new ArrayList<>();
        if(employee.getSex().equals("男")){
            sexs.add("男");
            sexs.add("女");
        }else {
            sexs.add("女");
            sexs.add("男");
        }
        model.addAttribute("sexs",sexs);
        return "user/personal_center";
    }

    @GetMapping("/user/change_personal_info")
    public String personal_center(String tx_uri,String mail,String sex,Model model){
        Employee employee= (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
        if(tx_uri!=null&&!tx_uri.equals("")){
            employee.setTxUri(tx_uri.split("/")[3]);
        }
        if(mail!=null&&!mail.equals("")){
            employee.setEmail(mail);
        }
        if(sex!=null&&!sex.equals("")){
            employee.setSex(sex);
        }
        jpaEmployee.save(employee);

        List<String> sexs=new ArrayList<>();
        if(employee.getSex().equals("男")){
            sexs.add("男");
            sexs.add("女");
        }else {
            sexs.add("女");
            sexs.add("男");
        }
        model.addAttribute("sexs",sexs);
        model.addAttribute("user",employee);
        return "redirect:/common/basicDoc";
    }

    @GetMapping("/user/select_tx")
    public String change_tx(){
        return "user/select_tx";
    }

    @GetMapping("/user/change_password")
    public String change_pwd(Model model){
        Employee employee= (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
        model.addAttribute("user",employee);
        return "user/change_pwd";
    }

}
