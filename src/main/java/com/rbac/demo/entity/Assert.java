package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

@Entity
public class Assert {
    private Integer id;
    private String aname;
    private String assestnum;
    private String labelpic;
    private String model;
    private Date putintime;
    private String remarks;
    private String snnum;
    private String workless;
    private String price;
    private Date brotime;
    private Date damagetime;
    private Date returntime;
    private String assertPic;
    private Employee employeeByBorrower;
    private AssertType assertTypeByAssertType;
    private Collection<OperatRecord> operatRecordsById;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "aname", nullable = true, length = 128)
    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    @Basic
    @Column(name = "assestnum", nullable = true, length = 64)
    public String getAssestnum() {
        return assestnum;
    }

    public void setAssestnum(String assestnum) {
        this.assestnum = assestnum;
    }

    @Basic
    @Column(name = "labelpic", nullable = true, length = 128)
    public String getLabelpic() {
        return labelpic;
    }

    public void setLabelpic(String labelpic) {
        this.labelpic = labelpic;
    }

    @Basic
    @Column(name = "model", nullable = true, length = 255)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Basic
    @Column(name = "putintime", nullable = true)
    public Date getPutintime() {
        return putintime;
    }

    public void setPutintime(Date putintime) {
        this.putintime = putintime;
    }

    @Basic
    @Column(name = "remarks", nullable = true, length = 511)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Basic
    @Column(name = "snnum", nullable = true, length = 64)
    public String getSnnum() {
        return snnum;
    }

    public void setSnnum(String snnum) {
        this.snnum = snnum;
    }

    @Basic
    @Column(name = "workless", nullable = true, length = 2)
    public String getWorkless() {
        return workless;
    }

    public void setWorkless(String workless) {
        this.workless = workless;
    }

    @Basic
    @Column(name = "price", nullable = true, length = 64)
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Basic
    @Column(name = "brotime", nullable = true)
    public Date getBrotime() {
        return brotime;
    }

    public void setBrotime(Date brotime) {
        this.brotime = brotime;
    }

    @Basic
    @Column(name = "damagetime", nullable = true)
    public Date getDamagetime() {
        return damagetime;
    }

    public void setDamagetime(Date damagetime) {
        this.damagetime = damagetime;
    }

    @Basic
    @Column(name = "returntime", nullable = true)
    public Date getReturntime() {
        return returntime;
    }

    public void setReturntime(Date returntime) {
        this.returntime = returntime;
    }

    @Basic
    @Column(name = "assert_pic", nullable = true, length = 255)
    public String getAssertPic() {
        return assertPic;
    }

    public void setAssertPic(String assertPic) {
        this.assertPic = assertPic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Assert anAssert = (Assert) o;

        if (id != null ? !id.equals(anAssert.id) : anAssert.id != null) return false;
        if (aname != null ? !aname.equals(anAssert.aname) : anAssert.aname != null) return false;
        if (assestnum != null ? !assestnum.equals(anAssert.assestnum) : anAssert.assestnum != null) return false;
        if (labelpic != null ? !labelpic.equals(anAssert.labelpic) : anAssert.labelpic != null) return false;
        if (model != null ? !model.equals(anAssert.model) : anAssert.model != null) return false;
        if (putintime != null ? !putintime.equals(anAssert.putintime) : anAssert.putintime != null) return false;
        if (remarks != null ? !remarks.equals(anAssert.remarks) : anAssert.remarks != null) return false;
        if (snnum != null ? !snnum.equals(anAssert.snnum) : anAssert.snnum != null) return false;
        if (workless != null ? !workless.equals(anAssert.workless) : anAssert.workless != null) return false;
        if (price != null ? !price.equals(anAssert.price) : anAssert.price != null) return false;
        if (brotime != null ? !brotime.equals(anAssert.brotime) : anAssert.brotime != null) return false;
        if (damagetime != null ? !damagetime.equals(anAssert.damagetime) : anAssert.damagetime != null) return false;
        if (returntime != null ? !returntime.equals(anAssert.returntime) : anAssert.returntime != null) return false;
        if (assertPic != null ? !assertPic.equals(anAssert.assertPic) : anAssert.assertPic != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (aname != null ? aname.hashCode() : 0);
        result = 31 * result + (assestnum != null ? assestnum.hashCode() : 0);
        result = 31 * result + (labelpic != null ? labelpic.hashCode() : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (putintime != null ? putintime.hashCode() : 0);
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        result = 31 * result + (snnum != null ? snnum.hashCode() : 0);
        result = 31 * result + (workless != null ? workless.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (brotime != null ? brotime.hashCode() : 0);
        result = 31 * result + (damagetime != null ? damagetime.hashCode() : 0);
        result = 31 * result + (returntime != null ? returntime.hashCode() : 0);
        result = 31 * result + (assertPic != null ? assertPic.hashCode() : 0);
        return result;
    }
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "borrower", referencedColumnName = "id")
    public Employee getEmployeeByBorrower() {
        return employeeByBorrower;
    }

    public void setEmployeeByBorrower(Employee employeeByBorrower) {
        this.employeeByBorrower = employeeByBorrower;
    }
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "assert_type", referencedColumnName = "id")
    public AssertType getAssertTypeByAssertType() {
        return assertTypeByAssertType;
    }

    public void setAssertTypeByAssertType(AssertType assertTypeByAssertType) {
        this.assertTypeByAssertType = assertTypeByAssertType;
    }

    @OneToMany(mappedBy = "assertByAssertAsset")
    public Collection<OperatRecord> getOperatRecordsById() {
        return operatRecordsById;
    }

    public void setOperatRecordsById(Collection<OperatRecord> operatRecordsById) {
        this.operatRecordsById = operatRecordsById;
    }
}
