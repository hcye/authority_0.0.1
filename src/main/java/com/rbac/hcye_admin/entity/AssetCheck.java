package com.rbac.hcye_admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

@Entity
@Table(name = "asset_check", schema = "mydb", catalog = "")
public class AssetCheck {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "status", nullable = false, length = 10)
    private String status;


    @Basic
    @Column(name = "check_date", nullable = true)
    private Date checkDate;

    @Basic
    @Column(name = "remarks", nullable = true, length = 100)
    private String remarks;

    @Basic
    @Column(name = "starter", nullable = true)
    private Integer stater;

    @Basic
    @Column(name = "starter_name", nullable = true, length = 30)
    private String starter_name;

    public int getStater() {
        return stater;
    }

    public void setStater(int stater) {
        this.stater = stater;
    }

    public String getStarter_name() {
        return starter_name;
    }

    public void setStarter_name(String starter_name) {
        this.starter_name = starter_name;
    }

    @OneToMany(mappedBy = "assetCheckByAssetCheck")
    private Collection<AssetCkRecord> assetCkRecordsById;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

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

        AssetCheck that = (AssetCheck) o;

        if (id != that.id) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (checkDate != null ? !checkDate.equals(that.checkDate) : that.checkDate != null) return false;
        if (remarks != null ? !remarks.equals(that.remarks) : that.remarks != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (checkDate != null ? checkDate.hashCode() : 0);
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        return result;
    }

    public Collection<AssetCkRecord> getAssetCkRecordsById() {
        return assetCkRecordsById;
    }

    public void setAssetCkRecordsById(Collection<AssetCkRecord> assetCkRecordsById) {
        this.assetCkRecordsById = assetCkRecordsById;
    }


}
