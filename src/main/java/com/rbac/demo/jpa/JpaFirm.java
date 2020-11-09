package com.rbac.demo.jpa;

import com.rbac.demo.entity.SwFirm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFirm extends JpaRepository<SwFirm, Integer> {
}
