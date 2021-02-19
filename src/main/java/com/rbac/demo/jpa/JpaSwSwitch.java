package com.rbac.demo.jpa;

import com.rbac.demo.entity.SwSwitch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaSwSwitch extends JpaRepository<SwSwitch,Integer> {
    SwSwitch findSwSwitchByLabel(String label);
    SwSwitch findSwSwitchByIpAddr(String ip);
    Page<SwSwitch> findSwSwitchesByLabelLike(String name, Pageable pageable);
  //  @Query("select ast from Assert ast where ast.aname=:astName and ast.workless='0' and ast.assetTypeByAssertType=:astType ")
 //   Page<Assert> findAssertsByDevice(@Param("astName") String astName, @Param("astType") AssetType assetType, Pageable pageable);
    @Query("select sw from SwSwitch sw where sw.label like :name and sw.swFirmByFirm.fname=:fname")
    Page<SwSwitch> findSwSwitchesByLabelLikeAndsAndfirm(@Param("name")String name,@Param("fname")String fname,Pageable pageable);
    List<SwSwitch> findSwSwitchesByLevel(String leve);
}
