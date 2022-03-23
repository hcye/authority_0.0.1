package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.AssetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAssetRecord extends JpaRepository<AssetRecord,Integer> {

}
