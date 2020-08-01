package com.rbac.demo;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.Role;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.jpa.JpaRole;
import com.rbac.demo.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    RoleService roleService;
    @Test
    public void t1(){
        Employee employee =jpaEmployee.findEmployeeById(169);

//        List<Role> roles=jpaEmployee.findRoleByEmployee(employee);
//        System.out.println(roles);
    }
}
