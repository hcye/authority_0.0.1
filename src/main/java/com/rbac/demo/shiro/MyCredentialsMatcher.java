package com.rbac.demo.shiro;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.jpa.JpaEmployee;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MyCredentialsMatcher implements CredentialsMatcher {
    @Autowired
    private JpaEmployee jpaEmployee;
    /**
     * Returns {@code true} if the provided token credentials match the stored account credentials,
     * {@code false} otherwise.
     *
     * @param token the {@code AuthenticationToken} submitted during the authentication attempt
     * @param info  the {@code AuthenticationInfo} stored in the system.
     * @return {@code true} if the provided token credentials match the stored account credentials,
     * {@code false} otherwise.
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        char[] pwd= (char[]) token.getCredentials();
        String inputPwd=String.valueOf(pwd);
        String name= (String) token.getPrincipal();
        Employee employee =jpaEmployee.findEmployeeByLoginName(name);

        if (employee!=null){
            String dbPwd=employee.getPwd();
            String encryptPwd=ShiroUtils.encryption(inputPwd,ByteSource.Util.bytes(employee.getPingyin()).toHex());
            if(!dbPwd.equals(encryptPwd)){
                throw new IncorrectCredentialsException();//密码错误
            }
        }else {
            throw new UnknownAccountException();//用户名不存在
        }
        return true;
    }
}
