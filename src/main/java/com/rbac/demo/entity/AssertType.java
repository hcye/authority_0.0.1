package com.rbac.demo.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "assert_type", schema = "mydb1")
public class AssertType {
    private Integer id;
    private String typeName;
    private Collection<Assert> assertsById;
    private String assetCode;
    private String desc;
    private Timestamp createTime;
    private String creator;
    private Collection<DevType> devTypesById;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssertType that = (AssertType) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (typeName != null ? !typeName.equals(that.typeName) : that.typeName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "assertTypeByAssertType")
    public Collection<Assert> getAssertsById() {
        return assertsById;
    }

    public void setAssertsById(Collection<Assert> assertsById) {
        this.assertsById = assertsById;
    }

    @Basic
    @Column(name = "asset_code", nullable = false, length = 255)
    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    @Basic
    @Column(name = "desc", nullable = true, length = 255)
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    @OneToMany(mappedBy = "assertTypeByAssertTypeId")
    public Collection<DevType> getDevTypesById() {
        return devTypesById;
    }

    public void setDevTypesById(Collection<DevType> devTypesById) {
        this.devTypesById = devTypesById;
    }
}
