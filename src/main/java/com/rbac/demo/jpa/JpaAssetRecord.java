package com.rbac.demo.jpa;

import com.rbac.demo.entity.Adinfo;
import com.rbac.demo.entity.AssetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAssetRecord extends JpaRepository<AssetRecord,Integer> {

}
