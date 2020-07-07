package com.rbac.demo;

import com.rbac.demo.entity.ScanInfo;
import com.rbac.demo.jpa.JpaFilter;
import com.rbac.demo.pcap4j.ArpReply;
import com.rbac.demo.pcap4j.GetIps;
import com.rbac.demo.pcap4j.GetMacByIp;
import org.junit.jupiter.api.Test;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@SpringBootTest
public class Pcap4JTest3 {
    @Autowired
    private JpaFilter jpaFilter;
    @Test
    public void test() throws PcapNativeException, NotOpenException, UnknownHostException {
        List<String> list=new GetIps().get(jpaFilter);
        GetMacByIp getMacByIp = new GetMacByIp();
        ArpReply arpReply = new ArpReply();
//        List<String > list=new ArrayList<>();

        Map<String, String> dstMac = null;
        System.out.println(list);
        dstMac = getMacByIp.GetMac(list, "10.75.60.35", "6c:4b:90:8b:42:03", "10.75.60.35", 4);
        System.out.println(dstMac);
        if (!dstMac.isEmpty()) {
            arpReply.createThread(dstMac, "6c:4b:90:8b:42:03", "10.75.60.1", "10.75.60.35");
        } else {
            System.out.println("主机不存活");
        }

    }

//        String strDstIpAddress,String strSrcIpAddress,String srcMacAddr,String interfaceIp

}

