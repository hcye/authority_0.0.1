package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.StateCodeProj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaProjStatCode extends JpaRepository<StateCodeProj,Integer> {
    @Query("select proj.projName from StateCodeProj proj where proj.projType = 'git'")
    List<String> getDistinctProjNameWhereTypeIsGit();
    @Query("select proj.projName from StateCodeProj proj where proj.projType = 'svn'")
    List<String> getDistinctProjNameWhereTypeIsSvn();
    @Query("select proj.projName from StateCodeProj proj where proj.projType =:repoType")
    List<String> getProjsByType(@Param("repoType")String type);
    StateCodeProj findByProjNameAndAndProjType(String name,String type);
}
