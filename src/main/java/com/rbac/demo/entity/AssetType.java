package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "asset_type", schema = "mydb1", catalog = "")
public class AssetType {
    private Integer id;
    private String typeName;
    private String assetCode="";
    private String remarks="";
    private Timestamp createTime;
    private String creator="";
    private Collection<Assert> assertsById;
    private Collection<DevType> devTypesById;
    private String permiCode;

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
    @Column(name = "type_name", nullable = true, length = 64)
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Basic
    @Column(name = "asset_code", nullable = true, length = 255)
    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    @Basic
    @Column(name = "remarks", nullable = true, length = 255)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "creator", nullable = true, length = 255)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssetType assetType = (AssetType) o;

        if (id != null ? !id.equals(assetType.id) : assetType.id != null) return false;
        if (typeName != null ? !typeName.equals(assetType.typeName) : assetType.typeName != null) return false;
        if (assetCode != null ? !assetCode.equals(assetType.assetCode) : assetType.assetCode != null) return false;
        if (remarks != null ? !remarks.equals(assetType.remarks) : assetType.remarks != null) return false;
        if (createTime != null ? !createTime.equals(assetType.createTime) : assetType.createTime != null) return false;
        if (creator != null ? !creator.equals(assetType.creator) : assetType.creator != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + (assetCode != null ? assetCode.hashCode() : 0);
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "assetTypeByAssertType")
    public Collection<Assert> getAssertsById() {
        return assertsById;
    }

    public void setAssertsById(Collection<Assert> assertsById) {
        this.assertsById = assertsById;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "assetTypeByAssertTypeId")
    public Collection<DevType> getDevTypesById() {
        return devTypesById;
    }

    public void setDevTypesById(Collection<DevType> devTypesById) {
        this.devTypesById = devTypesById;
    }

    @Basic
    @Column(name = "permi_code", nullable = true, length = 255)
    public String getPermiCode() {
        return permiCode;
    }

    public void setPermiCode(String permiCode) {
        this.permiCode = permiCode;
    }
}
