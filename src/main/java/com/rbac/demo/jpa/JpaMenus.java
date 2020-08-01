package com.rbac.demo.jpa;

import com.rbac.demo.entity.Menus;
import com.rbac.demo.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaMenus extends JpaRepository<Menus,Integer> {
    @Query("select rm.resourceByResourceId from Resource2Menus rm where rm.menusByMenusId=:m")
    List<Resource> findMenusByResource(@Param("m")Menus m);
}
