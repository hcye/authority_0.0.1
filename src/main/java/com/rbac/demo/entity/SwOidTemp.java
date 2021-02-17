package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sw_oid_temp", schema = "mydb1", catalog = "")
public class SwOidTemp {
    private int id;
    private String oidName;
    private String oidTemp;
    private SwFirm swFirmBySwFirm;
    private String remark;

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
    @Column(name = "oid_name", nullable = true, length = 64)
    public String getOidName() {
        return oidName;
    }

    public void setOidName(String oidName) {
        this.oidName = oidName;
    }

    @Basic
    @Column(name = "oid_temp", nullable = true, length = 255)
    public String getOidTemp() {
        return oidTemp;
    }

    public void setOidTemp(String oidTemp) {
        this.oidTemp = oidTemp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwOidTemp swOidTemp = (SwOidTemp) o;
        return id == swOidTemp.id &&
                Objects.equals(oidName, swOidTemp.oidName) &&
                Objects.equals(oidTemp, swOidTemp.oidTemp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, oidName, oidTemp);
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sw_firm", referencedColumnName = "id")
    public SwFirm getSwFirmBySwFirm() {
        return swFirmBySwFirm;
    }

    public void setSwFirmBySwFirm(SwFirm swFirmBySwFirm) {
        this.swFirmBySwFirm = swFirmBySwFirm;
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
