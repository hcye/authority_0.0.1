package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "dev_type", schema = "mydb1", catalog = "")
public class DevType {
    private int id;
    private String devName;
    private String desc;
    private Timestamp createTime;
    private String creator;
    private AssertType assertTypeByAssertTypeId;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "dev_name", nullable = true, length = 255)
    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevType devType = (DevType) o;
        return id == devType.id &&
                Objects.equals(devName, devType.devName) &&
                Objects.equals(desc, devType.desc) &&
                Objects.equals(createTime, devType.createTime) &&
                Objects.equals(creator, devType.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, devName, desc, createTime, creator);
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "assert_type_id", referencedColumnName = "id")
    public AssertType getAssertTypeByAssertTypeId() {
        return assertTypeByAssertTypeId;
    }

    public void setAssertTypeByAssertTypeId(AssertType assertTypeByAssertTypeId) {
        this.assertTypeByAssertTypeId = assertTypeByAssertTypeId;
    }
}
