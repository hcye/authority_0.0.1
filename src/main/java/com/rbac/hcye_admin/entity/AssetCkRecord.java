package com.rbac.hcye_admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "asset_ck_record", schema = "mydb", catalog = "")
public class AssetCkRecord {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "asset_name", nullable = true, length = 30)
    private String assetName;
    @Basic
    @Column(name = "asset_id", nullable = true)
    private Integer assetId;
    @Basic
    @Column(name = "asset_code", nullable = true, length = 50)
    private String assetCode;
    @Basic
    @Column(name = "check_remark", nullable = true, length = 100)
    private String checkRemark;

    @ManyToOne
    @JoinColumn(name = "asset_check", referencedColumnName = "id")
    private AssetCheck assetCheckByAssetCheck;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getCheckRemark() {
        return checkRemark;
    }

    public void setCheckRemark(String checkRemark) {
        this.checkRemark = checkRemark;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssetCkRecord that = (AssetCkRecord) o;

        if (id != that.id) return false;
        if (assetName != null ? !assetName.equals(that.assetName) : that.assetName != null) return false;
        if (assetId != null ? !assetId.equals(that.assetId) : that.assetId != null) return false;
        if (assetCode != null ? !assetCode.equals(that.assetCode) : that.assetCode != null) return false;
        if (checkRemark != null ? !checkRemark.equals(that.checkRemark) : that.checkRemark != null) return false;


        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (assetName != null ? assetName.hashCode() : 0);
        result = 31 * result + (assetId != null ? assetId.hashCode() : 0);
        result = 31 * result + (assetCode != null ? assetCode.hashCode() : 0);
        result = 31 * result + (checkRemark != null ? checkRemark.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    public AssetCheck getAssetCheckByAssetCheck() {
        return assetCheckByAssetCheck;
    }

    public void setAssetCheckByAssetCheck(AssetCheck assetCheckByAssetCheck) {
        this.assetCheckByAssetCheck = assetCheckByAssetCheck;
    }
}
