package com.rbac.demo.jpa;

import com.rbac.demo.entity.Resources;
import com.rbac.demo.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaRole extends JpaRepository<Role,Integer> {
    @Query("select rm.resourcesByMenusId from Role2Resources rm where rm.roleByRoleId=:role ")
    List<Resources> findResourcesByRole(@Param("role")Role role);

    @Query("select rm.resourcesByMenusId from Role2Resources rm  where rm.roleByRoleId=:role and rm.resourcesByMenusId.resourcesByParentId is null and rm.resourcesByMenusId.type=:tp")
    List<Resources> findMenusByRole(@Param("tp") String type,@Param("role")Role role);

    @Query("select role from Role role where role.deleteFlag=0")
    List<Role> findAllRole();

    Page<Role> findRolesByAvalible(Byte avalible, Pageable pageable);

    @Query("select role from Role role")
    Page<Role> findRolesByPage(Pageable pageable);

    Page<Role> findRolesByRnameLikeAndAvalible(String rname,Byte avalible,Pageable pageable);

    Page<Role> findRolesByRnameLike(String rname,Pageable pageable);

    Role findRoleByAuthorityCode(String code);

    Role findRoleByRname(String rname);

}
