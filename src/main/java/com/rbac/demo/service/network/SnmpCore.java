package com.rbac.demo.service.network;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:获得本机的信息
 */
public class SnmpCore {
    //指定开始和结束字符 截取字符串
    public String getSubString(String info, String subStringStart, String subStringEnd) {
        if (subStringStart.equals("")) {
            //System.out.println("------0x-----"+info);
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
    public String binaryConvert(String source) {
        String baseString = "1.3.6.1.2.1.17.4.3.1.2.";
        BigInteger bigInteger = null;
        String macAddr10 = "";
        for (int i = 2; i <= source.length(); i = i + 3) {
            try {
                bigInteger = new BigInteger(source.substring(i - 2, i), 16);
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
//		System.out.println(baseString+macAddr10);
        return baseString + macAddr10;
    }

    public String getIpByMAC(String mac, String vlanid) {
        Map<String, String> arpOidMap = new HashMap();
        //把ip分成4个字符串方便调用
        arpOidMap.put("2", "42");
        arpOidMap.put("1", "43");
        arpOidMap.put("3", "44");
        arpOidMap.put("4", "45");
        arpOidMap.put("5", "46");
        arpOidMap.put("6", "47");
        arpOidMap.put("7", "48");
        arpOidMap.put("8", "49");
        arpOidMap.put("10", "50");
        arpOidMap.put("12", "52");
        //华为交换机 arp的snmp信息 oid树的前部分
        String ip;
        String arpOidBase = ".1.3.6.1.4.1.2011.5.25.123.1.17.1.11.";
        String oid;
        String arpResult;
        for (int i = 0; i < 254; i++) {
            ip = "172.16." + vlanid + "." + i;
            oid = arpOidBase + arpOidMap.get(vlanid) + "." + ip + ".1.32";
            arpResult = this.getInfo("192.168.100.3", "yhc41335", oid, "", "");
            if (arpResult == null) {
                continue;
            }
            String[] results = arpResult.split("=");
            if (results != null) {
                if (results[1].trim().equals(mac)) {
                    String[] r = results[0].split("\\.");
                    String dstIP = r[15] + "." + r[16] + "." + r[17] + "." + r[18];
                    return dstIP;
                }
            }
        }
        return null;
    }

    //通过ip查询主机mac地址
    public String getMACByIP(String ip) {

        Map<String, String> arpOidMap = new HashMap();
        //把ip分成4个字符串方便调用
        String[] ipS = ip.split("\\.");
        //
        arpOidMap.put("2", "42");
        arpOidMap.put("1", "43");
        arpOidMap.put("3", "44");
        arpOidMap.put("4", "45");
        arpOidMap.put("5", "46");
        arpOidMap.put("6", "47");
        arpOidMap.put("7", "48");
        arpOidMap.put("8", "49");
        arpOidMap.put("10", "50");
        arpOidMap.put("12", "52");
        //华为交换机 arp的snmp信息 oid树的前部分
        String arpOidBase = ".1.3.6.1.4.1.2011.5.25.123.1.17.1.11.";
        String oid = arpOidBase + arpOidMap.get(ipS[2]) + "." + ip + ".1.32";
        String arpResult = this.getInfo("192.168.101.100", "yhc41335", oid, "", "");
        int macIndex = arpResult.indexOf("=");
        //取得snmp查询结果并截取=号后的结果
        arpResult = arpResult.substring(macIndex + 2);
        return arpResult;
    }

    //通过mac地址查询 主机所在接口
    public String[] searchPort(String src) {
        String[] results = new String[2];
        String result = null;
        String subStr = null;
        if (src.equals("null")) {
            return null;
        }

        String oid = this.binaryConvert(src);
        //	System.out.println(oid);
        if (oid.equals("null")) {
            return null;
        }
        String[] allSwitchIP = {"192.168.100.121", "192.168.100.122",
                "192.168.100.123", "192.168.100.124", "192.168.100.125",
                "192.168.100.126", "192.168.100.127", "192.168.100.128",
                "192.168.100.129", "192.168.100.130", "192.168.100.131",
                "192.168.100.132", "192.168.100.133", "192.168.100.134",
                "192.168.100.135", "192.168.100.136", "192.168.100.137", "192.168.100.138", "192.168.100.139", "192.168.100.140"};
        for (int i = 0; i < allSwitchIP.length; i++) {
            result = this.getInfo(allSwitchIP[i], "yhc41335", oid, "", "");
            if (allSwitchIP[i] != "192.168.100.132" && allSwitchIP[i] != "192.168.100.133" &&
                    allSwitchIP[i] != "192.168.100.134" && allSwitchIP[i] != "192.168.100.135"
                    && allSwitchIP[i] != "192.168.100.136") {
                if (result != "null" && result.length() < 52) {
                    subStr = this.getSubStr2(result);
                    if (result.length() < 51) {
                        if (!subStr.equals("52")) {
                            results[0] = subStr;
                            results[1] = allSwitchIP[i];
                            return results;
                        }
                    }
                }
            } else {
                if (result != "null" && result.length() < 52) {
                    subStr = this.getSubStr2(result);
                    if (result.length() < 51) {
                        if (!subStr.equals("48")) {
                            results[0] = subStr;
                            results[1] = allSwitchIP[i];
                            return results;
                        }
                    }
                }
            }
        }
        return null;
    }

    //获得主机vlan号
    public String getVlanID(String targetIP) {
        String result = this.getInfo(targetIP, "yhc41335", "1.3.6.1.2.1.1.5.0", "", "");
        return result.substring(26);
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

