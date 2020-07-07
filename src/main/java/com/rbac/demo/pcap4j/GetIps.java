package com.rbac.demo.pcap4j;

import com.rbac.demo.entity.ScanInfo;
import com.rbac.demo.jpa.JpaFilter;

import java.util.ArrayList;
import java.util.List;

public class GetIps {
    public List<String> get(JpaFilter jpaFilter) {
        List<ScanInfo> scanInfoList = jpaFilter.findAll();
        List<String> list = new ArrayList<>();
        for (ScanInfo scanInfo : scanInfoList) {
            if (scanInfo == null || scanInfo.getOuiByOuiId() == null) {
                continue;
            }
            String info = scanInfo.getOuiByOuiId().getComInfo();
            if (info.contains("HUAWEI") || info.contains("TP-LINK")
                    || info.contains("vivo") || info.contains("Netcore") || info.contains("360")
                    || info.contains("Tenda") || info.contains("Xiaomi")||info.contains("H3C")) {
                list.add(scanInfo.getIp().trim());
            }
        }
        return list;
    }

}
