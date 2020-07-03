package com.rbac.demo;

import com.rbac.demo.entity.Oui;
import com.rbac.demo.entity.ScanInfo;
import com.rbac.demo.jpa.JpaFilter;
import com.rbac.demo.jpa.JpaOui;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Pcap4jTest2 {
    @Autowired
    private JpaFilter jpaFilter;
    @Test
    public void test(){
        List<ScanInfo> scanInfoList=jpaFilter.findAll();
        for (ScanInfo scanInfo:scanInfoList){
            if(scanInfo.getOuiByOuiId()!=null){
                System.out.println(scanInfo.getMac()+"---"+scanInfo.getIp()+"---"+scanInfo.getOuiByOuiId().getComInfo());
            }else {
                System.out.println(scanInfo.getMac()+"---"+scanInfo.getIp()+"-找不到对应厂商--");
            }
        }
    }
}
