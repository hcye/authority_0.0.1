package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.SwFirm;
import com.rbac.hcye_admin.entity.SwOidTemp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSwOidTemp extends JpaRepository<SwOidTemp,Integer> {
    SwOidTemp findSwOidTempByOidName(String name);
    SwOidTemp findSwOidTempByOidNameAndAndSwFirmBySwFirm(String oidname, SwFirm swFirm);
    SwOidTemp findSwOidTempByOidTemp(String tmp);
    Page<SwOidTemp> findSwOidTempsByOidNameLike(String name, Pageable pageable);
    //  @Query("select ast from Assert ast where ast.aname=:astName and ast.workless='0' and ast.assetTypeByAssertType=:astType ")
    //   Page<Assert> findAssertsByDevice(@Param("astName") String astName, @Param("astType") AssetType assetType, Pageable pageable);
    @Query("select sw from SwOidTemp sw where sw.oidName like :name and sw.swFirmBySwFirm.fname=:fname")
    Page<SwOidTemp> findSwOidTempsByOidNameLikeAndSwFirm(@Param("name")String name, @Param("fname")String fname, Pageable pageable);
}
