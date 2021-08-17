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
        String path="http://192.168.100.82:8099/source/svn/project/middleware/";
        String[] paths=path.split("source");
        for(String p:paths){
            System.out.println(p);
        }
    }
}
