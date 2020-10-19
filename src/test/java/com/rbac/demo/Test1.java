package com.rbac.demo;

import com.rbac.demo.entity.AssetType;
import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.OperatRecord;
import com.rbac.demo.jpa.*;
import com.rbac.demo.service.UpdateUserDB;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ResourceUtils;

import javax.naming.NamingException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

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
    public void t() throws NamingException {

        UpdateUserDB.updateUserTable(jpaEmployee,"192.168.100.10","hsaecd","yehangcheng@hsaecd.com","Yhc142536..");
    }
}
