package com.rbac.demo.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "sw_firm", schema = "mydb1", catalog = "")
public class SwFirm {
    private int id;
    private String fname;
    private String remark;
    private Collection<Oid> oidsById;
    private Collection<SysSwitch> sysSwitchesById;
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
    @Column(name = "fname", nullable = true, length = 64)
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SwFirm swFirm = (SwFirm) o;

        if (id != swFirm.id) return false;
        if (fname != null ? !fname.equals(swFirm.fname) : swFirm.fname != null) return false;
        if (remark != null ? !remark.equals(swFirm.remark) : swFirm.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (fname != null ? fname.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "swFirmByFirm")
    public Collection<Oid> getOidsById() {
        return oidsById;
    }

    public void setOidsById(Collection<Oid> oidsById) {
        this.oidsById = oidsById;
    }

    @OneToMany(mappedBy = "swFirmByFirm")
    public Collection<SysSwitch> getSysSwitchesById() {
        return sysSwitchesById;
    }

    public void setSysSwitchesById(Collection<SysSwitch> sysSwitchesById) {
        this.sysSwitchesById = sysSwitchesById;
    }
}
