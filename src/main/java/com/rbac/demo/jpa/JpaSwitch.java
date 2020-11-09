package com.rbac.demo.jpa;

import com.rbac.demo.entity.SysSwitch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSwitch extends JpaRepository<SysSwitch,Integer> {
}
