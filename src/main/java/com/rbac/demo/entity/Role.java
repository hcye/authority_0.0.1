package com.rbac.demo.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Role {
    private Integer id;
    private String rname;
    private Collection<Role2Menus> role2MenusById;
    private Collection<User> usersById;
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
    @Column(name = "rname", nullable = true, length = 32)
    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (id != null ? !id.equals(role.id) : role.id != null) return false;
        if (rname != null ? !rname.equals(role.rname) : role.rname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (rname != null ? rname.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "roleByRoleId")
    public Collection<Role2Menus> getRole2MenusById() {
        return role2MenusById;
    }

    public void setRole2MenusById(Collection<Role2Menus> role2MenusById) {
        this.role2MenusById = role2MenusById;
    }

    @OneToMany(mappedBy = "roleByRoleId")
    public Collection<User> getUsersById() {
        return usersById;
    }

    public void setUsersById(Collection<User> usersById) {
        this.usersById = usersById;
    }
}
