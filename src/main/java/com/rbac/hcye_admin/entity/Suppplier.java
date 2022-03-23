package com.rbac.hcye_admin.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Suppplier {
    private int id;
    private String supplierName;
    private String remark;
    private Collection<Assert> assertsById;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "supplier_name")
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Basic
    @Column(name = "remark")
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

        Suppplier suppplier = (Suppplier) o;

        if (id != suppplier.id) return false;
        if (supplierName != null ? !supplierName.equals(suppplier.supplierName) : suppplier.supplierName != null)
            return false;
        if (remark != null ? !remark.equals(suppplier.remark) : suppplier.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (supplierName != null ? supplierName.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "suppplierBySupplier")
    public Collection<Assert> getAssertsById() {
        return assertsById;
    }

    public void setAssertsById(Collection<Assert> assertsById) {
        this.assertsById = assertsById;
    }
}
