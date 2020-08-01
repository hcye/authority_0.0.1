package com.rbac.demo.filter;

/*import com.rbac.demo.entity.Func;
import com.rbac.demo.entity.Menus;
import com.rbac.demo.entity.Role2Menus;
import com.rbac.demo.entity.User;*/
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@Configuration
public class AuthorityFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

      /*  HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User user= (User) request.getSession().getAttribute("user");
        String uri=((HttpServletRequest) servletRequest).getRequestURI();

        if(uri.contains("index")||uri.contains(".js")||uri.contains(".ico")||uri.contains(".gif")||uri.contains(".json")
                ||uri.contains(".css")||uri.contains("font")||uri.contains(".img")||uri.contains("finish")){
            filterChain.doFilter(servletRequest,servletResponse);
        }else if(uri.contains("login")||uri.equals("/")){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            if(user.getId()==1){
                filterChain.doFilter(servletRequest,servletResponse);
            }else {
                List<Role2Menus> role2Menus = (List<Role2Menus>) user.getRoleByRoleId().getRole2MenusById();
                List<Menus> listM=new ArrayList<>();
                List<Func> funcList=new ArrayList<>();
                for (Role2Menus role2Menu:role2Menus){
                    listM.add(role2Menu.getMenusByMenuId());
                }
                for (Menus menus:listM){
                    funcList.addAll(menus.getFuncsById());
                }
                boolean flag=false;
                for (Func func:funcList){
                    System.out.println(uri+"---"+func.getUrl());
                    if(uri.contains(func.getUrl())){
                        flag=true;
                    }
                }
                if(flag){
                    filterChain.doFilter(servletRequest,servletResponse);
                }else {
                    response.sendRedirect("error");
                }
            }

        }*/

    }

    @Override
    public void destroy() {

    }
}
