package com.rbac.demo.entity;

import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sw_iphead_map", schema = "mydb1", catalog = "")
public class SwIpheadMap {
    private int id;
    private String ipHead;
    private String oidRelateCode;
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
    @Column(name = "ip_head", nullable = true, length = 64)
    public String getIpHead() {
        return ipHead;
    }

    public void setIpHead(String ipHead) {
        this.ipHead = ipHead;
    }

    @Basic
    @Column(name = "oid_relate_code", nullable = true, length = 32)
    public String getOidRelateCode() {
        return oidRelateCode;
    }

    public void setOidRelateCode(String oidRelateCode) {
        this.oidRelateCode = oidRelateCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwIpheadMap that = (SwIpheadMap) o;
        return id == that.id &&
                Objects.equals(ipHead, that.ipHead) &&
                Objects.equals(oidRelateCode, that.oidRelateCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ipHead, oidRelateCode);
    }
}
