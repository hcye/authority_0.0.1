package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.SysMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMail extends JpaRepository<SysMail,Integer> {
    SysMail findSysMailByForwhat(String forwhat);
}
