package com.rbac.demo.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;

public class ShiroUtil {
    public static String createSecurPwd(String pwd,String salt){
        return new SimpleHash("MD5", pwd, salt, 128).toString();
    }
}
