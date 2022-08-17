package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.StoreLocate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaStoreLocate extends JpaRepository<StoreLocate,Integer> {
    Page<StoreLocate> findStoreLocatesByLocateLike(String locate, Pageable pageable);
    List<StoreLocate> findStoreLocatesByLocate(String locate);
}
