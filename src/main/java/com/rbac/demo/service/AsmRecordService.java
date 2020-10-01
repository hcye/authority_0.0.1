package com.rbac.demo.service;

import com.rbac.demo.entity.Assert;
import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.OperatRecord;
import com.rbac.demo.jpa.JpaOperatRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;

@Component
public class AsmRecordService {
    @Autowired
    private  JpaOperatRecord jpaOperatRecord;
    public void write(String action, Timestamp actionTime, Employee employeeByDealer, Employee employeeByAssertEmp, Assert assertByAssertAsset){
        OperatRecord op=new OperatRecord( action,  actionTime,  employeeByDealer,  employeeByAssertEmp,  assertByAssertAsset);
        jpaOperatRecord.save(op);
    }
}
