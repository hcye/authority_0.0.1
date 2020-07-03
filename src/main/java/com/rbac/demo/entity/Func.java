package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Func {
    private Integer id;
    private String fname;
    private String url;
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

    @Basic
    @Column(name = "fname", nullable = true, length = 32)
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    @Basic
    @Column(name = "url", nullable = true, length = 32)
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

        Func func = (Func) o;

        if (id != null ? !id.equals(func.id) : func.id != null) return false;
        if (fname != null ? !fname.equals(func.fname) : func.fname != null) return false;
        if (url != null ? !url.equals(func.url) : func.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fname != null ? fname.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
    @JsonIgnore //防止json序列化由于外键导致的内存溢出，部署在manytoone上
    @ManyToOne
    @JoinColumn(name = "menus_id", referencedColumnName = "id")
    public Menus getMenusByMenusId() {
        return menusByMenusId;
    }

    public void setMenusByMenusId(Menus menusByMenusId) {
        this.menusByMenusId = menusByMenusId;
    }
}
