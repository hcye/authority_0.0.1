package com.rbac.demo.controller;

import com.rbac.demo.entity.Func;
import com.rbac.demo.entity.Menus;
import com.rbac.demo.entity.Role2Menus;
import com.rbac.demo.entity.User;
import com.rbac.demo.jpa.JpaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class Login {
    @Autowired
    JpaUser jpaUser;
    @RequestMapping("/")
    public String login(String name, String pwd , HttpSession session,Model model){
       if(jpaUser.findUserByUnameAndPwd(name,pwd)!=null) {
           User user=jpaUser.findUserByUnameAndPwd(name,pwd);
           session.setAttribute("user",user);

           return "index";
       }else {

           return "login";
       }
    }
}
