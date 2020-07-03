package com.rbac.demo.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Oui {
    private Long id;
    private String macHead;
    private String comInfo;
    private Collection<ScanInfo> scanInfosById;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "mac_head", nullable = true, length = 16)
    public String getMacHead() {
        return macHead;
    }

    public void setMacHead(String macHead) {
        this.macHead = macHead;
    }

    @Basic
    @Column(name = "com_info", nullable = true, length = 255)
    public String getComInfo() {
        return comInfo;
    }

    public void setComInfo(String comInfo) {
        this.comInfo = comInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Oui oui = (Oui) o;

        if (id != null ? !id.equals(oui.id) : oui.id != null) return false;
        if (macHead != null ? !macHead.equals(oui.macHead) : oui.macHead != null) return false;
        if (comInfo != null ? !comInfo.equals(oui.comInfo) : oui.comInfo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (macHead != null ? macHead.hashCode() : 0);
        result = 31 * result + (comInfo != null ? comInfo.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "ouiByOuiId")
    public Collection<ScanInfo> getScanInfosById() {
        return scanInfosById;
    }

    public void setScanInfosById(Collection<ScanInfo> scanInfosById) {
        this.scanInfosById = scanInfosById;
    }
}
