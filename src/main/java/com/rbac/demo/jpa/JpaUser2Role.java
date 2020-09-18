package com.rbac.demo.jpa;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.User2Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaUser2Role extends JpaRepository<User2Role,Integer> {

    List<User2Role> findUser2RolesByEmployeeByUserId(Employee e);

}
