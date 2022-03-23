package com.rbac.hcye_admin.easyExcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.rbac.hcye_admin.entity.*;
import com.rbac.hcye_admin.jpa.*;
import com.rbac.hcye_admin.service.AsmRecordService;
import com.rbac.hcye_admin.service.AsmService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReadAssetEventListener extends AnalysisEventListener<AssetDownloadModel> {
    private JpaAssert jpaAssert;
    private JpaEmployee jpaEmployee;
    private JpaAssetType jpaAssetType;
    private JpaDevType jpaDevType;
    private AsmService asmService;
    private JpaGroup jpaGroup;
    private JpaSupplier jpaSupplier;
    private JpaOperatRecord jpaOperatRecord;
    private List<Assert> listOthermeans = new ArrayList<>();
    private JpaAssetRecord jpaAssetRecord;
    private AsmRecordService asmRecordService;

    public ReadAssetEventListener(AsmRecordService asmRecordService,JpaAssetRecord jpaAssetRecord,JpaSupplier jpaSupplier,JpaAssert jpaAssert,JpaEmployee jpaEmployee,JpaAssetType jpaAssetType,AsmService asmService,JpaOperatRecord jpaOperatRecord,JpaDevType jpaDevType,JpaGroup jpaGroup) {
        this.jpaAssert=jpaAssert;
        this.jpaEmployee=jpaEmployee;
        this.jpaAssetType=jpaAssetType;
        this.jpaOperatRecord=jpaOperatRecord;
        this.asmService=asmService;
        this.jpaDevType=jpaDevType;
        this.jpaGroup=jpaGroup;
        this.jpaSupplier=jpaSupplier;
        this.jpaAssetRecord=jpaAssetRecord;
        this.asmRecordService=asmRecordService;
    }

    @Override
    public void invoke(AssetDownloadModel assetDownloadModel, AnalysisContext analysisContext) {
        String devName= assetDownloadModel.getDevName().trim();
        String assetNum= assetDownloadModel.getAssetNum().trim();
        String borrower= assetDownloadModel.getBorrower().trim();
        String borTime= assetDownloadModel.getBorTime().trim();
        String price= assetDownloadModel.getPrice().trim();
        String model= assetDownloadModel.getModel().trim();
        String putinTime= assetDownloadModel.getPutinTime().trim();
        String remarks= assetDownloadModel.getRemarks().trim();
        String workless= assetDownloadModel.getWorkless().trim();
        String assetTypeStr= assetDownloadModel.getAssetType().trim();
        String snNum= assetDownloadModel.getSnNum().trim();
        String provider=assetDownloadModel.getProvider().trim();
        String groupId=assetDownloadModel.getSysGroup().trim();
        String templat = "";
        Assert anAssert = new Assert();
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        int num=analysisContext.readRowHolder().getRowIndex();
        if (!assetDownloadModel.getDevName().equals("")) {
            anAssert.setAname(devName.trim());
            AssetType assetType = null;


            if (assetTypeStr != null && assetTypeStr.trim().length() > 0) {
                List<AssetType> types = jpaAssetType.findAll();
                boolean flag = false;
                for (AssetType s : types) {
                    if (s.getTypeName().equals(assetTypeStr.trim())) {
                        assetType = s;
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    anAssert.setAssetTypeByAssertType(assetType);
                    DevType devType =jpaDevType.findDevTypeByDevNameAndAssetTypeByAssertTypeId(devName.trim(),jpaAssetType.findAssetTypeByName(assetTypeStr.trim()));
                    if(devType==null){
                        throw new ExcelAnalysisException(num + "行" + "设备类型未预定义！资产管理->设备类型->新增，添加对应资产类型下的设备类型");
                    }
                    templat =devType.getAssetNumTemplate();
                } else {
                    throw new ExcelAnalysisException(num + "行" + "类型不存在! 资产管理->资产类型->新增，添加");
                }
            } else {
                throw new ExcelAnalysisException(num + "行" + "类型 不能为空");
            }



            if (assetNum != null) {
                if ((!assetNum.equals(""))&&jpaAssert.findAssertByAssestnum(assetNum.trim()) != null) {
                    throw new ExcelAnalysisException(num + "行的资产编号重复");
                } else {
                    boolean res = asmService.validDevTypeNum(assetNum, templat);
                    if (res) {
                        anAssert.setAssestnum(assetNum.trim());
                    } else {
                        throw new ExcelAnalysisException(num + "行的资产编号不匹配模板要求");
                    }
                }
            }
            if (!model.equals("")) {
                anAssert.setModel(model.trim());
            }
            if (!snNum.equals("")) {
                anAssert.setSnnum(snNum.trim());
            }
            if (!price.equals("")) {
                try {
                    float pri=Float.parseFloat(price);
                }catch (Exception e){
                    throw new ExcelAnalysisException(num + "价格只能是数字或者小数");
                }
                anAssert.setPrice(price.trim());
            }
            if (!putinTime.equals("")) {   //到库时间
                try {
                    anAssert.setPutintime(new Date(format1.parse(putinTime.trim()).getTime()));
                } catch (ParseException e) {
                    throw new ExcelAnalysisException(num + "行的入库时间格式错误");
                }
            }
            if (!borrower.equals("")) {  //借用人
                List<Employee> employees = jpaEmployee.findEmployeesByEname(borrower.trim());

                if (employees.size() ==1) {
                    anAssert.setEmployeeByBorrower(employees.get(0));
                } else if(employees.size()==0){
                    throw new ExcelAnalysisException(num + "行的用户 " + borrower + " 不存在");
                }else if(employees.size()>1){
                    throw new ExcelAnalysisException(num + "行的用户 " + borrower + " 存在多个,请在ad域修改重名用户");
                }
            }
            if (!borTime.equals("")) {
                try {
                    anAssert.setBrotime(new Date(format1.parse(borTime.trim()).getTime()));
                } catch (ParseException e) {
                    throw new ExcelAnalysisException(num + "行的借用时间格式错误");
                }
            }
            if (!remarks.equals("")) {
                anAssert.setRemarks(remarks.trim());
            }
            if (!workless.equals("")) {

                if (workless.trim().equals("报废")) {
                    anAssert.setWorkless("1");
                } else if (workless.trim().equals("完好")) {
                    anAssert.setWorkless("0");
                } else {
                    throw new ExcelAnalysisException(num + "行" + "只能填写 报废或者完好");
                }
            } else {
                throw new ExcelAnalysisException(num + "行" + "只能填写 报废或者完好");
            }
            if(!provider.equals("")){
                List<Suppplier> supppliers=jpaSupplier.findAll();
                boolean flag=false;
                for (Suppplier suppplier:supppliers){
                    if(suppplier.getSupplierName().equals(provider.trim())){
                        anAssert.setSuppplierBySupplier(suppplier);
                        flag=true;
                        break;
                    }
                }
                if(!flag){
                    throw new ExcelAnalysisException(num + "行" + "名为 "+provider+" 的供应商不存在,资产管理->供应商->新增");
                }
            }
            if(!groupId.equals("")){
                int sysGroupId=Integer.parseInt(groupId);
                SysGroup sysGroup=jpaGroup.findById(sysGroupId).get();
                if(sysGroup!=null){
                    anAssert.setSysGroupBySysGroup(sysGroup);
                }else {
                    throw new ExcelAnalysisException(num + "行" + "无法搜索到对应主体");
                }
            }

        } else {
            throw new ExcelAnalysisException(num + "行" + "设备名称为空");

        }
        for (Assert o : listOthermeans) {
            if(anAssert.getAssestnum().equals("")){
                listOthermeans.add(anAssert);
                break;
            } else if (o.getAssestnum().equals(anAssert.getAssestnum())) {
                throw new ExcelAnalysisException(num + "行" + "资产编号表格内重复");
            }
        }
        listOthermeans.add(anAssert);
    }


    @Transactional(rollbackFor = ExcelAnalysisException.class)
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        Employee employee = (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
        OperatRecord record = new OperatRecord();
        record.setAction("上传");
        record.setActionTime(new Timestamp(new java.util.Date().getTime()));
        record.setEmployeeByDealer(employee);
        jpaOperatRecord.save(record);
        for(Assert ast:listOthermeans){
            Assert anAssert=jpaAssert.save(ast);
            asmRecordService.createAndSaveAssetRecord(AssetAction.putin,anAssert,null,null);
        }
       // jpaAssert.saveAll(listOthermeans);
    }
}
