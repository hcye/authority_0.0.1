package com.rbac.demo.service;

import com.rbac.demo.entity.Assert;
import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.OperatRecord;
import com.rbac.demo.jpa.JpaOperatRecord;
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
        Employee dealer= (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
        operatRecord.setAction(ac);
        operatRecord.setActionTime(new Timestamp(new Date().getTime()));
        operatRecord.setAssertByAssertAsset(ast);
        operatRecord.setEmployeeByAssertEmp(employee);
        operatRecord.setEmployeeByDealer(dealer);
        jpaOperatRecord.save(operatRecord);
    }
}
