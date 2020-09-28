package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "sys_group", schema = "mydb1", catalog = "")
public class SysGroup {
    private Integer id;
    private String gname;
    private Integer priority;
    private Byte avalible=1;
    private Collection<Employee> employeesById;
    private SysGroup sysGroupByParentId;
    private Collection<SysGroup> sysGroupsById;
    private Timestamp creatTime=new Timestamp(new Date().getTime());
    private Employee employeeByCreatorId;
    private Byte deleteFlag=0;
    private String leader="";

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
    @Column(name = "gname", nullable = true, length = 32)
    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    @Basic
    @Column(name = "priority", nullable = true)
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Basic
    @Column(name = "avalible", nullable = true)
    public Byte getAvalible() {
        return avalible;
    }

    public void setAvalible(Byte avalible) {
        this.avalible = avalible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysGroup sysGroup = (SysGroup) o;

        if (id != null ? !id.equals(sysGroup.id) : sysGroup.id != null) return false;
        if (gname != null ? !gname.equals(sysGroup.gname) : sysGroup.gname != null) return false;
        if (priority != null ? !priority.equals(sysGroup.priority) : sysGroup.priority != null) return false;
        if (avalible != null ? !avalible.equals(sysGroup.avalible) : sysGroup.avalible != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (gname != null ? gname.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (avalible != null ? avalible.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "sysGroupByGroupId")
    public Collection<Employee> getEmployeesById() {
        return employeesById;
    }

    public void setEmployeesById(Collection<Employee> employeesById) {
        this.employeesById = employeesById;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    public SysGroup getSysGroupByParentId() {
        return sysGroupByParentId;
    }

    public void setSysGroupByParentId(SysGroup sysGroupByParentId) {
        this.sysGroupByParentId = sysGroupByParentId;
    }

    @OneToMany(mappedBy = "sysGroupByParentId")
    public Collection<SysGroup> getSysGroupsById() {
        return sysGroupsById;
    }

    public void setSysGroupsById(Collection<SysGroup> sysGroupsById) {
        this.sysGroupsById = sysGroupsById;
    }

    @Basic
    @Column(name = "creat_time", nullable = true)
    public Timestamp getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Timestamp creatTime) {
        this.creatTime = creatTime;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    public Employee getEmployeeByCreatorId() {
        return employeeByCreatorId;
    }

    public void setEmployeeByCreatorId(Employee employeeByCreatorId) {
        this.employeeByCreatorId = employeeByCreatorId;
    }

    @Basic
    @Column(name = "delete_flag", nullable = true)
    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Basic
    @Column(name = "leader", nullable = true, length = 32)
    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }
}
