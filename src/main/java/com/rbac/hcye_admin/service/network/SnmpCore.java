package com.rbac.hcye_admin.service.network;

import com.rbac.hcye_admin.entity.SwGateway;
import com.rbac.hcye_admin.entity.SwOidTemp;
import com.rbac.hcye_admin.entity.SwSwitch;
import com.rbac.hcye_admin.jpa.JpaGateway;
import com.rbac.hcye_admin.jpa.JpaSwOidTemp;
import com.rbac.hcye_admin.jpa.JpaSwSwitch;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:获得本机的信息
 */
@Component
public class SnmpCore {
    @Autowired
    private JpaSwOidTemp jpaSwOidTemp;
    @Autowired
    private JpaSwSwitch jpaSwSwitch;
    @Autowired
    private JpaGateway jpaGateway;
    //指定开始和结束字符 截取字符串
    public String getSubString(String info, String subStringStart, String subStringEnd) {
        if (subStringStart.equals("")) {
            return info;
        }
        StringBuffer sb = new StringBuffer();
        sb = sb.append(info);
        int start = 0;
        int end = 0;
        start = sb.indexOf(subStringStart) + 1;
        end = sb.indexOf(subStringEnd);
        if (start != -1 && end != -1) {
            return sb.substring(start, end);
        } else {
            return "null";
        }
    }

    public String getSubStr2(String src) {
        int start = 0;
        StringBuffer sb = new StringBuffer();
        sb = sb.append(src);
        start = sb.indexOf("=") + 2;
        return sb.substring(start);
    }

    //把16进制的mac地址转换成10进制的mac地址，并拼接成oid
    public String binaryConvert(String mac,String oidBase) {
        BigInteger bigInteger = null;
        String macAddr10 = "";
        for (int i = 2; i <= mac.length(); i = i + 3) {
            try {
                bigInteger = new BigInteger(mac.substring(i - 2, i), 16);
            } catch (NumberFormatException e) {
                // TODO: handle exception
                return "null";
            }
            if (i < 17) {
                macAddr10 = macAddr10 + bigInteger.toString() + ".";
            } else {
                macAddr10 = macAddr10 + bigInteger.toString();
                if (macAddr10.length() < 16) {
                    return "null";
                }
            }
        }
        return oidBase + macAddr10;
    }
/*
* mac查ip采用遍历网段方式，效率低下，耗时长。mac地址格式是 aa:bb:cc:dd:11:22
* */
    public String getIpByMAC(String mac) throws Exception {
        List<SwGateway> gateways=jpaGateway.findAll();
        for (SwGateway swGateway:gateways){
            String gateway=swGateway.getGateway();
            String[] str=gateway.split("/");
            String gateway_ip=str[0];
            String[] gateway_ips=gateway_ip.split("\\.");
            String mask=str[1];
            String gateway_ip_head=gateway_ips[0]+"."+gateway_ips[1]+"."+gateway_ips[2]+".";
            int gateway_ip_tail=Integer.parseInt(gateway_ips[3]);
            int ip_num= (int) Math.pow(2,32-Integer.parseInt(mask));
            for (int i=gateway_ip_tail;i<gateway_ip_tail+ip_num-3;i++){
                String ip=gateway_ip_head+i;
                if (mac.equals(getMACByIP(ip))){
                    return ip;
                }
            }
        }
        return "";
    }

    //通过ip查询主机mac地址,效率高
    public String getMACByIP(String ip) throws Exception {
        List<SwGateway> gateways=jpaGateway.findAll();
        String[] ips=ip.split("\\.");
        String ip_head=ips[0]+"."+ips[1]+"."+ips[2]+".";
        String ip_tail1=ips[3];
        int int_ip_tail=Integer.parseInt(ip_tail1);
        SwGateway targetGateway=null;
        for (SwGateway swGateway:gateways) {
            String gateway = swGateway.getGateway();
            if (gateway.contains(ip_head)) {
                String[] strings = gateway.split("/");
                String mask = strings[1];
                int gateway_tail = Integer.parseInt(strings[0].split("\\.")[3]);
                int intmask = Integer.parseInt(mask);
                int subnet_width = (int) Math.pow(2, 32 - intmask);
                int subnet_num = 256 / subnet_width;
                List<int[]> subnets = new ArrayList<>();
                for (int i = 0; i < subnet_num; i++) {
                    int[] b = {i * subnet_width, (i + 1) * subnet_width - 1};
                    subnets.add(b);
                }
                for (int[] a : subnets) {
                    if (gateway_tail >= a[0] && gateway_tail < a[1] && int_ip_tail >= a[0] && int_ip_tail <= a[1]) {
                        targetGateway = swGateway;
                        break;
                    }
                }
            }
        }
                //输入的ip在数据库的vlan范围内，可以执行查询
                if(targetGateway==null){
                    return "";
                }
                SwSwitch swSwitch=jpaSwSwitch.findSwSwitchesByLevel("核心").get(0);

                String vlanRelateNum = null;
                if(targetGateway.getOidRelateCode() == null){
                    getIpMapInteger();
                    vlanRelateNum = targetGateway.getOidRelateCode();
                }else {
                    vlanRelateNum=targetGateway.getOidRelateCode();
                }
                //System.out.println(vlanRelateNum);
                SwOidTemp oidTemp = jpaSwOidTemp.findSwOidTempByOidNameAndAndSwFirmBySwFirm("arp",swSwitch.getSwFirmByFirm());
                String oid_tmp = oidTemp.getOidTemp();
                oid_tmp=oid_tmp.replaceAll("\\[p1\\]", vlanRelateNum);
                oid_tmp=oid_tmp.replaceAll("\\[p2\\]", ip);
                String arpRes=this.getInfo(swSwitch.getIpAddr(),swSwitch.getSnmpComm(),oid_tmp,"","");
                if(arpRes.equals("null")||arpRes.contains("noSuch")){
                        return "";
                }
      //  1.3.6.1.4.1.2011.5.25.123.1.17.1.11.30.192.168.160.30.1.32 = noSuchInstance
//              return arpRes.substring(arpRes.indexOf("=")+1);
                return arpRes.split(" = ")[1];
    }
/*
    本函数针对华为交换机
    p2是ip,p1是vlan数字,本函数计算p1	.1.3.6.1.4.1.2011.5.25.123.1.17.1.11.[p1].[p2].1.32
    函数耗时长，占用系统资源多

 */
    public void getIpMapInteger() throws Exception {
        List<SwSwitch> swSwitches=jpaSwSwitch.findSwSwitchesByLevel("核心");
        if(swSwitches.size()==0){
            return;
        }
        SwSwitch swSwitch=swSwitches.get(0);
        String swip=swSwitch.getIpAddr();
        String comm=swSwitch.getSnmpComm();
        List<SwGateway> gateways=jpaGateway.findAll();
        List<Integer> re=new ArrayList<>();
        for (SwGateway swGateway:gateways){
            String code=swGateway.getOidRelateCode();
            if(code!=null&&!code.equals("")){
                continue;
            }

            boolean flag=false;
            String[] strs=swGateway.getGateway().split("/");
            String gateway_ip=strs[0];
            String mask=strs[1];
            int intm=Integer.parseInt(mask);
            //只接受24到32位的掩码
            if(intm<24||intm>=32){
                throw new Exception("掩码不合法");
            }

            int intmask = Integer.parseInt(mask);
            int subnet_width= (int) Math.pow(2,32-intmask);
            int subnet_num=256/subnet_width;
            List<int[]> subnets=new ArrayList<>();
            for (int i=0;i<subnet_num;i++){
                int[] b={i*subnet_width,(i+1)*subnet_width-1};
                subnets.add(b);
            }
            String ress[]=gateway_ip.split("\\.");
            int ip_start=Integer.parseInt(ress[3]);
            int ip_tail=0;
            for (int[] a:subnets){
                if(ip_start>=a[0]&&ip_start<a[1]){
                    ip_tail=a[1];
                    ip_start=a[0];
                    break;
                }
            }
            String ip_head=ress[0]+"."+ress[1]+"."+ress[2]+".";
            for(int i=0;i<4000;i++){
                //跳过已经扫描到的数字加快扫描速度
                for (Integer k:re){
                    if(k==i){
                        continue;
                    }
                }
                //扫描到关联字段后进入下一个vlan
                if(flag==true){
                    break;
                }
                SwOidTemp swOidTemp =jpaSwOidTemp.findSwOidTempByOidNameAndAndSwFirmBySwFirm("arp",swSwitch.getSwFirmByFirm());
                //替换网关占位符
                String oid_tmp=swOidTemp.getOidTemp();
                oid_tmp=oid_tmp.replaceAll("\\[p1\\]",i+"");
                String oid_head=oid_tmp.substring(0,oid_tmp.indexOf("[p2]"));
                String oid_tail=oid_tmp.substring(oid_tmp.indexOf("[p2]")+"[p2]".length());
                for (int j=ip_start;j<ip_tail;j++) {
                    String ip=ip_head+j;
                    String oid=oid_head+ip+oid_tail;
                   // System.out.println(oid);
                    String arpRes=this.getInfo(swip,comm,oid,"","");
                   // System.out.println(arpRes);
                    if(arpRes==null){
                        continue;
                    }
                    String[] res=arpRes.split("=");
                    if(res.length<2){
                        continue;
                    }
                    if(res[1].equals("null")||res[1].contains("noSuch")){
                        continue;
                    }else {
                        swGateway.setOidRelateCode(i+"");
                        jpaGateway.save(swGateway);
                        re.add(i);
                        flag=true;
                        break;
                    }
                }
            }
        }

    }

    //通过mac地址查询 主机所在接口  返回 ip,port 字符串
    public String searchPort(String mac) {
        String result;
        String level="接入";
        List<SwSwitch> swSwitches=jpaSwSwitch.findSwSwitchesByLevel(level);
        for (int i = 0; i < swSwitches.size(); i++) {
            SwSwitch cusw=swSwitches.get(i);
            SwOidTemp oidTemp=jpaSwOidTemp.findSwOidTempByOidNameAndAndSwFirmBySwFirm("mac",cusw.getSwFirmByFirm());
            String oid_head=oidTemp.getOidTemp().replaceAll("\\[p1\\]","");
            String oid = this.binaryConvert(mac,oid_head);
            result = this.getInfo(cusw.getIpAddr(),cusw.getSnmpComm(), oid, "", "");
            if(result==null||result.equals("null")){
                continue;
            }
            if(!result.contains("noSuch")){
                String cascadePort=cusw.getCascadePort();
                String[] ports;
                String port=result.split(" = ")[1] ;
                //剔除级联口扫描到的数据
                if(cascadePort.contains(",")){
                     ports=cascadePort.split(",");
                     for (String p:ports){
                      if(p.equals(port)){
                          continue;
                      }
                     }
                }else {
                    if(port.equals(cascadePort)){
                        continue;
                    }
                }
                //获得结果
                return cusw.getIpAddr()+","+port;
            }else {
                continue;
            }
        }
        return "";
    }

    //获得主机vlan号
    public String getVlanID(String targetIP) {
        String result = this.getInfo(targetIP, "yhc4133", "1.3.6.1.2.1.1.5.0", "", "");
//        return result.substring(26);
        return result;
    }

    //oid查询函数
    public String getInfo(String targetIP, String communityName, String OID, String subStringStart, String subStringEnd) {
        try {
            CommunityTarget myTarget = new CommunityTarget();
            // 定义远程主机的地址
            // Address deviceAdd =
            // GenericAddress.parse("udp:192.168.1.233/161");
            // 定义本机的地址
            Address localAdd = GenericAddress.parse("udp:" + targetIP + "/161");//被监控主机
            // 设定远程主机的地址
            // myTarget.setAddress(deviceAdd);
            // 设定本地主机的地址
            myTarget.setAddress(localAdd);
            myTarget.setCommunity(new OctetString(communityName));
            myTarget.setRetries(2);
            myTarget.setTimeout(5 * 60);
            myTarget.setVersion(SnmpConstants.version2c);
            TransportMapping transport = new DefaultUdpTransportMapping();// 设定传输协议为UDP
            transport.listen();
            Snmp protocol = new Snmp(transport);
            PDU request = new PDU();
            if (OID == null) {
                return null;
            }
            request.add(new VariableBinding(new OID(OID)));
            request.setType(PDU.GET);
            ResponseEvent responseEvent = protocol.send(request, myTarget);
            if(responseEvent==null){
                return null;
            }
            PDU response = responseEvent.getResponse();
            if (response != null) {
                VariableBinding vb1 = response.get(0);
                transport.close();
                return this.getSubString(vb1.toString(), subStringStart, subStringEnd);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
    }
}

