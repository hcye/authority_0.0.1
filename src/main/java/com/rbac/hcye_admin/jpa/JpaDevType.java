package com.rbac.hcye_admin.jpa;
import com.rbac.hcye_admin.entity.AssetType;
import com.rbac.hcye_admin.entity.DevType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaDevType extends JpaRepository<DevType,Integer> {
    @Query("select devtype from DevType devtype where devtype.devName=:name and devtype.assetTypeByAssertTypeId=:tp")
    DevType findDevTypeByDevNameAndAssertType(@Param("name")String name,@Param("tp") AssetType tp);

    List<DevType> findDevTypesByAssetNumTemplate(String temp);


    @Query("select dev.devName from DevType dev where dev.assetTypeByAssertTypeId.typeName=:tp")
    List<String> findDevTypesNameByAssertType(@Param("tp")String tp);

    @Query("select dev from DevType dev where dev.assetTypeByAssertTypeId.typeName=:tp")
    List<DevType> findDevTypesByAssertType(@Param("tp")String devType);

    @Query("select dev from DevType dev where dev.assetTypeByAssertTypeId.typeName=:tp")
    Page<DevType> findDevTypesByAssertType(@Param("tp")String devType,Pageable pageable);

    @Query("select dev from DevType dev where dev.assetTypeByAssertTypeId=:tp and dev.devName=:name")
    DevType findDevTypeByDevNameAndAssetTypeByAssertTypeId(@Param("name")String name,@Param("tp")AssetType assetType);

    Page<DevType> findDevTypesByDevNameLike(String name, Pageable pageable);

    @Query("select dev from DevType dev where dev.devName like :name and dev.assetTypeByAssertTypeId.typeName=:type")
    Page<DevType> findDevTypesByDevNameLikeAndAssetType(@Param("name") String name,@Param("type") String type, Pageable pageable);
}
