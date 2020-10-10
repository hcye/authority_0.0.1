package com.rbac.demo;

import com.rbac.demo.entity.AssetType;
import com.rbac.demo.entity.OperatRecord;
import com.rbac.demo.jpa.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ResourceUtils;

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
    public void t() throws IOException, ParseException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String st="2020-10-01";
        java.util.Date start=simpleDateFormat.parse(st);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.MONTH,-4);
        java.util.Date end=calendar.getTime();
        Page<OperatRecord> page=jpaOperatRecord.findOperatRecordsByActionAndActionTimeBetween("借用",end,start,PageRequest.of(0,400));
        List<OperatRecord> operatRecords=page.getContent();
        List<OperatRecord> notReturn=new ArrayList<>();
        for (OperatRecord operatRecord:operatRecords){
            if(operatRecord.getAssertByAssertAsset().getEmployeeByBorrower()!=null){
                notReturn.add(operatRecord);
            }
        }
        for (OperatRecord record:notReturn){
            System.out.println(record.getAssertByAssertAsset());
        }


    }
}
