package com.rbac.demo;

import com.rbac.demo.pcap4j.BuildIcmpPacket;
import com.rbac.demo.pcap4j.SendPacket;
import com.rbac.demo.pcap4j.inter.PacketBuilder;
import org.junit.jupiter.api.Test;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.UnknownHostException;

@SpringBootTest
public class TestSendPing {
    @Test
    public void t() throws PcapNativeException, NotOpenException, UnknownHostException {
        SendPacket sendPacket=new SendPacket();
//        PacketBuilder builder, String srcGateway, String dstip, ExecutorService pool, PcapNetworkInterface nif

//        PacketBuilder builder=new BuildIcmpPacket();
//        sendPacket.send();
    }
}
