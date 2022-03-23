package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.Resources;
import com.rbac.hcye_admin.entity.Role;
import com.rbac.hcye_admin.entity.Role2Resources;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaRole2Resources extends JpaRepository<Role2Resources,Integer> {
    List<Role2Resources> findRole2ResourcesByRoleByRoleId( Role role);


    List<Role2Resources> findRole2ResourcesByResourcesByMenusId(Resources resources);
}
