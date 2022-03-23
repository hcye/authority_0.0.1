package com.rbac.hcye_admin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "state_url", schema = "mydb", catalog = "")
public class StateUrl {
    private int id;
    private String stateUrl;
    private StateUrlSet stateUrlSetBySetFk;
    private String urlName;
    private String remark;
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
    @Column(name = "state_url", nullable = true, length = 255)
    public String getStateUrl() {
        return stateUrl;
    }

    public void setStateUrl(String stateUrl) {
        this.stateUrl = stateUrl;
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

        StateUrl stateUrl1 = (StateUrl) o;

        if (id != stateUrl1.id) return false;
        if (stateUrl != null ? !stateUrl.equals(stateUrl1.stateUrl) : stateUrl1.stateUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (stateUrl != null ? stateUrl.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "set_fk", referencedColumnName = "id")
    public StateUrlSet getStateUrlSetBySetFk() {
        return stateUrlSetBySetFk;
    }

    public void setStateUrlSetBySetFk(StateUrlSet stateUrlSetBySetFk) {
        this.stateUrlSetBySetFk = stateUrlSetBySetFk;
    }

    @Basic
    @Column(name = "url_name", nullable = true, length = 255)
    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }
}
