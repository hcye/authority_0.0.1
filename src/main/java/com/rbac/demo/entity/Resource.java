package com.rbac.demo.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Resource {
    private Integer id;
    private String rname;
    private String type;
    private Integer priority;
    private String permission;
    private Byte avalible;
    private Collection<Resource2Menus> resource2MenusById;
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

    @Basic
    @Column(name = "type", nullable = true, length = 255)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
    @Column(name = "permission", nullable = true, length = 255)
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
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

        Resource resource = (Resource) o;

        if (id != null ? !id.equals(resource.id) : resource.id != null) return false;
        if (rname != null ? !rname.equals(resource.rname) : resource.rname != null) return false;
        if (type != null ? !type.equals(resource.type) : resource.type != null) return false;
        if (priority != null ? !priority.equals(resource.priority) : resource.priority != null) return false;
        if (permission != null ? !permission.equals(resource.permission) : resource.permission != null) return false;
        if (avalible != null ? !avalible.equals(resource.avalible) : resource.avalible != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (rname != null ? rname.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (permission != null ? permission.hashCode() : 0);
        result = 31 * result + (avalible != null ? avalible.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "resourceByResourceId")
    public Collection<Resource2Menus> getResource2MenusById() {
        return resource2MenusById;
    }

    public void setResource2MenusById(Collection<Resource2Menus> resource2MenusById) {
        this.resource2MenusById = resource2MenusById;
    }
}
