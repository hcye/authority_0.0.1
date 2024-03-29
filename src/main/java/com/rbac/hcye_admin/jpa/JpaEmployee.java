package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.Employee;
import com.rbac.hcye_admin.entity.Role;
import com.rbac.hcye_admin.entity.SysGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaEmployee extends JpaRepository<Employee,Integer> {

    @Query("select e from Employee  e where e.id=:id")
    Employee findEmployeeById(@Param("id")int id);
    List<Employee> findEmployeesByEname(String ename);
    @Query("select ur.roleByRoleId from User2Role ur where ur.employeeByUserId=:e")
    List<Role> findRoleByEmployee(@Param("e") Employee e);
    @Query("select employees from Employee employees where employees.sysGroupByGroupId=:sysGroup and employees.onjob='0'  ")
    Page<Employee> findEmployeesBySysGroupByGroupId(@Param("sysGroup") SysGroup sysGroup, Pageable pageable);
    @Query("select employee from Employee employee where  employee.onjob='0' and employee.ename like :name ")
    Page<Employee> findEmployeesByEnameLike(@Param("name") String name,Pageable pageable);
    @Query("select employee from Employee employee where   employee.onjob='0' and employee.pingyin like :py")
    Page<Employee> findEmployeesByPingyinLike(@Param("py") String py,Pageable pageable);
    @Query("select employee.ename from Employee employee where employee.onjob='0' and employee.ename like :name")
    List<String> findEmployeesNameByNameLike(@Param("name")String name);
    @Query("select employee.ename from Employee employee where  employee.onjob='0' and employee.pingyin like :py")
    List<String> findEmployeesNameByPinyinLike(@Param("py")String name);

    Employee findEmployeeByLoginName(String ename);


    @Query("select employee from Employee employee where  employee.onjob='0' and employee.pingyin like :py")
    List<Employee> findEmployeesByPingyinLike(@Param("py")String name);

    @Query("select employee from Employee employee where employee.onjob='0' and  employee.ename like :name")
    List<Employee> findEmployeesByEnameLike(@Param("name")String name);

    @Query("select employee from Employee employee where  employee.pingyin like :py")
    List<Employee> findEmployeesByPingyinLikeForReturn(@Param("py")String name);

    @Query("select employee from Employee employee where  employee.ename like :name")
    List<Employee> findEmployeesByEnameLikeForReturn(@Param("name")String name);

}
