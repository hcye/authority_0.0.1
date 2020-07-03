package com.rbac.demo.pcap4j;



import com.rbac.demo.entity.Oui;
import com.rbac.demo.entity.ScanInfo;
import com.rbac.demo.jpa.JpaFilter;
import com.rbac.demo.jpa.JpaOui;
import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FiltAllPacket {

    public  void filtAllPacket(JpaOui jpaOui, JpaFilter jpaFilter) throws PcapNativeException,NotOpenException, InterruptedException {
        InetAddress addr ;
        PcapNetworkInterface nif=null;
        List<Oui> ouiList=jpaOui.findAll();
        final boolean[] flag = {false};
        try {
            addr = InetAddress.getByName("10.75.60.35");
            nif = Pcaps.getDevByAddress(addr);
            nif.getAddresses();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
        int snapLen = 65536;
        PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;//混杂模式
        int timeout = 20;
        PcapHandle handle = nif.openLive(snapLen, mode, timeout);
        handle.loop(-1, new PacketListener() {
            @Override
            public void gotPacket(PcapPacket pcapPacket) {
                if(pcapPacket!=null){
                    IpV4Packet packets=pcapPacket.get(IpV4Packet.class);
                    if(packets!=null){
                        if(packets.getHeader().getSrcAddr().getHostAddress().contains("10.75")
                                &&packets.getHeader().getSrcAddr().getHostAddress().contains("192.168")
                                &&!packets.getHeader().getSrcAddr().getHostAddress().equals("10.75.60.35")) {
                            EthernetPacket ethernetPacket=pcapPacket.get(EthernetPacket.class);
                            String srcMacAddr="";
                            String ip="";
                            if(ethernetPacket!=null){
                                srcMacAddr=ethernetPacket.getHeader().getSrcAddr().toString();
                                ip=packets.getHeader().getSrcAddr().getHostAddress();
                            }
                            List<ScanInfo> scanInfos=jpaFilter.findAll();
                            flag[0] =false;
                            for(ScanInfo scanInf:scanInfos){
                                if(scanInf.getMac().equals(srcMacAddr)){

                                    if(!scanInf.getIp().equals(ip)){
                                        scanInf.setIp(ip);
                                        jpaFilter.save(scanInf);
                                    }
                                    flag[0] =true;
                                    break;
                                }
                            }
                            if(!flag[0]){
                                ScanInfo scanInfo=new ScanInfo();
                                scanInfo.setIp(packets.getHeader().getSrcAddr().getHostAddress());
                                scanInfo.setTtl(packets.getHeader().getTtlAsInt());
                                scanInfo.setMac(srcMacAddr);
                                for (Oui oui:ouiList){
                                    if(srcMacAddr.contains(oui.getMacHead())){
                                        scanInfo.setOuiByOuiId(oui);
                                        break;
                                    }
                                }
                                jpaFilter.save(scanInfo);
                            }
                        }
                    }
                }
           /*     if(pcapPacket!=null){

                    Packet packets1=pcapPacket.getPayload();
                    Iterator<Packet> iterator=packets1.iterator();
                    while (iterator.hasNext()){
                        Packet packet1=iterator.next();
                        TcpPacket tcpPacket=packet1.get(TcpPacket.class);

                        if(tcpPacket!=null){
                            TcpPacket.TcpHeader tcpHeader =tcpPacket.getHeader();
                            if(tcpHeader.getDstPort().valueAsInt()!=0){
                                System.out.println(tcpHeader);
                            }
                        }
                    }
                }*/
            }
        });
        handle.close();
    }

}
