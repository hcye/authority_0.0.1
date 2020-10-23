package com.rbac.demo.realm;
import com.rbac.demo.entity.Employee;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.service.UserService;
import com.rbac.demo.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

@EnableCaching
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
        Employee employee=jpaEmployeee.findEmployeeByLoginName(username);


        if(employee==null){
            return null;
        }
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userService.getRoleSStrSet(employee));
        authorizationInfo.setStringPermissions(userService.getPermissionStrSet(employee));
        return authorizationInfo;
    }
    /**
     * 认证
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken){
        UsernamePasswordToken token= (UsernamePasswordToken) authenticationToken;
        Employee employee=jpaEmployeee.findEmployeeByLoginName(token.getUsername());

        if(employee!=null){
            if(employee.getStatus()==null||employee.getStatus()==1||employee.getOnjob().equals("1")||employee.getSysGroupByGroupId().getAvalible()==0||employee.getSysGroupByGroupId().getDeleteFlag()==1){
                throw new LockedAccountException();//账号锁定
            }
        }else {
            throw new UnknownAccountException();
        }
         //这里要实现密码验证需要重写credentialsMater,然后在realmconfig内getDefaultSecurityManager方法上 userRealm参数上设置这个匹配器
        //        userRealm.setCredentialsMatcher(myCredentialsMatcher());
        //        defaultSecurityManager.setRealm(userRealm);
        return new SimpleAuthenticationInfo(     //
                employee.getLoginName(),  //用户名principle
                ShiroUtils.encryption(String.valueOf(token.getPassword()),ByteSource.Util.bytes(employee.getPingyin()).toHex()),    //密码password.加密后的密码
                ByteSource.Util.bytes(employee.getPingyin()),
                getName());             //realm名字

    }

    public void clearCachedAuthorizationInfo()
    {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

    @Override
    public CacheManager getCacheManager() {
        return super.getCacheManager();
    }


}
