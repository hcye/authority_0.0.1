package com.rbac.hcye_admin;

import com.rbac.hcye_admin.entity.AssetType;
import com.rbac.hcye_admin.entity.Employee;
import com.rbac.hcye_admin.entity.Role;
import com.rbac.hcye_admin.jpa.JpaAssetType;
import com.rbac.hcye_admin.jpa.JpaEmployee;
import com.rbac.hcye_admin.jpa.JpaRole;
import com.rbac.hcye_admin.jpa.JpaSwSwitch;
import com.rbac.hcye_admin.service.network.SnmpCore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.Embeddable;
import java.util.List;

@SpringBootTest
public class JpaTests {
    @Autowired
    JpaEmployee jpaEmployee;
    @Autowired
    JpaAssetType jpaAssetType;

    @Autowired
    JpaRole jpaRole;
    @Autowired
    JpaSwSwitch jpaSwSwitch;
    @Autowired
    SnmpCore snmpCore;
    @Test
    public void t1(){
        List< AssetType> types =jpaAssetType.findAssertType();
        }
    }

