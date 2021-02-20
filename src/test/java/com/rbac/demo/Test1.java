package com.rbac.demo;

import com.rbac.demo.entity.*;
import com.rbac.demo.jpa.*;
import com.rbac.demo.service.GetAllAduser;
import com.rbac.demo.service.Sendmail;
import com.rbac.demo.service.UpdateUserDB;
import com.rbac.demo.service.network.SnmpCore;
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
    @Autowired
    SnmpCore snmpCore;
    @Autowired
    JpaSwOidTemp jpaSwOidTemp;

    @Test
    public void t() throws Exception {
     SwOidTemp swOidTemp =jpaSwOidTemp.findSwOidTempByOidName("arp");
 //    snmpCore.getIpMapInteger("192.168.101.100","yhc4133");
//     String oid=".1.3.6.1.4.1.2011.5.25.123.1.17.1.11.0.[p2].1.32";
  //   System.out.println(oid.substring(0,oid.indexOf("[p2]")));
//    System.out.println(oid.substring(oid.indexOf("[p2]")+"[p2]".length()));
        String a=snmpCore.searchPort("50:7b:9d:61:ed:7f");
        System.out.println(a);
    }
}
