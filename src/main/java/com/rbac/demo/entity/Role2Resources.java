package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Role2Resources {
    private Integer id;
    private Role roleByRoleId;
    private Resources resourcesByMenusId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role2Resources that = (Role2Resources) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    public Role getRoleByRoleId() {
        return roleByRoleId;
    }

    public void setRoleByRoleId(Role roleByRoleId) {
        this.roleByRoleId = roleByRoleId;
    }
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "menus_id", referencedColumnName = "id")
    public Resources getResourcesByMenusId() {
        return resourcesByMenusId;
    }

    public void setResourcesByMenusId(Resources resourcesByMenusId) {
        this.resourcesByMenusId = resourcesByMenusId;
    }
}
