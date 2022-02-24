package com.rbac.demo.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sw_gateway", schema = "mydb", catalog = "")
public class SwGateway {
    private int id;
    private String gateway;
    private String oidRelateCode;
    private String vlanid;
    private String remark;

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
    @Column(name = "gateway", nullable = true, length = 64)
    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    @Basic
    @Column(name = "oid_relate_code", nullable = true, length = 32)
    public String getOidRelateCode() {
        return oidRelateCode;
    }

    public void setOidRelateCode(String oidRelateCode) {
        this.oidRelateCode = oidRelateCode;
    }

    @Basic
    @Column(name = "vlanid", nullable = true, length = 32)
    public String getVlanid() {
        return vlanid;
    }

    public void setVlanid(String vlanid) {
        this.vlanid = vlanid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwGateway swGateway = (SwGateway) o;
        return id == swGateway.id &&
                Objects.equals(gateway, swGateway.gateway) &&
                Objects.equals(oidRelateCode, swGateway.oidRelateCode) &&
                Objects.equals(vlanid, swGateway.vlanid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gateway, oidRelateCode, vlanid);
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
