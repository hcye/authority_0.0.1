package com.rbac.demo;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.Role;
import com.rbac.demo.entity.SwSwitch;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.jpa.JpaRole;
import com.rbac.demo.jpa.JpaSwSwitch;
import com.rbac.demo.service.RoleService;
import com.rbac.demo.service.network.SnmpCore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class JpaTests {
    @Autowired
    JpaEmployee jpaEmployee;
    @Autowired
    JpaRole jpaRole;
    @Autowired
    JpaSwSwitch jpaSwSwitch;
    @Autowired
    SnmpCore snmpCore;
    @Test
    public void t1(){
     String res=snmpCore.getMACByIP("192.168.10.10");
     System.out.println(res);
    }
}
