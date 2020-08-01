package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Employee {
    private Integer id;
    private String edepart;
    private String email;
    private String ename;
    private String onjob="1";
    private String pingyin;
    private String pwd;
    private Byte locked=0;
    private String salt="hcye";
    private Group groupByGroupId;
    private Collection<OperatRecord> operatRecordsById_dealer;
    private Collection<OperatRecord> operatRecordsById_assertEmp;
    private Collection<Othermeans> othermeansById_borrower;
    private Collection<User2Role> user2RolesById;
    private Collection<OperatRecord> operatRecordsById;
    private Collection<OperatRecord> operatRecordsById_0;
    private Collection<Othermeans> othermeansById;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "edepart", nullable = true, length = 64)
    public String getEdepart() {
        return edepart;
    }

    public void setEdepart(String edepart) {
        this.edepart = edepart;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 128)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "ename", nullable = true, length = 64)
    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    @Basic
    @Column(name = "onjob", nullable = true, length = 4)
    public String getOnjob() {
        return onjob;
    }

    public void setOnjob(String onjob) {
        this.onjob = onjob;
    }

    @Basic
    @Column(name = "pingyin", nullable = true, length = 128)
    public String getPingyin() {
        return pingyin;
    }

    public void setPingyin(String pingyin) {
        this.pingyin = pingyin;
    }

    @Basic
    @Column(name = "pwd", nullable = true, length = 64)
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Basic
    @Column(name = "locked", nullable = true)
    public Byte getLocked() {
        return locked;
    }

    public void setLocked(Byte locked) {
        this.locked = locked;
    }

    @Basic
    @Column(name = "salt", nullable = true, length = 64)
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (id != null ? !id.equals(employee.id) : employee.id != null) return false;
        if (edepart != null ? !edepart.equals(employee.edepart) : employee.edepart != null) return false;
        if (email != null ? !email.equals(employee.email) : employee.email != null) return false;
        if (ename != null ? !ename.equals(employee.ename) : employee.ename != null) return false;
        if (onjob != null ? !onjob.equals(employee.onjob) : employee.onjob != null) return false;
        if (pingyin != null ? !pingyin.equals(employee.pingyin) : employee.pingyin != null) return false;
        if (pwd != null ? !pwd.equals(employee.pwd) : employee.pwd != null) return false;
        if (locked != null ? !locked.equals(employee.locked) : employee.locked != null) return false;
        if (salt != null ? !salt.equals(employee.salt) : employee.salt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (edepart != null ? edepart.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (ename != null ? ename.hashCode() : 0);
        result = 31 * result + (onjob != null ? onjob.hashCode() : 0);
        result = 31 * result + (pingyin != null ? pingyin.hashCode() : 0);
        result = 31 * result + (pwd != null ? pwd.hashCode() : 0);
        result = 31 * result + (locked != null ? locked.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    public Group getGroupByGroupId() {
        return groupByGroupId;
    }

    public void setGroupByGroupId(Group groupByGroupId) {
        this.groupByGroupId = groupByGroupId;
    }

    @OneToMany(mappedBy = "employeeByDealer")
    public Collection<OperatRecord> getOperatRecordsById_dealer() {
        return operatRecordsById_dealer;
    }

    public void setOperatRecordsById_dealer(Collection<OperatRecord> operatRecordsById_dealer) {
        this.operatRecordsById_dealer = operatRecordsById_dealer;
    }

    @OneToMany(mappedBy = "employeeByAssertEmp")
    public Collection<OperatRecord> getOperatRecordsById_assertEmp() {
        return operatRecordsById_assertEmp;
    }

    public void setOperatRecordsById_assertEmp(Collection<OperatRecord> operatRecordsById_assertEmp) {
        this.operatRecordsById_assertEmp = operatRecordsById_assertEmp;
    }

    @OneToMany(mappedBy = "employeeByBorrower")
    public Collection<Othermeans> getOthermeansById_borrower() {
        return othermeansById_borrower;
    }

    public void setOthermeansById_borrower(Collection<Othermeans> othermeansById_borrower) {
        this.othermeansById_borrower = othermeansById_borrower;
    }

    @OneToMany(mappedBy = "employeeByUserId")
    public Collection<User2Role> getUser2RolesById() {
        return user2RolesById;
    }

    public void setUser2RolesById(Collection<User2Role> user2RolesById) {
        this.user2RolesById = user2RolesById;
    }

    @OneToMany(mappedBy = "employeeByDealer")
    public Collection<OperatRecord> getOperatRecordsById() {
        return operatRecordsById;
    }

    public void setOperatRecordsById(Collection<OperatRecord> operatRecordsById) {
        this.operatRecordsById = operatRecordsById;
    }

    @OneToMany(mappedBy = "employeeByAssertEmp")
    public Collection<OperatRecord> getOperatRecordsById_0() {
        return operatRecordsById_0;
    }

    public void setOperatRecordsById_0(Collection<OperatRecord> operatRecordsById_0) {
        this.operatRecordsById_0 = operatRecordsById_0;
    }

    @OneToMany(mappedBy = "employeeByBorrower")
    public Collection<Othermeans> getOthermeansById() {
        return othermeansById;
    }

    public void setOthermeansById(Collection<Othermeans> othermeansById) {
        this.othermeansById = othermeansById;
    }
}
