package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.AssetCkRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAssetCkRecord extends JpaRepository<AssetCkRecord,Integer> {
}
