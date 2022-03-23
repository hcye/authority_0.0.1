package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.Suppplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSupplier  extends JpaRepository<Suppplier,Integer> {
    List<Suppplier> findSupppliersBySupplierName(String name);
    Page<Suppplier> findSupppliersBySupplierNameLike(String name, Pageable pageable);
}
