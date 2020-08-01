package com.rbac.demo.jpa;

import com.rbac.demo.entity.Resource2Menus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaResource2menus extends JpaRepository<Resource2Menus,Integer> {
}
