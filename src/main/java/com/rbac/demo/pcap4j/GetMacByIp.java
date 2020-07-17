package com.rbac.demo.pcap4j;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.pcap4j.core.*;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.*;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.MacAddress;
import org.pcap4j.util.NifSelector;

public class GetMacByIp {

    private static final String COUNT_KEY = SendArpRequest.class.getName() + ".count";
    private static final int COUNT = Integer.getInteger(COUNT_KEY, 1);
    private static final String TU_KEY = GetMacByIp.class.getName() + ".tu";
    private static final int TU = Integer.getInteger(TU_KEY, 4000); // [bytes]
    private static final String READ_TIMEOUT_KEY = SendArpRequest.class.getName() + ".readTimeout";
    private static final int READ_TIMEOUT = Integer.getInteger(READ_TIMEOUT_KEY, 10); // [ms]
    private static final String SNAPLEN_KEY = SendArpRequest.class.getName() + ".snaplen";
    private static final int SNAPLEN = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]


    private static MacAddress resolvedAddr;

    public GetMacByIp() {}
    /**
     * 通过arp包解析局域网内主机mac
     *
     * */
    public Map<String,String> GetMac(List<String> strDstIpAddresss,PcapNetworkInterface nif , int timeOutSeconds) throws PcapNativeException, NotOpenException {

        MacAddress SRC_MAC_ADDR = MacAddress.getByName(nif.getLinkLayerAddresses().get(0).toString());  //构造源mac地址对象
        PcapHandle handle = nif.openLive(SNAPLEN, PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);  //接收数据包
        PcapHandle sendHandle = nif.openLive(SNAPLEN, PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);  //发送数据包
        Map<String,String> res=new HashMap<>();
        for (String strDstIpAddress : strDstIpAddresss) {
            ExecutorService pool = Executors.newSingleThreadExecutor();
            try {
                if(!handle.isOpen()){
                    handle = nif.openLive(SNAPLEN, PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);  //接收数据包
                }
                handle.setFilter(   //接收数据包的过滤器
                        "icmp and ether dst " + Pcaps.toBpfString(SRC_MAC_ADDR), BpfCompileMode.OPTIMIZE);

                PacketListener listener = new PacketListener() {
                    @Override
                    public void gotPacket(PcapPacket pcapPacket) {
                        if (pcapPacket.contains(EthernetPacket.class)) {
                            EthernetPacket arp = pcapPacket.get(EthernetPacket.class);
                            if (arp.getHeader()!=null) {
                                GetMacByIp.resolvedAddr = arp.getHeader().getSrcAddr();
                            }
                        }
                    }
                };
                Task t = new Task(handle, listener);
                pool.execute(t);
                byte[] echoData = new byte[TU - 28];
                for (int i = 0; i < echoData.length; i++) {
                    echoData[i] = (byte) i;
                }
                IcmpV4EchoPacket.Builder echoBuilder = new IcmpV4EchoPacket.Builder();
                echoBuilder
                        .identifier((short) 1)
                        .payloadBuilder(new UnknownPacket.Builder().rawData(echoData));

                IcmpV4CommonPacket.Builder icmpV4CommonBuilder = new IcmpV4CommonPacket.Builder();
                icmpV4CommonBuilder
                        .type(IcmpV4Type.ECHO)
                        .code(IcmpV4Code.NO_CODE)
                        .payloadBuilder(echoBuilder)
                        .correctChecksumAtBuild(true);

                IpV4Packet.Builder ipv4Builder=new IpV4Packet.Builder();
                try {
                    ipv4Builder
                            .version(IpVersion.IPV4)
                            .tos(IpV4Rfc791Tos.newInstance((byte) 0))
                            .ttl((byte) 100)
                            .protocol(IpNumber.IPV4)
                            .srcAddr((Inet4Address) InetAddress.getByName(nif.getAddresses().get(0).toString()))
                            .dstAddr((Inet4Address) InetAddress.getByName(strDstIpAddress))
                            .payloadBuilder(icmpV4CommonBuilder)
                            .correctChecksumAtBuild(true)
                            .correctLengthAtBuild(true);
                } catch (UnknownHostException e) {
                    throw new IllegalArgumentException(e);
                }



                EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder();
                etherBuilder
                        .dstAddr(MacAddress.ETHER_BROADCAST_ADDRESS)
                        .srcAddr(SRC_MAC_ADDR)
                        .type(EtherType.IPV4)
                        .payloadBuilder(ipv4Builder)
                        .paddingAtBuild(true);
                for (int i = 0; i < timeOutSeconds; i++) {
                    Packet p = etherBuilder.build();
                    if(!sendHandle.isOpen()){
                        sendHandle = nif.openLive(SNAPLEN, PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);  //发送数据包
                    }
                    sendHandle.sendPacket(p);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            } finally {
                try {
            /*        pool.shutdown();   //查询是否任务完成*/
                    if (!pool.awaitTermination(timeOutSeconds, TimeUnit.SECONDS)) {   //启动计时器，如果在指定时间内没完成就返回false.执行资源关闭任务，关闭线程
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
                if ( GetMacByIp.resolvedAddr != null) {
                    res.put(strDstIpAddress, GetMacByIp.resolvedAddr.toString()) ;   //返回解析到的mac地址
                } else {
                    continue;    //如果在指定时间内没解析到mac地址，则认为该主机关机或不存在
                }
            }

        }
        return res;
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
