package com.rbac.demo.entity;

import javax.persistence.*;

@Entity
public class Adinfo {
    private Integer id;
    private String dc;
    private String adip;
    private String domainadminname;
    private String domainadminpwd;

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
    @Column(name = "dc", nullable = true, length = 32)
    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    @Basic
    @Column(name = "adip", nullable = true, length = 64)
    public String getAdip() {
        return adip;
    }

    public void setAdip(String adip) {
        this.adip = adip;
    }

    @Basic
    @Column(name = "domainadminname", nullable = true, length = 64)
    public String getDomainadminname() {
        return domainadminname;
    }

    public void setDomainadminname(String domainadminname) {
        this.domainadminname = domainadminname;
    }

    @Basic
    @Column(name = "domainadminpwd", nullable = true, length = 64)
    public String getDomainadminpwd() {
        return domainadminpwd;
    }

    public void setDomainadminpwd(String domainadminpwd) {
        this.domainadminpwd = domainadminpwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Adinfo adinfo = (Adinfo) o;

        if (id != null ? !id.equals(adinfo.id) : adinfo.id != null) return false;
        if (dc != null ? !dc.equals(adinfo.dc) : adinfo.dc != null) return false;
        if (adip != null ? !adip.equals(adinfo.adip) : adinfo.adip != null) return false;
        if (domainadminname != null ? !domainadminname.equals(adinfo.domainadminname) : adinfo.domainadminname != null)
            return false;
        if (domainadminpwd != null ? !domainadminpwd.equals(adinfo.domainadminpwd) : adinfo.domainadminpwd != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dc != null ? dc.hashCode() : 0);
        result = 31 * result + (adip != null ? adip.hashCode() : 0);
        result = 31 * result + (domainadminname != null ? domainadminname.hashCode() : 0);
        result = 31 * result + (domainadminpwd != null ? domainadminpwd.hashCode() : 0);
        return result;
    }
}
