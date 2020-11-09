package com.rbac.demo.jpa;

import com.rbac.demo.entity.Oid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOid extends JpaRepository<Oid,Integer> {
}
