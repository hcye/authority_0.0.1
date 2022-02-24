package com.rbac.demo.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "state_code_proj", schema = "mydb", catalog = "")
public class StateCodeProj {
    private int id;
    private String projType;
    private String projName;
    private Collection<StateCode> stateCodesById;
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
    @Column(name = "proj_type", nullable = true, length = 255)
    public String getProjType() {
        return projType;
    }

    public void setProjType(String projType) {
        this.projType = projType;
    }

    @Basic
    @Column(name = "proj_name", nullable = true, length = 255)
    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateCodeProj that = (StateCodeProj) o;

        if (id != that.id) return false;
        if (projType != null ? !projType.equals(that.projType) : that.projType != null) return false;
        if (projName != null ? !projName.equals(that.projName) : that.projName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (projType != null ? projType.hashCode() : 0);
        result = 31 * result + (projName != null ? projName.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "stateCodeProjByProjId")
    public Collection<StateCode> getStateCodesById() {
        return stateCodesById;
    }

    public void setStateCodesById(Collection<StateCode> stateCodesById) {
        this.stateCodesById = stateCodesById;
    }
}
