package com.rbac.demo;

import com.rbac.demo.pcap4j.*;
import com.rbac.demo.pcap4j.inter.PacketBuilder;
import org.junit.jupiter.api.Test;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class NifTest {
    @Test
    public void t() throws PcapNativeException, UnknownHostException {


//        PacketBuilder builder=new BuildArpPacket(nif);
//        SendPacket sendPacket=new SendPacket();
//        PacketBuilder builder, String srcGateway, String dstip, ExecutorService pool, PcapNetworkInterface nif
        PcapNetworkInterface nif = Pcaps.getDevByAddress(InetAddress.getByName("10.75.60.35"));
        PcapHandle sendHandle = nif.openLive(40000, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 4000);
        PacketBuilder builder=new BuildIcmpPacket(nif);
        String gateway="10.75.60.1";
        String dstip="10.75.10.100/25";
        Set<String> livehost=new HashSet<>();
        SendPacket sendPacket=new SendPacket();
        ExecutorService pool=Executors.newCachedThreadPool();
        boolean b=sendPacket.send(builder,gateway,dstip,pool,nif,sendHandle);




    }


}
