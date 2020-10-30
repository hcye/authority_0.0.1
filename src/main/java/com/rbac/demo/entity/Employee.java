package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Employee {
    private Integer id;
    private String edepart="";
    private String email="";
    private String ename="";
    private String onjob="";
    private String pingyin="";
    private String pwd="";
    private Collection<OperatRecord> operatRecordsById;
    private Collection<OperatRecord> operatRecordsById_0;

    private Collection<EchangeDevs> exchangeSender;
    private Collection<EchangeDevs> exchangeResiver;



    private Collection<User2Role> user2RolesById;
    private String loginName;
    private SysGroup sysGroupByGroupId;
    private Collection<SysGroup> sysGroupsById;
    private Byte status=0;
    private String sex="男";
    private String txUri="tx_ms_04.gif";
    private Collection<Assert> assertsById;

    public Employee(){

    }

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
    @Column(name = "login_name", nullable = true, length = 64)
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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
    @Column(name = "pwd", nullable = true, length = 255)
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    @Basic
    @Column(name = "tx_uri", nullable = true, length = 64)
    public String getTxUri() {
        return txUri;
    }

    public void setTxUri(String txUri) {
        this.txUri = txUri;
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
        return result;
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
    @OneToMany(mappedBy = "senderFK")
    public Collection<EchangeDevs> getExchangeSender() {
        return exchangeSender;
    }

    public void setExchangeSender(Collection<EchangeDevs> exchangeSender) {
        this.exchangeSender = exchangeSender;
    }
    @OneToMany(mappedBy = "resiverFK")
    public Collection<EchangeDevs> getExchangeResiver() {
        return exchangeResiver;
    }

    public void setExchangeResiver(Collection<EchangeDevs> exchangeResiver) {
        this.exchangeResiver = exchangeResiver;
    }



    @OneToMany(mappedBy = "employeeByUserId")
    public Collection<User2Role> getUser2RolesById() {
        return user2RolesById;
    }

    public void setUser2RolesById(Collection<User2Role> user2RolesById) {
        this.user2RolesById = user2RolesById;
    }


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    public SysGroup getSysGroupByGroupId() {
        return sysGroupByGroupId;
    }

    public void setSysGroupByGroupId(SysGroup sysGroupByGroupId) {
        this.sysGroupByGroupId = sysGroupByGroupId;
    }

    @OneToMany(mappedBy = "employeeByCreatorId")
    public Collection<SysGroup> getSysGroupsById() {
        return sysGroupsById;
    }

    public void setSysGroupsById(Collection<SysGroup> sysGroupsById) {
        this.sysGroupsById = sysGroupsById;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Basic
    @Column(name = "sex", nullable = true, length = 8)
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @OneToMany(mappedBy = "employeeByBorrower")
    public Collection<Assert> getAssertsById() {
        return assertsById;
    }

    public void setAssertsById(Collection<Assert> assertsById) {
        this.assertsById = assertsById;
    }
}
