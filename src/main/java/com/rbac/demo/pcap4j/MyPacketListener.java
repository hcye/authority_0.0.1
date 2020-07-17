package com.rbac.demo.pcap4j;

import org.pcap4j.core.*;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.IpV4Packet;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyPacketListener {

    private PcapHandle sendHandle;

    public PcapHandle getSendHandle() {
        return sendHandle;
    }

    public void setSendHandle(PcapHandle sendHandle) {
        this.sendHandle = sendHandle;
    }



    public Set<String> lisener(String dstip, PcapNetworkInterface nif) throws PcapNativeException {
        String srcip = nif.getAddresses().get(0).getAddress().getHostAddress();
        final String SNAPLEN_KEY = ArpReply.class.getName() + ".snaplen";
        final int SNAPLEN = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]
        Set<String> set = new HashSet<>();
        sendHandle = nif.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 2000);
        Pcap4JTools tools = new Pcap4JTools();
        List<String> res = tools.getIpRange(dstip);
        if (res == null) {
            return null;
        }
        String startIp = res.get(0);
        int end = Integer.parseInt(res.get(1));
        String[] ips = startIp.split("\\.");
        String ipHead = ips[0] + "." + ips[1] + "." + ips[2] + ".";

        int[] tail = {Integer.parseInt(startIp.split("\\.")[3]), end};
        int scanIpCount = tail[1] - tail[0];
        PacketListener listener = new PacketListener() {
            @Override
            public void gotPacket(PcapPacket pcapPacket) {
                IpV4Packet ipV4Packet = pcapPacket.get(IpV4Packet.class);
                if (pcapPacket.contains(IpV4Packet.class)) {
                    for (int i = tail[0]; i <= tail[1]; i++) {
                        if (ipV4Packet.getHeader().getSrcAddr().getHostAddress().equals(ipHead + i) && ipV4Packet.getHeader().getDstAddr().getHostAddress().equals(srcip)) {
                            set.add(ipHead + i);
                        }
                    }
                } else if (pcapPacket.contains(ArpPacket.class)) {
                    ArpPacket arp = pcapPacket.get(ArpPacket.class);
                    for (int i = tail[0]; i <= tail[1]; i++) {
                        if (arp.getHeader().getDstProtocolAddr().getHostAddress().equals(srcip) && arp.getHeader().getSrcProtocolAddr().getHostAddress().equals(ipHead + i))
                            set.add(ipHead + i);
                    }
                }

            }
        };
        try {
            sendHandle.loop(scanIpCount * 100, listener);
        } catch (InterruptedException e) {
            return set;
        } catch (NotOpenException e) {
            e.printStackTrace();
        }
/*        task t = new task(sendHandle, listener, scanIpCount, nif);
        pool.execute(t);
        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (
                InterruptedException e) {
            sendHandle.close();
            pool.shutdownNow();
        } finally {
            return set;
        }


    }

    private class task implements Runnable {
        private PcapHandle sendHandle;
        private PacketListener listener;
        private final int ipcount;
        private final PcapNetworkInterface nif;

        public task(PcapHandle sendHandle, PacketListener listener, int ipcount, PcapNetworkInterface nif) {
            this.sendHandle = sendHandle;
            this.listener = listener;
            this.ipcount = ipcount;
            this.nif = nif;
        }

        *//**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         *//*
        @Override
        public void run() {
            try {
                if (!sendHandle.isOpen()) {
                    sendHandle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 2000);
                }
                sendHandle.loop(ipcount * 100, listener);
            } catch (PcapNativeException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
            } catch (NotOpenException e) {
                e.printStackTrace();
            }
        }*/
        return set;
    }
}
