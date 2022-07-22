package com.rbac.hcye_admin.service;

import com.rbac.hcye_admin.entity.Assert;
import com.rbac.hcye_admin.entity.Employee;
import com.rbac.hcye_admin.entity.OperatRecord;
import com.rbac.hcye_admin.jpa.JpaOperatRecord;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class WriteLog {
    @Autowired
    private JpaOperatRecord jpaOperatRecord;
    public void write(Assert ast, Employee employee,String ac){
        OperatRecord operatRecord=new OperatRecord();
        String loginUserName= (String) SecurityUtils.getSubject().getPrincipal();
        Employee dealer= (Employee) SecurityUtils.getSubject().getSession().getAttribute(loginUserName);
        operatRecord.setAction(ac);
        operatRecord.setActionTime(new Timestamp(new Date().getTime()));
        operatRecord.setAssertByAssertAsset(ast);
        operatRecord.setEmployeeByAssertEmp(employee);
        operatRecord.setEmployeeByDealer(dealer);
        jpaOperatRecord.save(operatRecord);
    }
}
