package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.AssetCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAssetCheck extends JpaRepository<AssetCheck,Integer> {
}
