package com.rbac.demo.jpa;

import com.rbac.demo.entity.Menus;
import com.rbac.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaRole extends JpaRepository<Role,Integer> {
    @Query("select rm.menusByMenusId from Role2Menus rm where rm.roleByRoleId=:role ")
    List<Menus> findMenusByRole(@Param("role")Role role);
}
