package com.rbac.demo;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.Role;
import com.rbac.demo.entity.SwSwitch;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.jpa.JpaRole;
import com.rbac.demo.jpa.JpaSwSwitch;
import com.rbac.demo.service.RoleService;
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
    @Test
    public void t1(){
        Pageable pageable= PageRequest.of(1,10);
        Page<SwSwitch> page=jpaSwSwitch.findSwSwitchesByLabelLikeAndsAndfirm("%%","华为",pageable);
        System.out.println(page.toList().size());
    }
}
