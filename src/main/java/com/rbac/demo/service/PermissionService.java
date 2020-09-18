package com.rbac.demo.service;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

@Service("permission")
public class PermissionService {
    //前端鉴权  var editflag = [[${@permission.getPermi('asm:role:edit')}]];
    private  final static  String disableBtn="layui-btn-disabled";
    private final static String enableBtn="";
    public String getPermi(String permi){
        String btnClass=SecurityUtils.getSubject().isPermitted(permi) ? enableBtn : disableBtn;
        return btnClass;
    }
}
