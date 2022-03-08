package com.rbac.demo.jpa;

import com.rbac.demo.entity.Suppplier;
import com.rbac.demo.entity.SwFirm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSupplier  extends JpaRepository<Suppplier,Integer> {
    List<Suppplier> findSupppliersBySupplierName(String name);
    Page<Suppplier> findSupppliersBySupplierNameLike(String name, Pageable pageable);
}
