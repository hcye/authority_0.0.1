package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.Employee;
import com.rbac.hcye_admin.entity.User2Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaUser2Role extends JpaRepository<User2Role,Integer> {

    List<User2Role> findUser2RolesByEmployeeByUserId(Employee e);

}
