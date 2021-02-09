package com.rbac.demo.jpa;

import com.rbac.demo.entity.SwOidTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSwOidTemp extends JpaRepository<SwOidTemp,Integer> {
}
