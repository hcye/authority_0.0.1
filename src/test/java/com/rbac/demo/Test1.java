package com.rbac.demo;

import com.rbac.demo.jpa.*;
import com.rbac.demo.service.UpdateUserDB;
import com.rbac.demo.shiro.ShiroUtils;
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
    @Test
    public void t() throws NamingException, ParseException {
        String encryptPwd= ShiroUtils.encryption("admin", ByteSource.Util.bytes("admin").toHex());
        System.out.println(encryptPwd);
    /*    UpdateUserDB.updateUserTable(jpaEmployee,"192.168.100.10","hsaecd","yehangcheng@hsaecd.com","Yhc142536..");*/


        DateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
        format1.parse("2020/07/01");
    }
}
