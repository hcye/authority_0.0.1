package com.rbac.hcye_admin.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "sw_firm", schema = "mydb", catalog = "")
public class SwFirm {
    private int id;
    private String fname;
    private String remark;
    private Collection<SwOidTemp> swOidTempsById;
    private Collection<SwSwitch> swSwitchesById;
    private Collection<Oid> oidsById;


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
        return id == swFirm.id &&
                Objects.equals(fname, swFirm.fname) &&
                Objects.equals(remark, swFirm.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fname, remark);
    }

    @OneToMany(mappedBy = "swFirmBySwFirm")
    public Collection<SwOidTemp> getSwOidTempsById() {
        return swOidTempsById;
    }

    public void setSwOidTempsById(Collection<SwOidTemp> swOidTempsById) {
        this.swOidTempsById = swOidTempsById;
    }

    @OneToMany(mappedBy = "swFirmByFirm")
    public Collection<SwSwitch> getSwSwitchesById() {
        return swSwitchesById;
    }

    public void setSwSwitchesById(Collection<SwSwitch> swSwitchesById) {
        this.swSwitchesById = swSwitchesById;
    }

    @OneToMany(mappedBy = "swFirmByFirm")
    public Collection<Oid> getOidsById() {
        return oidsById;
    }

    public void setOidsById(Collection<Oid> oidsById) {
        this.oidsById = oidsById;
    }


}
