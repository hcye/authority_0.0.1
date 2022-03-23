package com.rbac.hcye_admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "state_code", schema = "mydb", catalog = "")
public class StateCode {
    private int id;
    private String repoName;
    private String repoUrl;
    private Date addTime;
    private StateCodeProj stateCodeProjByProjId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "repo_name", nullable = true, length = 255)
    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    @Basic
    @Column(name = "repo_url", nullable = true, length = 255)
    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    @Basic
    @Column(name = "add_time", nullable = true)
    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateCode stateCode = (StateCode) o;

        if (id != stateCode.id) return false;
        if (repoName != null ? !repoName.equals(stateCode.repoName) : stateCode.repoName != null) return false;
        if (repoUrl != null ? !repoUrl.equals(stateCode.repoUrl) : stateCode.repoUrl != null) return false;
        if (addTime != null ? !addTime.equals(stateCode.addTime) : stateCode.addTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (repoName != null ? repoName.hashCode() : 0);
        result = 31 * result + (repoUrl != null ? repoUrl.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        return result;
    }
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "proj_id", referencedColumnName = "id")
    public StateCodeProj getStateCodeProjByProjId() {
        return stateCodeProjByProjId;
    }

    public void setStateCodeProjByProjId(StateCodeProj stateCodeProjByProjId) {
        this.stateCodeProjByProjId = stateCodeProjByProjId;
    }
}
