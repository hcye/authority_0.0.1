package com.rbac.demo.jpa;

import com.rbac.demo.entity.Role2Menus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRole2Menus extends JpaRepository<Role2Menus,Integer> {
}
