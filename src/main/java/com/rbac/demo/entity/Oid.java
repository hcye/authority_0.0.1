package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Oid {
    private int id;
    private String oidHead;
    private String oidTail;
    private String content;
    private String remark;
    private SwFirm swFirmByFirm;
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
    @Column(name = "oid_head", nullable = true, length = 128)
    public String getOidHead() {
        return oidHead;
    }

    public void setOidHead(String oidHead) {
        this.oidHead = oidHead;
    }

    @Basic
    @Column(name = "oid_tail", nullable = true, length = 128)
    public String getOidTail() {
        return oidTail;
    }

    public void setOidTail(String oidTail) {
        this.oidTail = oidTail;
    }

    @Basic
    @Column(name = "content", nullable = true, length = 128)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Oid oid = (Oid) o;

        if (id != oid.id) return false;
        if (oidHead != null ? !oidHead.equals(oid.oidHead) : oid.oidHead != null) return false;
        if (oidTail != null ? !oidTail.equals(oid.oidTail) : oid.oidTail != null) return false;
        if (content != null ? !content.equals(oid.content) : oid.content != null) return false;
        if (remark != null ? !remark.equals(oid.remark) : oid.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (oidHead != null ? oidHead.hashCode() : 0);
        result = 31 * result + (oidTail != null ? oidTail.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "firm", referencedColumnName = "id")
    public SwFirm getSwFirmByFirm() {
        return swFirmByFirm;
    }

    public void setSwFirmByFirm(SwFirm swFirmByFirm) {
        this.swFirmByFirm = swFirmByFirm;
    }
}
