package com.rbac.hcye_admin.jpa;

import com.rbac.hcye_admin.entity.Assert;
import com.rbac.hcye_admin.entity.AssetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaAssert extends JpaRepository<Assert,Integer> {
    @Query("select ast from Assert ast where ast.aname=:astName and ast.workless='0' and ast.assetTypeByAssertType=:astType ")
    Page<Assert> findAssertsByDevice(@Param("astName") String astName,@Param("astType") AssetType assetType, Pageable pageable);

    @Query("select ast from Assert ast where ast.workless='0' and ast.aname like :name and ast.assetTypeByAssertType.typeName=:type")
    Page<Assert> findAssertsByAnameAndAssetType(@Param("name")String name,@Param("type")String type,Pageable pageable);


    @Query("select distinct ast.aname from Assert ast where ast.assetTypeByAssertType=:astType and ast.workless='0'")
    List<String> getDistinctAssertNames(@Param("astType") AssetType astType);

    @Query("select ast from Assert ast where ast.workless=:dam and ast.assetTypeByAssertType.typeName=:tp")
    Page<Assert> findAssertsBytype(@Param("tp") String type,@Param("dam") String damFlag, Pageable pageable);

    @Query("select ast from Assert ast where ast.workless='0' and ast.assestnum like :searchKey and ast.assetTypeByAssertType.typeName=:type")
    Page<Assert> findAssertsByAssestnumAndAssetType(@Param("searchKey")String searchKey,@Param("type")String type,Pageable pageable);


    Assert  findAssertByAssestnum(String num);

    List<Assert> findAssertsByLocate(int locate);

    List<Assert> findAssertsByAname(String name);

    @Query("select ast from Assert ast where ast.workless='0' and ast.aname = :name and ast.assetTypeByAssertType=:asttype")
    List<Assert> findAssertsByAnameAndAssetType(@Param("name")String name,@Param("asttype")AssetType assetType);

    @Query("select ast from Assert ast where ast.workless=:dam and ast.aname like :searchKey and ast.assetTypeByAssertType.typeName=:tp")
    List<Assert> findAssertsByAnameLikeAndDamFlagAndType(@Param("tp")String type,@Param("searchKey")String search,@Param("dam") String damFlag);
    @Query("select ast from Assert ast where ast.workless=:dam and ast.assestnum like :searchKey and ast.assetTypeByAssertType.typeName=:tp")
    List<Assert> findAssertsByAssestnumLikeAndDamFlagAndType(@Param("tp")String type,@Param("searchKey")String search,@Param("dam") String damFlag);
    @Query("select ast from Assert ast where ast.workless=:dam and ast.employeeByBorrower.pingyin like :searchKey ")
    List<Assert> findAssertsByBorroworPingyinLikeAndDamFlag(@Param("searchKey")String search,@Param("dam") String damFlag);
    @Query("select ast from Assert ast where ast.workless=:dam and ast.employeeByBorrower.ename like :searchKey")
    List<Assert> findAssertsByBorroworNameLikeAndDamFlag(@Param("searchKey")String search,@Param("dam") String damFlag);
    @Query("select ast from Assert ast where ast.workless=:dam and ast.assetTypeByAssertType.typeName=:tp")
    List<Assert> findAssertsBytype(@Param("tp") String type,@Param("dam") String damFlag);
    @Query("select ast from Assert ast where ast.assetTypeByAssertType.typeName=:tp")
    List<Assert> findAssertByAssetType_without_damflag(@Param("tp") String type);

    @Query("select ast from Assert ast where ast.workless=:dam and ast.snnum like :searchKey")
    List<Assert> findAssertBySnnumLikeAndDamFlag(@Param("searchKey")String search,@Param("dam") String damFlag);




//    @Query("select ast from Assert ast where ast.workless=:dam and ast.assetTypeByAssertType.typeName=:tp")
//    List<Assert> findAssertsBytypeAndDevType(@Param("tp") String type,@Param("dtp") String dev_type);




    @Query("select ast from Assert ast where ast.workless=:dam and ast.aname like :searchKey and ast.assetTypeByAssertType.typeName=:tp")
    Page<Assert> findAssertsByAnameLikeAndDamFlagAndType(@Param("tp")String type,@Param("searchKey")String search,@Param("dam") String damFlag, Pageable pageable);
    @Query("select ast from Assert ast where ast.workless=:dam and ast.assestnum like :searchKey and ast.assetTypeByAssertType.typeName=:tp")
    Page<Assert> findAssertsByAssestnumLikeAndDamFlagAndType(@Param("tp")String type,@Param("searchKey")String search,@Param("dam") String damFlag, Pageable pageable);
    @Query("select ast from Assert ast where ast.workless=:dam and ast.employeeByBorrower.pingyin like :searchKey")
    Page<Assert> findAssertsByBorroworPingyinLikeAndDamFlag(@Param("searchKey")String search,@Param("dam") String damFlag,  Pageable pageable);
    @Query("select ast from Assert ast where ast.workless=:dam and ast.employeeByBorrower.ename like :searchKey")
    Page<Assert> findAssertsByBorroworNameLikeAndDamFlag(@Param("searchKey")String search,@Param("dam") String damFlag,  Pageable pageable);

    @Query("select ast from Assert ast where ast.workless=:dam and ast.snnum like :searchKey")
    Page<Assert> findAssertBySnnumLikeAndDamFlag(@Param("searchKey")String search,@Param("dam") String damFlag,  Pageable pageable);

    @Query("select ast from Assert ast where ast.assetTypeByAssertType.typeName=:tp and ast.employeeByBorrower is null")
    Page<Assert> findAssertByAssetTypeWithoutDamflagWithoutBroByPage(@Param("tp") String type,Pageable pageable);

    @Query("select ast from Assert ast where ast.assetTypeByAssertType.typeName=:tp and ast.aname=:dtp and ast.employeeByBorrower is null")
    Page<Assert> findAssertsByAnameAndAssetTypeNameWithoutBro(@Param("tp") String type,@Param("dtp") String dev_type,Pageable pageable);



}
