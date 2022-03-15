package com.rbac.demo.jpa;

import com.rbac.demo.entity.AssetAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAssetAction extends JpaRepository<AssetAction,Integer> {
    AssetAction findAssetActionByAssetAction(String action);
}
