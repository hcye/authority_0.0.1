package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.AssetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JpaAssetType extends JpaRepository<AssetType,Integer> {
        @Query("select spname from AssetType spname")
        List<AssetType> findAssertType();
        @Query("select spname from AssetType spname where spname.typeName=:name")
        AssetType findAssetTypeByName(@Param("name") String name);


        @Query("select atp from AssetType atp where atp.assetCode is not null")
        Page<AssetType> findAssertTypesAsPage(Pageable pageable);

        @Query("select atp from AssetType atp where atp.typeName like :name")
        Page<AssetType> findAssertTypesByTypeNameLike(@Param("name")String name,Pageable pageable);
}
