package com.rbac.hcye_admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "operat_record", schema = "mydb", catalog = "")
public class OperatRecord {
    private Integer id;
    private String action="";
    private Timestamp actionTime;
    private Employee employeeByDealer;
    private Employee employeeByAssertEmp;
    private Assert assertByAssertAsset;
    private String remarks;

    public OperatRecord(String action,Timestamp actionTime, Employee employeeByDealer,Employee employeeByAssertEmp,Assert assertByAssertAsset,String remarks) {
        this.action=action;
        this.actionTime=actionTime;
        this.employeeByDealer=employeeByDealer;
        this.employeeByAssertEmp=employeeByAssertEmp;
        this.assertByAssertAsset=assertByAssertAsset;
        this.remarks=remarks;
    }
    public OperatRecord(){

    }


    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "action", nullable = true, length = 255)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Basic
    @Column(name = "action_time", nullable = true)
    public Timestamp getActionTime() {
        return actionTime;
    }

    public void setActionTime(Timestamp actionTime) {
        this.actionTime = actionTime;
    }

    @Basic
    @Column(name = "remarks", nullable = true)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperatRecord that = (OperatRecord) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        if (actionTime != null ? !actionTime.equals(that.actionTime) : that.actionTime != null) return false;
        if (remarks != null ? !remarks.equals(that.remarks) : that.remarks != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (actionTime != null ? actionTime.hashCode() : 0);
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "dealer", referencedColumnName = "id")
    public Employee getEmployeeByDealer() {
        return employeeByDealer;
    }

    public void setEmployeeByDealer(Employee employeeByDealer) {
        this.employeeByDealer = employeeByDealer;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "assert_emp", referencedColumnName = "id")
    public Employee getEmployeeByAssertEmp() {
        return employeeByAssertEmp;
    }

    public void setEmployeeByAssertEmp(Employee employeeByAssertEmp) {
        this.employeeByAssertEmp = employeeByAssertEmp;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "assert_asset", referencedColumnName = "id")
    public Assert getAssertByAssertAsset() {
        return assertByAssertAsset;
    }

    public void setAssertByAssertAsset(Assert assertByAssertAsset) {
        this.assertByAssertAsset = assertByAssertAsset;
    }
}
