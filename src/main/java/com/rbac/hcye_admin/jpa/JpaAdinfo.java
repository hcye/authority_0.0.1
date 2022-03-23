package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.Adinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAdinfo extends JpaRepository<Adinfo,Integer> {
}
