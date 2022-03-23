package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.Assert;
import com.rbac.hcye_admin.entity.Employee;
import com.rbac.hcye_admin.entity.OperatRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JpaOperatRecord extends JpaRepository<OperatRecord,Integer> {
    Page<OperatRecord> findOperatRecordsByAction(String action, Pageable pageable);
    Page<OperatRecord> findOperatRecordsByActionAndActionTimeBetween(String action, Date date1,Date date2,Pageable pageable);

    Page<OperatRecord> findOperatRecordsByActionTimeBetween(Date date1,Date date2,Pageable pageable);

    List<OperatRecord> findOperatRecordsByEmployeeByAssertEmp(Employee e);
    List<OperatRecord> findOperatRecordsByEmployeeByDealer(Employee e);
    List<OperatRecord> findOperatRecordsByAssertByAssertAsset(Assert asset);

}
