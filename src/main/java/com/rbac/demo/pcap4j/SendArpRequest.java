package com.rbac.demo.pcap4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

public class SendArpRequest {
    public SendArpRequest() {
    }
    public String sendArp(String strDstIpAddress, PcapNetworkInterface nif, PcapHandle sendHandle,BuildArpPacket buildArpPacket)  {
        final String srcIpAddress = nif.getAddresses().get(0).getAddress().getHostAddress();
        ExecutorService pool=Executors.newSingleThreadExecutor();
         MacAddress[] resolvedAddr = new MacAddress[1];
        try {

            PacketListener listener = new PacketListener() {
                @Override
                public void gotPacket(PcapPacket pcapPacket) {
                    if (pcapPacket.contains(ArpPacket.class)) {
                        ArpPacket arp = pcapPacket.get(ArpPacket.class);
                        if (arp.getHeader().getDstProtocolAddr().getHostAddress().equals(srcIpAddress) && arp.getHeader().getSrcProtocolAddr().getHostAddress().equals(strDstIpAddress)) {
                            try {
                                resolvedAddr[0] = arp.getHeader().getSrcHardwareAddr();
                                sendHandle.breakLoop();
                            } catch (NotOpenException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            };
            Task t = new Task(sendHandle, listener, nif);

            pool.execute(t);
            Packet p = buildArpPacket.getWholePacket(strDstIpAddress, "");
            sendHandle.sendPacket(p);

        } finally {
            pool.shutdown();
            try {
                pool.awaitTermination(4000, TimeUnit.MILLISECONDS);  //如果200毫秒没收到回包就中断进程
                if (sendHandle != null && sendHandle.isOpen()) {
                    sendHandle.breakLoop();
                }
                if (pool != null && !pool.isShutdown()) {
                    pool.shutdownNow();
                }
            } catch (InterruptedException | NotOpenException e) {
                return "";
            }
            if (resolvedAddr[0] != null) {
                return resolvedAddr[0].toString();
            } else {
                return "";
            }
        }
    }

    private static class Task implements Runnable {

        private PcapHandle handle;
        private PacketListener listener;
        private PcapNetworkInterface nif;

        public Task(PcapHandle handle, PacketListener listener, PcapNetworkInterface nif) {
            this.handle = handle;
            this.listener = listener;
            this.nif = nif;
        }

        @Override
        public void run() {
            try {
                if (!handle.isOpen()) {
                    handle = nif.openLive(65536, PromiscuousMode.PROMISCUOUS, 2000);
                }
                handle.loop(-1, listener);
            } catch (PcapNativeException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
            } catch (NotOpenException e) {
                e.printStackTrace();
            }
        }
    }
}
