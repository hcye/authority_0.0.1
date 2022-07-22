package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.AssetCheck;
import com.rbac.hcye_admin.entity.AssetCkRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaAssetCkRecord extends JpaRepository<AssetCkRecord,Integer> {
    List<AssetCkRecord> findAssetCkRecordsByAssetCheckByAssetCheck(AssetCheck assetCheck);
}
