package com.rbac.demo.jpa;

import com.rbac.demo.entity.RtcBug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRtcBug extends JpaRepository<RtcBug,Integer> {
}
