package com.rbac.demo.jpa;

import com.rbac.demo.entity.Menus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenus extends JpaRepository<Menus,Integer> {

}
