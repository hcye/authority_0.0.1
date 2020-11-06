package com.rbac.demo.jpa;

import com.rbac.demo.entity.SysMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMail extends JpaRepository<SysMail,Integer> {
    SysMail findSysMailByForwhat(String forwhat);
}
