package com.rbac.hcye_admin.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "check_history", schema = "mydb", catalog = "")
public class CheckHistory {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "check_time", nullable = true)
    private Date checkTime;
    @Basic
    @Column(name = "check_records_ids", nullable = true, length = 500)
    private String checkRecordsIds;
    @Basic
    @Column(name = "check_kong_ids", nullable = true, length = 500)
    private String checkKongIds;
    @Basic
    @Column(name = "check_operator", nullable = true)
    private Integer checkOperator;
    @Basic
    @Column(name = "operator_name", nullable = true, length = 20)
    private String operatorName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckRecordsIds() {
        return checkRecordsIds;
    }

    public void setCheckRecordsIds(String checkRecordsIds) {
        this.checkRecordsIds = checkRecordsIds;
    }

    public String getCheckKongIds() {
        return checkKongIds;
    }

    public void setCheckKongIds(String checkKongIds) {
        this.checkKongIds = checkKongIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CheckHistory that = (CheckHistory) o;

        if (id != that.id) return false;
        if (checkTime != null ? !checkTime.equals(that.checkTime) : that.checkTime != null) return false;
        if (checkRecordsIds != null ? !checkRecordsIds.equals(that.checkRecordsIds) : that.checkRecordsIds != null)
            return false;
        if (checkKongIds != null ? !checkKongIds.equals(that.checkKongIds) : that.checkKongIds != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (checkTime != null ? checkTime.hashCode() : 0);
        result = 31 * result + (checkRecordsIds != null ? checkRecordsIds.hashCode() : 0);
        result = 31 * result + (checkKongIds != null ? checkKongIds.hashCode() : 0);
        return result;
    }

    public Integer getCheckOperator() {
        return checkOperator;
    }

    public void setCheckOperator(Integer checkOperator) {
        this.checkOperator = checkOperator;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}
