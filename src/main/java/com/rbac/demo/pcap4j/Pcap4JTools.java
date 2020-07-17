package com.rbac.demo.pcap4j;


import java.util.ArrayList;
import java.util.List;

public class Pcap4JTools {
   public List<String> getIpRange(String dstip) {
       List<String> res=new ArrayList<>();   //存储结果，[0] 代表 开始的ip,[1]代表第ipRange结尾ip的第四位数字
       String[] dstips;
       String mask;
       String startIp;
       if (dstip.contains("/")) {
           dstips = dstip.split("/");
           mask = dstips[1];
           startIp = dstips[0];
       } else {
           mask = "32";
           startIp = dstip;
       }
       try {
           Integer.parseInt(mask);
       } catch (NumberFormatException e) {
           return null;
       }
       String[] ips = startIp.split("\\.");
       if (ips.length != 4) {
           return null;
       }
       int end;
       if (mask.equals("32") || Integer.parseInt(mask) > 32 || Integer.parseInt(mask) < 1) {
           end = Integer.parseInt(ips[3]);
       } else {
           end = (int) (Integer.parseInt(ips[3]) + Math.pow(2, (32 - Integer.parseInt(mask))));
           if (end > 255) {
               end = 255;
           } else {
               end = end - 2;
           }
       }
       res.add(startIp);
       res.add(String.valueOf(end));
       return res;
   }
   public boolean isDifferentVlan(String dstIp,String gateway){
       String[] dstIps= dstIp.split("\\.");
       String[] gateways=gateway.split("\\.");
       String dstIpHead=dstIps[0]+dstIps[1]+dstIps[2];
       String gatewayHead=gateways[0]+gateways[1]+gateways[2];
       if(dstIpHead.equals(gatewayHead)){
           return true;
       }else {
           return false;
       }
   }
}
