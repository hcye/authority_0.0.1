package com.rbac.demo.controller;

import com.rbac.demo.entity.ScanInfo;
import com.rbac.demo.jpa.JpaFilter;
import com.rbac.demo.pcap4j.ArpReply;
import com.rbac.demo.pcap4j.GetMacByIp;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ThisisAMethod {
    @Autowired
    static JpaFilter jpaFilter;
    public static void main(String[] args) throws PcapNativeException, NotOpenException, UnknownHostException {
        List<ScanInfo> scanInfoList = jpaFilter.findAll();
        List<String> ips=new ArrayList<>();
        ArpReply reply=new ArpReply();
        for (ScanInfo scanInfo : scanInfoList) {
            if (scanInfo == null || scanInfo.getOuiByOuiId() == null) {
                return;
            }
            String info = scanInfo.getOuiByOuiId().getComInfo();
            if (info.contains("HUAWEI") || info.contains("TP-LINK")
                    || info.contains("vivo") || info.contains("Netcore") || info.contains("360")
                    || info.contains("Tenda") || info.contains("Xiaomi") || scanInfo.getIp().equals("10.75.60.156")) {
                ips.add(scanInfo.getIp());
            }

        }
        GetMacByIp getMacByIp=new GetMacByIp();
//        try {
////            Map<String,String>  map=getMacByIp.GetMac(ips,"10.75.60.35", "6c:4b:90:8b:42:03", "10.75.60.35", 2);
////            if (!map.isEmpty()) {
//               /* reply.ARPSpoofing(map, "6c:4b:90:8b:42:03", "10.75.60.1", "10.75.60.35");*/
////            } else {
////                System.out.println("主机不存活");
////            }
////        } catch (PcapNativeException e) {
////            e.printStackTrace();
////        } catch (UnknownHostException e) {
////            e.printStackTrace();
////        } catch (NotOpenException e) {
////            e.printStackTrace();
//        }
    }
}
