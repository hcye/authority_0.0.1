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

import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @RequestMapping("/user/add")
    @RequiresPermissions("asm:add")
    public String add(){
        return "/hcye/test";
    }
    @RequestMapping("/delete")
    public String delete(){
        return "delete";
    }
    //编辑用户
    @RequestMapping("/user/edit")
    public String turnToEditPage(int id, Model model){
        Employee employee=jpaEmployee.findById(id).get();
        List<String> groupsName=jpaGroup.getDistinctGourpName();
        List<Role> roles=jpaRole.findAllRole();
        //普通用户不能成为超级管理员
        roles.remove(jpaRole.findById(1).get());
        String gname=employee.getSysGroupByGroupId().getGname();
        List<String> status=new ArrayList<>();
        int statu=employee.getStatus();

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
        return "user/edit";
    }
    //进入登录页面
    @RequestMapping("/")
    public String login(){
        return "login.html";
    }
    //登录
    @RequestMapping("/login")
    public String login(String  username, String pwd, Model model){

        /**
         * 测试环境手动清除缓存
         *
         * */
        ShiroUtils.clearCachedAuthorizationInfo();
        UsernamePasswordToken token=new UsernamePasswordToken(username,pwd);
        Subject subject= SecurityUtils.getSubject();
        try {
            subject.login(token);
        }catch (UnknownAccountException e){
            System.out.println("用户名错误");
            return "login";
        }catch (LockedAccountException e ){
            System.out.println("账户锁定");
            return "login";
        }catch (IncorrectCredentialsException e){
            System.out.println("密码错误");
            return "login";
        }
        /**
         * 调用subject.hasRole  触发realm授权函数，使进入首页后的shiro 标签有效
         * */
        /**
         * 这样做不优雅，暂时不知道怎么做
         * */
        Employee e=jpaEmployee.findEmployeesByEname(username).get(0);
        List<Role> roles=jpaEmployee.findRoleByEmployee(e);
        Set<Resources> resourcesSet=new HashSet<>();

        if(roles!=null){
            for (Role role:roles){

                resourcesSet.addAll(jpaRole.findMenusByRole(role));
            }
        }

        for (Resources resources:resourcesSet){
            System.out.println(resources.getDescription());
        }
        subject.hasRole("超级管理员");
        model.addAttribute("resources",resourcesSet);
        return "redirect:/index";

    }
    //用户翻页
    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/user/user")
    public String user(){
        return "/user/user";
    }

}
