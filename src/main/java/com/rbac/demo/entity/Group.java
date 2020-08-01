package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Group {
    private Integer id;
    private String gname;
    private Integer priority;
    private Byte avalible=1;
    private Collection<Employee> employeesById;
    private Group groupByParentId;
    private Collection<Group> groupsById;
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

        Group group = (Group) o;

        if (id != null ? !id.equals(group.id) : group.id != null) return false;
        if (gname != null ? !gname.equals(group.gname) : group.gname != null) return false;
        if (priority != null ? !priority.equals(group.priority) : group.priority != null) return false;
        if (avalible != null ? !avalible.equals(group.avalible) : group.avalible != null) return false;

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

    @OneToMany(mappedBy = "groupByGroupId")
    public Collection<Employee> getEmployeesById() {
        return employeesById;
    }

    public void setEmployeesById(Collection<Employee> employeesById) {
        this.employeesById = employeesById;
    }
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    public Group getGroupByParentId() {
        return groupByParentId;
    }

    public void setGroupByParentId(Group groupByParentId) {
        this.groupByParentId = groupByParentId;
    }

    @OneToMany(mappedBy = "groupByParentId")
    public Collection<Group> getGroupsById() {
        return groupsById;
    }

    public void setGroupsById(Collection<Group> groupsById) {
        this.groupsById = groupsById;
    }
}
