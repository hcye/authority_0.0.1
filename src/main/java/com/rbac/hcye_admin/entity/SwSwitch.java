package com.rbac.hcye_admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sw_switch", schema = "mydb", catalog = "")
public class SwSwitch {
    private int id;
    private String level;
    private String ipAddr;
    private String snmpComm;
    private String location;
    private String label;
    private String remark;
    private String online;
    private String blockUp;
    private String cascadePort;
    private SwFirm swFirmByFirm;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "level", nullable = true, length = 64)
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Basic
    @Column(name = "ip_addr", nullable = true, length = 64)
    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    @Basic
    @Column(name = "snmp_comm", nullable = true, length = 64)
    public String getSnmpComm() {
        return snmpComm;
    }

    public void setSnmpComm(String snmpComm) {
        this.snmpComm = snmpComm;
    }

    @Basic
    @Column(name = "location", nullable = true, length = 255)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "label", nullable = true, length = 255)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "online", nullable = true, length = 8)
    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    @Basic
    @Column(name = "block_up", nullable = true, length = 8)
    public String getBlockUp() {
        return blockUp;
    }

    public void setBlockUp(String blockUp) {
        this.blockUp = blockUp;
    }

    @Basic
    @Column(name = "cascade_port", nullable = true, length = 255)
    public String getCascadePort() {
        return cascadePort;
    }

    public void setCascadePort(String cascadePort) {
        this.cascadePort = cascadePort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwSwitch swSwitch = (SwSwitch) o;
        return id == swSwitch.id &&
                Objects.equals(level, swSwitch.level) &&
                Objects.equals(ipAddr, swSwitch.ipAddr) &&
                Objects.equals(snmpComm, swSwitch.snmpComm) &&
                Objects.equals(location, swSwitch.location) &&
                Objects.equals(label, swSwitch.label) &&
                Objects.equals(remark, swSwitch.remark) &&
                Objects.equals(online, swSwitch.online) &&
                Objects.equals(blockUp, swSwitch.blockUp) &&
                Objects.equals(cascadePort, swSwitch.cascadePort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, level, ipAddr, snmpComm, location, label, remark, online, blockUp, cascadePort);
    }
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "firm", referencedColumnName = "id")
    public SwFirm getSwFirmByFirm() {
        return swFirmByFirm;
    }

    public void setSwFirmByFirm(SwFirm swFirmByFirm) {
        this.swFirmByFirm = swFirmByFirm;
    }
}
