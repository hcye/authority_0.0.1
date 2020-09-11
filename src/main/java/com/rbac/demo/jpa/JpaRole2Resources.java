package com.rbac.demo.jpa;

import com.rbac.demo.entity.Role;
import com.rbac.demo.entity.Role2Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaRole2Resources extends JpaRepository<Role2Resources,Integer> {
    List<Role2Resources> findRole2ResourcesByRoleByRoleId(@Param("role") Role role);
}
