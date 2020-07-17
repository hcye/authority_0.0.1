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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * 过滤并拦截局域网内特定厂商设备
 *
 *
 * */

public class FiltAllPacket {
    public  void filtAllPacket(JpaOui jpaOui, JpaFilter jpaFilter,String netCardIp) throws PcapNativeException{
        List<ExecutorService> pools=new ArrayList<>();   //存储线程
        List<String> macs=new ArrayList<>();    //存储已经拦截的设备的mac地址，防止重复拦截
        InetAddress addr ;
        PcapNetworkInterface nif=null;
        List<Oui> ouiList=jpaOui.findAll();     //IEEE组织厂商mac地址对应表，已经读入数据库。通过jpa读出存入list备用
        final boolean[] flag = {false,false};   //标志
        try {
            addr = InetAddress.getByName(netCardIp);   //通过ip获得本机监听网卡
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
        ExecutorService pool= Executors.newSingleThreadExecutor();

        PacketListener listener = new PacketListener() {    //开启监听loop
            @Override
            public void gotPacket(PcapPacket pcapPacket) {   //监听数据包
                if (pcapPacket != null) {
                    IpV4Packet packets = pcapPacket.get(IpV4Packet.class);
                    EthernetPacket ethernetPacket = pcapPacket.get(EthernetPacket.class);
                    if (packets != null) {
                        /**
                         * 如果数据包是IPv4包，则继续。
                         *
                         * */
                        String hostAddress = packets.getHeader().getSrcAddr().getHostAddress();   //获得捕获数据包的源地址
                        if (hostAddress.contains("10.75") && !hostAddress.equals(netCardIp)) {  //过滤数据包
                            String srcMacAddr = "";
                            String ip = "";
                            if (ethernetPacket.getHeader() != null) {
                                /**
                                 * 如果数据包的ethernet层不为空，则获取数据链路层的源mac地址
                                 *
                                 * */
                                srcMacAddr = ethernetPacket.getHeader().getSrcAddr().toString();
                                ip = hostAddress;
                                System.out.println(srcMacAddr + "---" + ip);


                            }
                            List<ScanInfo> scanInfos = jpaFilter.findAll();  //获得所有存储的数据包对象
                            flag[0] = false;
                            for (ScanInfo scanInf : scanInfos) {
                                flag[1] = false;
                                if (scanInf.getMac().equals(srcMacAddr)) {
                                    if (scanInf.getOuiByOuiId() == null) {
                                        /**
                                         * 如果过滤到的数据包的mac地址在oui表里面找不到对应项则跳过
                                         *
                                         * */
                                        continue;
                                    }
                                    String info = scanInf.getOuiByOuiId().getComInfo();
                                    if (info.contains("HUAWEI") || info.contains("TP-LINK")
                                            || info.contains("vivo") || info.contains("Netcore") || info.contains("360")
                                            || info.contains("Tenda") || info.contains("Xiaomi") || info.contains("H3C")) {
                                        /**
                                         * 过滤华为、tp、腾达等设备
                                         *
                                         * */
                                        System.out.println(srcMacAddr + "---" + ip + "---" + info);

                                        for (String string : macs) {
                                            if (scanInf.getMac().equals(string)) {
                                                /**
                                                 * 判断捕获的数据包是否已经被拦截，如果已经被拦截则跳过
                                                 *
                                                 * */
                                                flag[1] = true;
                                                break;
                                            }
                                        }
                                        if (flag[1]) {
                                            continue;
                                        }
                                            /*    ArpReply reply = new ArpReply();
                                                Map<String, String> map = new HashMap<>();
                                                map.put(ip, scanInf.getMac());
                                                System.out.println("扫描到"+scanInf.getMac()+ip);*/
                                        macs.add(scanInf.getIp() + "-" + scanInf.getMac() + "-" + scanInf.getOuiByOuiId().getComInfo());
                                        /*         try {

                                         *//**
                                         * 使用createThread方法创建arp欺骗进程，map存储arp欺骗目标,无法跨网段欺骗
                                         *
                                         * *//*
                                            System.out.println("开始进程"+scanInf.getMac()+ip);
                                            ExecutorService pool=reply.createThread(map,"6c:4b:90:8b:42:03", "10.75.60.1", "10.75.60.35");
                                            macs.add(scanInf.getMac());    //
                                            pools.add(pool);
                                        } catch (UnknownHostException e) {
                                            e.printStackTrace();
                                        } catch (PcapNativeException e) {
                                                e.printStackTrace();
                                            }*/
                                    }
                                    if (!scanInf.getIp().equals(ip)) {
                                        scanInf.setIp(ip);
                                        jpaFilter.save(scanInf);
                                    }
                                    flag[0] = true;
                                    break;
                                }
                            }
                            if (!flag[0]) {

                                /**
                                 * 如果扫描到的数据包没被存入数据库，则构建新对象存入数据库
                                 *
                                 * */
                                ScanInfo scanInfo = new ScanInfo();
                                scanInfo.setIp(packets.getHeader().getSrcAddr().getHostAddress());
                                scanInfo.setTtl(packets.getHeader().getTtlAsInt());
                                scanInfo.setMac(srcMacAddr);
                                for (Oui oui : ouiList) {
                                    if (srcMacAddr.contains(oui.getMacHead())) {
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
        };
        task t=new task(handle,listener);
        pool.execute(t);
        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            handle.close();
            pool.shutdownNow();
        }finally {
            for (String s:macs){
                System.out.println(s);
            }
        }
    }
    private class task implements Runnable{
        private PcapHandle handle;
        private PacketListener listener;
        public task(PcapHandle handle,PacketListener listener){
            this.handle=handle;
            this.listener=listener;
        }
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            try {
                handle.loop(-1,listener);
            } catch (PcapNativeException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {

            } catch (NotOpenException e) {
                e.printStackTrace();
            }
        }
    }
}