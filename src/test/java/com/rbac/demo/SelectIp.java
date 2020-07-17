package com.rbac.demo;

import com.rbac.demo.entity.ScanInfo;
import com.rbac.demo.jpa.JpaFilter;
import com.rbac.demo.pcap4j.MyPacketListener;
import org.junit.jupiter.api.Test;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SelectIp {
    @Autowired
    private JpaFilter jpaFilter;
    @Test
    public void test() throws PcapNativeException, UnknownHostException, NotOpenException {
     /*   MyPacketListener listener=new MyPacketListener();
        Set<String> list=listener.lisener("10.75.60.35","10.75.60.248");
        try {
            listener.getPool().awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            listener.getSendHandle().breakLoop();
            if(listener.getSendHandle().isOpen()){
                listener.getSendHandle().close();
            }
            listener.getPool().shutdownNow();
        }
        for (String s:list){
            System.out.println(s);
        }*/
    }
}
