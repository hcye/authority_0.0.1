package com.rbac.demo.jpa;

import com.rbac.demo.entity.Resources;
import com.rbac.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaResources extends JpaRepository<Resources,Integer> {

    @Query("select res from Resources res where res.description=:des")
    Resources findResourcesByDescription(@Param("des") String des);
    @Query("select res from Resources res where res.permission=:permission")
    Resources findResourcesByPermission(@Param("permission") String permission);


}
