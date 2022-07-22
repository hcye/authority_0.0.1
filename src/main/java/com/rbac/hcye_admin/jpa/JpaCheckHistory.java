package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.CheckHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface JpaCheckHistory extends JpaRepository<CheckHistory,Integer> {
}
