package com.rbac.demo.controller;

import com.rbac.demo.dtree.Dtree;
import com.rbac.demo.dtree.GetDtreeList;
import com.rbac.demo.entity.Menus;
import com.rbac.demo.entity.Role2Menus;
import com.rbac.demo.entity.User;
import com.rbac.demo.jpa.JpaMenus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
@RestController
public class GetDtree {
    @Autowired
    GetDtreeList list;
    @RequestMapping("/getTree")
    public List<Dtree> getDtree(HttpSession session){
        User user= (User) session.getAttribute("user");
        List<Role2Menus> role2Menus= (List<Role2Menus>) user.getRoleByRoleId().getRole2MenusById();
        List<Menus> menus=new ArrayList<>();
        for (Role2Menus role2Menus1:role2Menus){
            menus.add(role2Menus1.getMenusByMenuId());
        }
       return list.getDtrees(menus);
    }
}
