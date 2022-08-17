package com.rbac.hcye_admin.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "asset_action", schema = "mydb", catalog = "")
public class AssetAction {
    public static final String maintain="维修";
    public static final String putin="入库";

    public static final String damage="报废";
    public static final String borrow="借用";
    public static final String retrun_asset="归还";
    public static final String zhuanyi="转移";
    public static final String diaobo="调拨";

    public static final String move="借调";
    private int id;
    private String assetAction;
    private String remark;
    private Collection<AssetRecord> assetRecordsById;

    public AssetAction(){

    }
    public AssetAction(String action){
        this.assetAction=action;
    }
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "asset_action")
    public String getAssetAction() {
        return assetAction;
    }

    public void setAssetAction(String assetAction) {
        this.assetAction = assetAction;
    }

    @Basic
    @Column(name = "remark")
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

        AssetAction that = (AssetAction) o;

        if (id != that.id) return false;
        if (assetAction != null ? !assetAction.equals(that.assetAction) : that.assetAction != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (assetAction != null ? assetAction.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "assetActionByAssetAction")
    public Collection<AssetRecord> getAssetRecordsById() {
        return assetRecordsById;
    }

    public void setAssetRecordsById(Collection<AssetRecord> assetRecordsById) {
        this.assetRecordsById = assetRecordsById;
    }
}
