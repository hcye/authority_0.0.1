package com.rbac.demo.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
public class Role {
    private Integer id;
    private String rname;
    private String description;
    private Byte avalible=1;
    private Collection<Role2Resources> role2ResourcesById;
    private Collection<User2Role> user2RolesById;
    private Timestamp createTime;
    private String creatorName;
    private String authorityCode;
    private Short deleteFlag=0;
    private String remarks;

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
    @Column(name = "rname", nullable = true, length = 32)
    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

        Role role = (Role) o;

        if (id != null ? !id.equals(role.id) : role.id != null) return false;
        if (rname != null ? !rname.equals(role.rname) : role.rname != null) return false;
        if (description != null ? !description.equals(role.description) : role.description != null) return false;
        if (avalible != null ? !avalible.equals(role.avalible) : role.avalible != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (rname != null ? rname.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (avalible != null ? avalible.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "roleByRoleId")
    public Collection<Role2Resources> getRole2ResourcesById() {
        return role2ResourcesById;
    }

    public void setRole2ResourcesById(Collection<Role2Resources> role2ResourcesById) {
        this.role2ResourcesById = role2ResourcesById;
    }

    @OneToMany(mappedBy = "roleByRoleId")
    public Collection<User2Role> getUser2RolesById() {
        return user2RolesById;
    }

    public void setUser2RolesById(Collection<User2Role> user2RolesById) {
        this.user2RolesById = user2RolesById;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "creator_name", nullable = true, length = 64)
    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Basic
    @Column(name = "authority_code", nullable = true, length = 255)
    public String getAuthorityCode() {
        return authorityCode;
    }

    public void setAuthorityCode(String authorityCode) {
        this.authorityCode = authorityCode;
    }

    @Basic
    @Column(name = "delete_flag", nullable = true)
    public Short getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Short deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Basic
    @Column(name = "remarks", nullable = true, length = 255)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
