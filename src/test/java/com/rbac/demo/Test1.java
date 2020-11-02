package com.rbac.demo;

import com.rbac.demo.entity.DevType;
import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.Role;
import com.rbac.demo.entity.User2Role;
import com.rbac.demo.jpa.*;
import com.rbac.demo.service.UpdateUserDB;
import com.rbac.demo.shiro.ShiroUtils;
import org.apache.catalina.User;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ResourceUtils;

import javax.naming.NamingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@SpringBootTest
public class Test1 {
    @Autowired
    JpaRole2Resources jpaRole2Resources;
    @Autowired
    JpaRole jpaRole;
    @Autowired
    JpaGroup jpaGroup;
    @Autowired
    JpaEmployee jpaEmployee;
    @Autowired
    JpaResources jpaResources;
    @Autowired
    JpaDevType jpaDevType;
    @Autowired
    JpaAssert jpaAssert;
    @Autowired
    JpaAssetType jpaAssetType;
    @Autowired
    JpaOperatRecord jpaOperatRecord;
    @Autowired
    JpaUser2Role jpaUser2Role;
    @Test
    public void t()  {
        DevType devType =jpaDevType.findDevTypeByDevNameAndAssetTypeByAssertTypeId("手机",jpaAssetType.findAssetTypeByName("手机"));
        System.out.println(devType);

    }
}
