package com.rbac.hcye_admin.Interceptor;

import com.rbac.hcye_admin.entity.Employee;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session=request.getSession();
        try {
            if(SecurityUtils.getSecurityManager()==null){
                response.sendRedirect(request.getContextPath()+"/logout");
            }
        }catch (UnavailableSecurityManagerException e){
            response.sendRedirect(request.getContextPath()+"/logout");
        }

        String loginUserName= (String) SecurityUtils.getSubject().getPrincipal();
        Employee employee= (Employee) session.getAttribute(loginUserName);
        if(employee==null){
            response.sendRedirect(request.getContextPath()+"/logout");
            return false;
        }else {
            return true;
        }
    }
}
