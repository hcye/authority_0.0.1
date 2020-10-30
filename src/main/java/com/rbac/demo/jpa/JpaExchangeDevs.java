package com.rbac.demo.jpa;

import com.rbac.demo.entity.EchangeDevs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaExchangeDevs extends JpaRepository<EchangeDevs,Integer> {
}
