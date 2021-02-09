package com.rbac.demo.jpa;

import com.rbac.demo.entity.SwIpheadMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSwIpHeadMap extends JpaRepository<SwIpheadMap,Integer> {
}
