package com.rbac.hcye_admin.Interceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpSession session=request.getSession();
//
//        String loginUserName= (String) SecurityUtils.getSubject().getPrincipal();
//        System.out.println(loginUserName+"---------");
//        Employee employee= (Employee) session.getAttribute(loginUserName);
//
//
//        if(employee==null){
//            System.out.println("employee == null logout");
//            response.sendRedirect(request.getContextPath()+"/logout");
//            return false;
//        }else {
            return true;

    }
}
