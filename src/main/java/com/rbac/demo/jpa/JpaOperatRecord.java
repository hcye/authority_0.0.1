package com.rbac.demo.jpa;

import com.rbac.demo.entity.OperatRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOperatRecord extends JpaRepository<OperatRecord,Integer> {
}
