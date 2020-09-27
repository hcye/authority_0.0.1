package com.rbac.demo.jpa;

import com.rbac.demo.entity.Assert;
import com.rbac.demo.entity.AssetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaAssert extends JpaRepository<Assert,Integer> {
    @Query("select ast from Assert ast where ast.aname=:astName and ast.workless='0' and ast.employeeByBorrower is null")
    Page<Assert> findAssertsByDevice(@Param("astName") String astName, Pageable pageable);
    @Query("select distinct ast.aname from Assert ast where ast.assetTypeByAssertType=:astType and ast.workless='0'")
    List<String> getDistinctAssertNames(@Param("astType") AssetType astType);
    @Query("select ast from Assert ast where ast.workless='0' and ast.aname like :name and ast.employeeByBorrower is null")
    Page<Assert> findAssertsByAname(@Param("name")String name,Pageable pageable);
    @Query("select ast from Assert ast where ast.workless='0' and ast.assestnum like :searchKey and ast.employeeByBorrower is null")
    Page<Assert> findAssertsByAssestnum(@Param("searchKey")String searchKey,Pageable pageable);

    @Query("select ast from Assert ast where ast.workless=:dam and ast.assetTypeByAssertType.typeName=:tp")
    Page<Assert> findAssertsBytype(@Param("tp") String type,@Param("dam") String damFlag, Pageable pageable);

    @Query("select ast from Assert ast where ast.workless='0' and ast.aname like :name")
    Page<Assert> findAssertsByAnameLike(@Param("name")String name,Pageable pageable);


    @Query("select ast from Assert ast where ast.workless='0' and ast.assestnum like :searchKey ")
    Page<Assert> findAssertsByAssestnumLike(@Param("searchKey")String searchKey,Pageable pageable);

    @Query("select ast from Assert ast where ast.workless='0' and ast.employeeByBorrower.pingyin like :searchKey ")
    Page<Assert> findAssertsByBorroworPingyinLike(@Param("searchKey")String search, Pageable pageable);

    @Query("select ast from Assert ast where ast.workless='0' and ast.employeeByBorrower.ename like :searchKey ")
    Page<Assert> findAssertsByBorroworNameLike(@Param("searchKey")String search, Pageable pageable);
}