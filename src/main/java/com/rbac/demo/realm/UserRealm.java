package com.rbac.demo.realm;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.service.RoleService;
import com.rbac.demo.service.UserService;
import com.rbac.demo.shiro.ShiroUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private JpaEmployee jpaEmployeee;
    @Autowired
    private UserService userService;

    /**
     * 授权
     * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username= (String) principalCollection.getPrimaryPrincipal();
        List<Employee> employees=jpaEmployeee.findEmployeesByEname(username);
        if(employees==null||employees.size()==0){
            return null;
        }
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userService.getRoleSStrSet(employees.get(0)));
        authorizationInfo.setStringPermissions(userService.getPermissionStrSet(employees.get(0)));
        return authorizationInfo;
    }
    /**
     * 认证
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token= (UsernamePasswordToken) authenticationToken;
        List<Employee> employees=jpaEmployeee.findEmployeesByEname(token.getUsername());
        if(employees==null||employees.size()==0){
            throw new UnknownAccountException(); //账号不存在
        }else if(employees.get(0).getLocked()==null||employees.get(0).getLocked()==1){
            throw new LockedAccountException();//账号锁定
        }else if(!employees.get(0).getPwd().equals(new String(token.getPassword()))){
            throw new IncorrectCredentialsException();  //密码错误
        }
        return new SimpleAuthenticationInfo(
                employees.get(0).getEname(),  //用户名
                employees.get(0).getPwd(),    //密码
                ByteSource.Util.bytes("hcye".getBytes()),  //盐
                getName());             //realm名字

    }
}
