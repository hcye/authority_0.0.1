package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.AssetAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAsmAction extends JpaRepository<AssetAction,Integer> {
    AssetAction findAssetActionByAssetAction(String action);
}
