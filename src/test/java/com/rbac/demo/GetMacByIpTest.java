package com.rbac.demo;

import com.rbac.demo.pcap4j.GetMacByIp;
import org.junit.jupiter.api.Test;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class GetMacByIpTest {
    @Test
    public void test() throws PcapNativeException, NotOpenException, UnknownHostException {
        GetMacByIp getMacByIp=new GetMacByIp();
        List<String> ips=new ArrayList<>();
        for (int i=50;i<200;i++){
            ips.add("10.75.60."+i);
        }
        Map<String,String> map=getMacByIp.GetMac(ips, "10.75.60.35", "6c:4b:90:8b:42:03", "10.75.60.35", 1);
        System.out.println(map);
    }
}
