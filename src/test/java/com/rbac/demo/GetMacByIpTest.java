package com.rbac.demo;

import com.rbac.demo.pcap4j.GetMacByIp;
import com.rbac.demo.pcap4j.SendArpRequest;
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
        SendArpRequest sendArpRequest=new SendArpRequest();
//        String mac=sendArpRequest.sendArp( "10.75.60.35", "6c:4b:90:8b:42:03","10.75.60.2");
//        System.out.println(mac);
    }
}
