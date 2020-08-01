package com.rbac.demo.jpa;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface JpaEmployee extends JpaRepository<Employee,Integer> {

    @Query("select e from Employee  e where e.id=:id")
    Employee findEmployeeById(@Param("id")int id);
    List<Employee> findEmployeesByEname(String ename);
    @Query("select ur.roleByRoleId from User2Role ur where ur.employeeByUserId=:e")
    List<Role> findRoleByEmployee(@Param("e") Employee e);

}
