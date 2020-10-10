package com.rbac.demo.jpa;

import com.rbac.demo.entity.OperatRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface JpaOperatRecord extends JpaRepository<OperatRecord,Integer> {
    Page<OperatRecord> findOperatRecordsByAction(String action, Pageable pageable);
    Page<OperatRecord> findOperatRecordsByActionAndActionTimeBetween(String action, Date date1,Date date2,Pageable pageable);

    Page<OperatRecord> findOperatRecordsByActionTimeBetween(Date date1,Date date2,Pageable pageable);

}
