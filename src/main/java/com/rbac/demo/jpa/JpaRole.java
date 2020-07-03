package com.rbac.demo.jpa;

import com.rbac.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRole extends JpaRepository<Role,Integer> {

}
