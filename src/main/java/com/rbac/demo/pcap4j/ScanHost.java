package com.rbac.demo.pcap4j;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanHost {
    public ScanHost(){ }
//    srcip, srcmac, ipHead+i, srcGateway
    public void scan(String srcip,String srcmac,String srcGateway,String dstip) throws PcapNativeException, NotOpenException, UnknownHostException {
//        String srcip,String srcmac,String srcGateway,String dstip
        SendPacket pingPacket=new SendPacket();
        ExecutorService pool= Executors.newCachedThreadPool();
//        pingPacket.PingPacket(srcip,srcmac,srcGateway,dstip);
    }
}
