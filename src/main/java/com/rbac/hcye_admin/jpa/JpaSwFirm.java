package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.SwFirm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSwFirm extends JpaRepository<SwFirm,Integer> {
    SwFirm findSwFirmByFname(String name);
    Page<SwFirm> findSwFirmsByFnameLike(String name, Pageable pageable);
}
