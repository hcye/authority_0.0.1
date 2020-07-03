package com.rbac.demo.pcap4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.pcap4j.core.*;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.ArpHardwareType;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.MacAddress;
import org.pcap4j.util.NifSelector;

public class GetMacByIp {

    private static final String COUNT_KEY = SendArpRequest.class.getName() + ".count";
    private static final int COUNT = Integer.getInteger(COUNT_KEY, 1);

    private static final String READ_TIMEOUT_KEY = SendArpRequest.class.getName() + ".readTimeout";
    private static final int READ_TIMEOUT = Integer.getInteger(READ_TIMEOUT_KEY, 10); // [ms]

    private static final String SNAPLEN_KEY = SendArpRequest.class.getName() + ".snaplen";
    private static final int SNAPLEN = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]


    private static MacAddress resolvedAddr;

    public GetMacByIp() {}

    public String  GetMac(String strDstIpAddress,String strSrcIpAddress,String srcMacAddr,String interfaceIp,int timeOutSeconds) throws PcapNativeException, UnknownHostException {

        InetAddress inetAddress=InetAddress.getByName(interfaceIp);
        PcapNetworkInterface nif=Pcaps.getDevByAddress(inetAddress); //获得网卡
        MacAddress SRC_MAC_ADDR = MacAddress.getByName(srcMacAddr);  //构造源mac地址对象

        PcapHandle handle = nif.openLive(SNAPLEN, PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);  //接收数据包
        PcapHandle sendHandle = nif.openLive(SNAPLEN, PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);  //发送数据包


        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            handle.setFilter(   //接收数据包的过滤器
                    "arp and src host "
                            + strDstIpAddress
                            + " and dst host "
                            + strSrcIpAddress
                            + " and ether dst "
                            + Pcaps.toBpfString(SRC_MAC_ADDR),
                    BpfCompileMode.OPTIMIZE);

            PacketListener listener = new PacketListener() {
                @Override
                public void gotPacket(PcapPacket pcapPacket) {
                    if (pcapPacket.contains(ArpPacket.class)) {
                        ArpPacket arp = pcapPacket.get(ArpPacket.class);
                        if (arp.getHeader().getOperation().equals(ArpOperation.REPLY)) {
                            GetMacByIp.resolvedAddr = arp.getHeader().getSrcHardwareAddr();
                        }
                    }
                }
            };


            Task t = new Task(handle, listener);
            pool.execute(t);


            ArpPacket.Builder arpBuilder = new ArpPacket.Builder();
            try {
                arpBuilder
                        .hardwareType(ArpHardwareType.ETHERNET)
                        .protocolType(EtherType.IPV4)
                        .hardwareAddrLength((byte) MacAddress.SIZE_IN_BYTES)
                        .protocolAddrLength((byte) ByteArrays.INET4_ADDRESS_SIZE_IN_BYTES)
                        .operation(ArpOperation.REQUEST)
                        .srcHardwareAddr(SRC_MAC_ADDR)
                        .srcProtocolAddr(InetAddress.getByName(strSrcIpAddress))
                        .dstHardwareAddr(MacAddress.ETHER_BROADCAST_ADDRESS)
                        .dstProtocolAddr(InetAddress.getByName(strDstIpAddress));
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException(e);
            }

            EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder();
            etherBuilder
                    .dstAddr(MacAddress.ETHER_BROADCAST_ADDRESS)
                    .srcAddr(SRC_MAC_ADDR)
                    .type(EtherType.ARP)
                    .payloadBuilder(arpBuilder)
                    .paddingAtBuild(true);

            for (int i = 0; i < 2; i++) {
                Packet p = etherBuilder.build();
                sendHandle.sendPacket(p);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } finally {
            try {
                pool.shutdown();   //查询是否任务完成
                if(!pool.awaitTermination(timeOutSeconds, TimeUnit.SECONDS)){   //启动计时器，如果在指定时间内没完成就返回false.执行资源关闭任务，关闭线程
                    try {
                        handle.breakLoop();  //中断loop
                        handle.close();
                        if (sendHandle != null && sendHandle.isOpen()) {
                            sendHandle.close();
                        }
                        if (pool != null && !pool.isShutdown()) {
                            pool.shutdownNow();
                        }

                    } catch (NotOpenException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {

            }
            if(resolvedAddr!=null){
                return resolvedAddr.toString();   //返回解析到的mac地址
            }else {
                return "0";   //如果在指定时间内没解析到mac地址，则认为该主机关机或不存在
            }
        }
        }

    private static class Task implements Runnable {

        private PcapHandle handle;
        private PacketListener listener;

        public Task(PcapHandle handle, PacketListener listener) {
            this.handle = handle;
            this.listener = listener;
        }

        @Override
        public void run() {
            try {
                handle.loop(COUNT, listener);
            } catch (PcapNativeException e) {

            } catch (InterruptedException e) {

            } catch (NotOpenException e) {

            }
        }
    }
}
