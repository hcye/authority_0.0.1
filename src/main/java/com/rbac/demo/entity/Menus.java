package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Menus {
    private Integer id;
    private String description;
    private Integer priority;
    private String url;
    private Menus menusByParentId;
    private Collection<Menus> menusById;
    private Collection<Resource2Menus> resource2MenusById;
    private Collection<Role2Menus> role2MenusById;
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
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    @Column(name = "url", nullable = true, length = 255)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Menus menus = (Menus) o;

        if (id != null ? !id.equals(menus.id) : menus.id != null) return false;
        if (description != null ? !description.equals(menus.description) : menus.description != null) return false;
        if (priority != null ? !priority.equals(menus.priority) : menus.priority != null) return false;
        if (url != null ? !url.equals(menus.url) : menus.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    public Menus getMenusByParentId() {
        return menusByParentId;
    }

    public void setMenusByParentId(Menus menusByParentId) {
        this.menusByParentId = menusByParentId;
    }

    @OneToMany(mappedBy = "menusByParentId")
    public Collection<Menus> getMenusById() {
        return menusById;
    }

    public void setMenusById(Collection<Menus> menusById) {
        this.menusById = menusById;
    }

    @OneToMany(mappedBy = "menusByMenusId")
    public Collection<Resource2Menus> getResource2MenusById() {
        return resource2MenusById;
    }

    public void setResource2MenusById(Collection<Resource2Menus> resource2MenusById) {
        this.resource2MenusById = resource2MenusById;
    }

    @OneToMany(mappedBy = "menusByMenusId")
    public Collection<Role2Menus> getRole2MenusById() {
        return role2MenusById;
    }

    public void setRole2MenusById(Collection<Role2Menus> role2MenusById) {
        this.role2MenusById = role2MenusById;
    }
}
