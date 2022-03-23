package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.SysGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaGroup extends JpaRepository<SysGroup,Integer> {
    @Query("select count(employee) from Employee employee where employee.sysGroupByGroupId=:sysGroup and employee.onjob='0'")
    int getEmpCountOfGroup(@Param("sysGroup") SysGroup sysGroup);
    @Query("select distinct(sysGroup.gname) from SysGroup sysGroup where sysGroup.deleteFlag = 0")  //没被删除的
    List<String> getDistinctGourpName();
    @Query("select sysGroup from SysGroup sysGroup where sysGroup.deleteFlag = 0 and sysGroup.gname=:name")
    SysGroup findSysGroupByGname(@Param("name") String name);
    @Query("select sysGroup from SysGroup sysGroup where sysGroup.deleteFlag = 0")
    List<SysGroup> findAllExcludDeleted();
}
