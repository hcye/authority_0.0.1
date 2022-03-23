package com.rbac.hcye_admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "asset_record", schema = "mydb", catalog = "")
public class AssetRecord {
    private int id;
    private Date startTime;
    private String actDetail;
    private Date endTime;
    private String remark;
    private AssetAction assetActionByAssetAction;
    private Assert assertByAsset;

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
    @Column(name = "start_time")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "act_detail")
    public String getActDetail() {
        return actDetail;
    }

    public void setActDetail(String actDetail) {
        this.actDetail = actDetail;
    }

    @Basic
    @Column(name = "end_time")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

        AssetRecord that = (AssetRecord) o;

        if (id != that.id) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        if (actDetail != null ? !actDetail.equals(that.actDetail) : that.actDetail != null) return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (actDetail != null ? actDetail.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "asset_action", referencedColumnName = "id")
    public AssetAction getAssetActionByAssetAction() {
        return assetActionByAssetAction;
    }

    public void setAssetActionByAssetAction(AssetAction assetActionByAssetAction) {
        this.assetActionByAssetAction = assetActionByAssetAction;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "asset", referencedColumnName = "id")
    public Assert getAssertByAsset() {
        return assertByAsset;
    }

    public void setAssertByAsset(Assert assertByAsset) {
        this.assertByAsset = assertByAsset;
    }
}
