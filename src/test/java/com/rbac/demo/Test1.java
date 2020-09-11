package com.rbac.demo;

import com.rbac.demo.entity.*;
import com.rbac.demo.jpa.*;
import com.rbac.demo.shiro.ShiroUtils;
import com.rbac.demo.tool.ConvertStrForSearch;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.DigestUtils;

import javax.sql.rowset.spi.SyncResolver;
import java.util.EnumMap;
import java.util.Iterator;
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
    @Test
    public void t(){


        Employee employee=jpaEmployee.findById(169).get();
        employee.setPwd(ShiroUtils.encryption("Yhc14253666", ByteSource.Util.bytes("yehangcheng").toHex()));
        jpaEmployee.saveAndFlush(employee);

    }
}
