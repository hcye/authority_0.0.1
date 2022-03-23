package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.EchangeDevs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaExchangeDevs extends JpaRepository<EchangeDevs,Integer> {
}
