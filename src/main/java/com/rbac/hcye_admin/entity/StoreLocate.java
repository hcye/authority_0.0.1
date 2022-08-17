package com.rbac.hcye_admin.entity;

import javax.persistence.*;

@Entity
@Table(name = "store_locate", schema = "mydb", catalog = "")
public class StoreLocate {
    private int id;
    private String locate;
    private String remarks;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "locate", nullable = false, length = 100)
    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    @Basic
    @Column(name = "remarks", nullable = true, length = 100)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoreLocate that = (StoreLocate) o;

        if (id != that.id) return false;
        if (locate != null ? !locate.equals(that.locate) : that.locate != null) return false;
        if (remarks != null ? !remarks.equals(that.remarks) : that.remarks != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (locate != null ? locate.hashCode() : 0);
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        return result;
    }
}
