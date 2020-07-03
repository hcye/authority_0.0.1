package com.rbac.demo;

import com.rbac.demo.entity.Oui;
import com.rbac.demo.jpa.JpaFilter;
import com.rbac.demo.jpa.JpaOui;
import com.rbac.demo.pcap4j.FiltAllPacket;
import org.junit.jupiter.api.Test;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Pcap4JTets {
    @Autowired
    private JpaOui jpaOui;
    @Autowired
    private JpaFilter jpaFilter;
    @Test
    public void test()  {
        FiltAllPacket filtAllPacket=new FiltAllPacket();
        try {
            filtAllPacket.filtAllPacket(jpaOui,jpaFilter);
        } catch (PcapNativeException e) {
            e.printStackTrace();
        } catch (NotOpenException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
