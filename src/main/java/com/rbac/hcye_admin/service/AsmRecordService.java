package com.rbac.hcye_admin.service;

import com.rbac.hcye_admin.entity.*;
import com.rbac.hcye_admin.jpa.JpaAssetAction;
import com.rbac.hcye_admin.jpa.JpaAssetRecord;
import com.rbac.hcye_admin.jpa.JpaOperatRecord;
import com.rbac.hcye_admin.jpa.JpaStoreLocate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;

@Component
public class AsmRecordService {
    @Autowired
    private JpaOperatRecord jpaOperatRecord;
    @Autowired
    private JpaAssetRecord jpaAssetRecord;

    @Autowired
    private JpaStoreLocate jpaStoreLocate;
    @Autowired
    private JpaAssetAction jpaAssetAction;
    public void write(String action, Timestamp actionTime, Employee employeeByDealer, Employee employeeByAssertEmp, Assert assertByAssertAsset,String remarks){
        OperatRecord op=new OperatRecord( action,  actionTime,  employeeByDealer,  employeeByAssertEmp,  assertByAssertAsset,remarks);
        jpaOperatRecord.save(op);
    }
    public void createAndSaveAssetRecord(String action,Assert ast,Employee e,AssetType assetType){
        AssetRecord assetRecord=new AssetRecord();
        AssetAction assetAction=jpaAssetAction.findAssetActionByAssetAction(action);
        assetRecord.setAssetActionByAssetAction(assetAction);
        assetRecord.setAssertByAsset(ast);
        if(action.equals(AssetAction.retrun_asset)){
            assetRecord.setActDetail("从"+e.getLoginName()+"归还");
        }
        if(action.equals(AssetAction.borrow)){
            assetRecord.setActDetail("借用给:"+e.getEname());
        }
        if(action.equals(AssetAction.zhuanyi)){
            assetRecord.setActDetail("转移到:"+assetType.getTypeName());
        }
        if(action.equals(AssetAction.diaobo)){
            assetRecord.setActDetail("调拨到"+e.getLoginName());
        }
        if(action.equals(AssetAction.move)){
            assetRecord.setActDetail("借调到:"+jpaStoreLocate.findById(ast.getLocate()).get().getLocate());
        }
        assetRecord.setStartTime(new Date(new java.util.Date().getTime()));
        jpaAssetRecord.save(assetRecord);
    }
}
