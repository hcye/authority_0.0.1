package com.rbac.hcye_admin.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "state_url_set", schema = "mydb", catalog = "")
public class StateUrlSet {
    private int id;
    private String setName;
    private String remark;
    private Collection<StateUrl> stateUrlsById;
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
    @Column(name = "set_name", nullable = true, length = 255)
    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
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

        StateUrlSet that = (StateUrlSet) o;

        if (id != that.id) return false;
        if (setName != null ? !setName.equals(that.setName) : that.setName != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (setName != null ? setName.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "stateUrlSetBySetFk")
    public Collection<StateUrl> getStateUrlsById() {
        return stateUrlsById;
    }

    public void setStateUrlsById(Collection<StateUrl> stateUrlsById) {
        this.stateUrlsById = stateUrlsById;
    }
}
