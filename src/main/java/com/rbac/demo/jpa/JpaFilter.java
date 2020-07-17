package com.rbac.demo.jpa;

import com.rbac.demo.entity.ScanInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Scope("prototype")
public interface JpaFilter extends JpaRepository<ScanInfo,Long> {

    public List<ScanInfo> findScanInfoByip(String ip);
}
