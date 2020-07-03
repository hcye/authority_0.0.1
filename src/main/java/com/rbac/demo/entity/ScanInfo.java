package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "scan_info", schema = "pcap4j", catalog = "")
public class ScanInfo {
    private Long id;
    private String ip;
    private String mac;
    private Integer ttl;
    private Timestamp upTime;
    private Oui ouiByOuiId;
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ip", nullable = true, length = 64)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Basic
    @Column(name = "mac", nullable = true, length = 64)
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Basic
    @Column(name = "ttl", nullable = true)
    public Integer getTtl() {
        return ttl;
    }

    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

    @Basic
    @Column(name = "up_time", nullable = true)
    public Timestamp getUpTime() {
        return upTime;
    }

    public void setUpTime(Timestamp upTime) {
        this.upTime = upTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScanInfo scanInfo = (ScanInfo) o;

        if (id != null ? !id.equals(scanInfo.id) : scanInfo.id != null) return false;
        if (ip != null ? !ip.equals(scanInfo.ip) : scanInfo.ip != null) return false;
        if (mac != null ? !mac.equals(scanInfo.mac) : scanInfo.mac != null) return false;
        if (ttl != null ? !ttl.equals(scanInfo.ttl) : scanInfo.ttl != null) return false;
        if (upTime != null ? !upTime.equals(scanInfo.upTime) : scanInfo.upTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (mac != null ? mac.hashCode() : 0);
        result = 31 * result + (ttl != null ? ttl.hashCode() : 0);
        result = 31 * result + (upTime != null ? upTime.hashCode() : 0);
        return result;
    }
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "oui_id", referencedColumnName = "id")
    public Oui getOuiByOuiId() {
        return ouiByOuiId;
    }

    public void setOuiByOuiId(Oui ouiByOuiId) {
        this.ouiByOuiId = ouiByOuiId;
    }
}
