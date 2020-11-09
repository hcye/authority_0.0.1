package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "sys_switch", schema = "mydb1", catalog = "")
public class SysSwitch {
    private int id;
    private String level;
    private String ipAddr;
    private String snmpComm;
    private String location;
    private String label;
    private String remark;
    private String online;
    private String blockUp;
    private String sname;
    private String cascadePort;
    private SwFirm swFirmByFirm;
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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
    @Column(name = "sname", nullable = false, length = 32)
    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
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

        SysSwitch sysSwitch = (SysSwitch) o;

        if (id != sysSwitch.id) return false;
        if (level != null ? !level.equals(sysSwitch.level) : sysSwitch.level != null) return false;
        if (ipAddr != null ? !ipAddr.equals(sysSwitch.ipAddr) : sysSwitch.ipAddr != null) return false;
        if (snmpComm != null ? !snmpComm.equals(sysSwitch.snmpComm) : sysSwitch.snmpComm != null) return false;
        if (location != null ? !location.equals(sysSwitch.location) : sysSwitch.location != null) return false;
        if (label != null ? !label.equals(sysSwitch.label) : sysSwitch.label != null) return false;
        if (remark != null ? !remark.equals(sysSwitch.remark) : sysSwitch.remark != null) return false;
        if (online != null ? !online.equals(sysSwitch.online) : sysSwitch.online != null) return false;
        if (blockUp != null ? !blockUp.equals(sysSwitch.blockUp) : sysSwitch.blockUp != null) return false;
        if (sname != null ? !sname.equals(sysSwitch.sname) : sysSwitch.sname != null) return false;
        if (cascadePort != null ? !cascadePort.equals(sysSwitch.cascadePort) : sysSwitch.cascadePort != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (ipAddr != null ? ipAddr.hashCode() : 0);
        result = 31 * result + (snmpComm != null ? snmpComm.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (online != null ? online.hashCode() : 0);
        result = 31 * result + (blockUp != null ? blockUp.hashCode() : 0);
        result = 31 * result + (sname != null ? sname.hashCode() : 0);
        result = 31 * result + (cascadePort != null ? cascadePort.hashCode() : 0);
        return result;
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
