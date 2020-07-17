package com.rbac.demo.pcap4j;

import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;

import java.util.concurrent.ExecutorService;

public class GetDstMac {
    public String getDstMac(String dstIp, String gatewayIp, PcapNetworkInterface nif, PcapHandle sendHandle) throws PcapNativeException {

        final String srcip=nif.getAddresses().get(0).getAddress().getHostAddress();
        BuildArpPacket buildArpPacket=new BuildArpPacket(nif);
        SendArpRequest sendArpRequest=new SendArpRequest();
        String dstMac=null;
        String[] strs= srcip.split("\\.");
        String[] strs1= dstIp.split("\\.");
        /*默认掩码24位
         * 判断是否同一网段
         * */
        if(strs[0].equals(strs1[0])&&strs[1].equals(strs1[1])&&strs[2].equals(strs1[2])){
//            String strSrcIpAddress,String srcMac,String strDstIpAddress
            dstMac=sendArpRequest.sendArp(dstIp,nif,sendHandle, buildArpPacket);
            /*
             * 如果解析到mac地址就判断主机存活
             * */
            return dstMac;
        }else {
            /*
             * 同网段没解析到则跨网段解析
             * 获得网关mac地址
             * */
            dstMac=sendArpRequest.sendArp(gatewayIp,nif,sendHandle, buildArpPacket);
            return dstMac;
        }

    }
}
