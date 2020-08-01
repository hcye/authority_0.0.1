package com.rbac.demo.jpa;

import com.rbac.demo.entity.User2Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUser2Role extends JpaRepository<User2Role,Integer> {

}
