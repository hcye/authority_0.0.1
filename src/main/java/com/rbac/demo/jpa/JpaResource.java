package com.rbac.demo.jpa;

import com.rbac.demo.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaResource extends JpaRepository<Resource,Integer> {
}
