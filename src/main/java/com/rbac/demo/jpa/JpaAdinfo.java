package com.rbac.demo.jpa;

import com.rbac.demo.entity.Adinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAdinfo extends JpaRepository<Adinfo,Integer> {
}
