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

    Assert  findAssertByAssestnum(String num);
    @Query("select ast from Assert ast where ast.workless='0' and ast.aname = :name ")
    List<Assert> findAssertsByAname(@Param("name")String name);

    @Query("select ast from Assert ast where ast.workless=:dam and ast.aname like :searchKey")
    List<Assert> findAssertsByAnameLikeAndDamFlag(@Param("searchKey")String search,@Param("dam") String damFlag);
    @Query("select ast from Assert ast where ast.workless=:dam and ast.assestnum like :searchKey ")
    List<Assert> findAssertsByAssestnumLikeAndDamFlag(@Param("searchKey")String search,@Param("dam") String damFlag);
    @Query("select ast from Assert ast where ast.workless=:dam and ast.employeeByBorrower.pingyin like :searchKey ")
    List<Assert> findAssertsByBorroworPingyinLikeAndDamFlag(@Param("searchKey")String search,@Param("dam") String damFlag);
    @Query("select ast from Assert ast where ast.workless=:dam and ast.employeeByBorrower.ename like :searchKey ")
    List<Assert> findAssertsByBorroworNameLikeAndDamFlag(@Param("searchKey")String search,@Param("dam") String damFlag);
    @Query("select ast from Assert ast where ast.workless=:dam and ast.assetTypeByAssertType.typeName=:tp")
    List<Assert> findAssertsBytype(@Param("tp") String type,@Param("dam") String damFlag);


    @Query("select ast from Assert ast where ast.workless=:dam and ast.aname like :searchKey")
    Page<Assert> findAssertsByAnameLikeAndDamFlag(@Param("searchKey")String search,@Param("dam") String damFlag, Pageable pageable);
    @Query("select ast from Assert ast where ast.workless=:dam and ast.assestnum like :searchKey ")
    Page<Assert> findAssertsByAssestnumLikeAndDamFlag(@Param("searchKey")String search,@Param("dam") String damFlag, Pageable pageable);
    @Query("select ast from Assert ast where ast.workless=:dam and ast.employeeByBorrower.pingyin like :searchKey ")
    Page<Assert> findAssertsByBorroworPingyinLikeAndDamFlag(@Param("searchKey")String search,@Param("dam") String damFlag,  Pageable pageable);
    @Query("select ast from Assert ast where ast.workless=:dam and ast.employeeByBorrower.ename like :searchKey ")
    Page<Assert> findAssertsByBorroworNameLikeAndDamFlag(@Param("searchKey")String search,@Param("dam") String damFlag,  Pageable pageable);
}
