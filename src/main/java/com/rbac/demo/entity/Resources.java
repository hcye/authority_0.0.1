package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Resources {
    private Integer id;
    private String description;
    private Integer priority;
    private String url;
    private String permission;
    private String type;
    private Resources resourcesByParentId;
    private Collection<Resources> resourcesById;
    private Collection<Role2Resources> role2ResourcesById;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "priority", nullable = true)
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Basic
    @Column(name = "url", nullable = true, length = 255)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "permission", nullable = false, length = 64)
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Basic
    @Column(name = "type", nullable = false, length = 64)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resources resources = (Resources) o;

        if (id != null ? !id.equals(resources.id) : resources.id != null) return false;
        if (description != null ? !description.equals(resources.description) : resources.description != null)
            return false;
        if (priority != null ? !priority.equals(resources.priority) : resources.priority != null) return false;
        if (url != null ? !url.equals(resources.url) : resources.url != null) return false;
        if (permission != null ? !permission.equals(resources.permission) : resources.permission != null) return false;
        if (type != null ? !type.equals(resources.type) : resources.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (permission != null ? permission.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    public Resources getResourcesByParentId() {
        return resourcesByParentId;
    }

    public void setResourcesByParentId(Resources resourcesByParentId) {
        this.resourcesByParentId = resourcesByParentId;
    }

    @OneToMany(mappedBy = "resourcesByParentId")
    public Collection<Resources> getResourcesById() {
        return resourcesById;
    }

    public void setResourcesById(Collection<Resources> resourcesById) {
        this.resourcesById = resourcesById;
    }

    @OneToMany(mappedBy = "resourcesByMenusId")
    public Collection<Role2Resources> getRole2ResourcesById() {
        return role2ResourcesById;
    }

    public void setRole2ResourcesById(Collection<Role2Resources> role2ResourcesById) {
        this.role2ResourcesById = role2ResourcesById;
    }
}
