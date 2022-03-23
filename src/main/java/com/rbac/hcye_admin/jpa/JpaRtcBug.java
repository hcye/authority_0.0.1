package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.RtcBug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRtcBug extends JpaRepository<RtcBug,Integer> {
}
