package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.StateCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaStateCode extends JpaRepository<StateCode,Integer> {

    @Query("select distinct(stateCode.repoName) from StateCode stateCode where stateCode.stateCodeProjByProjId.projName =:projName")
    List<String> getReposByProj(@Param("projName")String projName);

    @Query("select stateCode.repoName from StateCode stateCode  where" +
            " stateCode.stateCodeProjByProjId.projType =:repoType and stateCode.stateCodeProjByProjId.projName =:projName")
    List<String> getReposByTypeAndProj(@Param("repoType")String type,@Param("projName")String projName);


    @Query("select stateCode from StateCode stateCode  where" +
            " stateCode.stateCodeProjByProjId.projType =:repoType and" +
            " stateCode.stateCodeProjByProjId.projName =:projName and" +
            " stateCode.repoName =:repoName")
    StateCode getRepoByTypeAndProjAndRepoName(@Param("repoType")String type,@Param("projName")
            String projName,@Param("repoName")String repoName);

}
