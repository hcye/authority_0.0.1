package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Menus {
    private Integer id;
    private String menuName;
    private String url;
    private Collection<Func> funcsById;
    private Menus menusByFatherId;
    private Collection<Menus> menusById;
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
    @Column(name = "menu_name", nullable = true, length = 32)
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @Basic
    @Column(name = "url", nullable = true, length = 64)
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
        if (menuName != null ? !menuName.equals(menus.menuName) : menus.menuName != null) return false;
        if (url != null ? !url.equals(menus.url) : menus.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (menuName != null ? menuName.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "menusByMenusId")
    public Collection<Func> getFuncsById() {
        return funcsById;
    }

    public void setFuncsById(Collection<Func> funcsById) {
        this.funcsById = funcsById;
    }
    @JsonIgnore //防止json序列化由于外键导致的内存溢出，部署在manytoone上
    @ManyToOne
    @JoinColumn(name = "father_id", referencedColumnName = "id")
    public Menus getMenusByFatherId() {
        return menusByFatherId;
    }

    public void setMenusByFatherId(Menus menusByFatherId) {
        this.menusByFatherId = menusByFatherId;
    }

    @OneToMany(mappedBy = "menusByFatherId")
    public Collection<Menus> getMenusById() {
        return menusById;
    }

    public void setMenusById(Collection<Menus> menusById) {
        this.menusById = menusById;
    }

    @OneToMany(mappedBy = "menusByMenuId")
    public Collection<Role2Menus> getRole2MenusById() {
        return role2MenusById;
    }

    public void setRole2MenusById(Collection<Role2Menus> role2MenusById) {
        this.role2MenusById = role2MenusById;
    }
}
