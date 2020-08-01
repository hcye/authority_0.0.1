package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Resource2Menus {
    private Integer id;
    private Resource resourceByResourceId;
    private Menus menusByMenusId;
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource2Menus that = (Resource2Menus) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "id")
    public Resource getResourceByResourceId() {
        return resourceByResourceId;
    }

    public void setResourceByResourceId(Resource resourceByResourceId) {
        this.resourceByResourceId = resourceByResourceId;
    }
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "menus_id", referencedColumnName = "id")
    public Menus getMenusByMenusId() {
        return menusByMenusId;
    }

    public void setMenusByMenusId(Menus menusByMenusId) {
        this.menusByMenusId = menusByMenusId;
    }
}