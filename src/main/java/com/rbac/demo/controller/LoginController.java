package com.rbac.demo.controller;

import com.rbac.demo.entity.Employee;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
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
            System.out.println(e);
            return "login";
        }
            return "index";

    }
}
