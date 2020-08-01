package com.rbac.demo.jpa;

import com.rbac.demo.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaGroup extends JpaRepository<Group,Integer> {
}
