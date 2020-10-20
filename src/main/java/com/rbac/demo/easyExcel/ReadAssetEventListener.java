package com.rbac.demo.easyExcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.rbac.demo.entity.Assert;
import com.rbac.demo.entity.AssetType;
import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.OperatRecord;
import com.rbac.demo.jpa.JpaAssert;
import com.rbac.demo.jpa.JpaAssetType;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.jpa.JpaOperatRecord;
import com.rbac.demo.service.AsmService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReadAssetEventListener extends AnalysisEventListener<AssetDownloadModel> {
    private JpaAssert jpaAssert;
    private JpaEmployee jpaEmployee;
    private JpaAssetType jpaAssetType;
    private AsmService asmService;
    private JpaOperatRecord jpaOperatRecord;
    private List<Assert> listOthermeans = new ArrayList<>();
    private Map<String,String> analysisInfo=new HashMap<>();

    public ReadAssetEventListener(JpaAssert jpaAssert,JpaEmployee jpaEmployee,JpaAssetType jpaAssetType,AsmService asmService,JpaOperatRecord jpaOperatRecord) {
        this.jpaAssert=jpaAssert;
        this.jpaEmployee=jpaEmployee;
        this.jpaAssetType=jpaAssetType;
        this.jpaOperatRecord=jpaOperatRecord;
        this.asmService=asmService;
    }

    @Override
    public void invoke(AssetDownloadModel assetDownloadModel, AnalysisContext analysisContext) {
        String devName= assetDownloadModel.getDevName();
        String assetNum= assetDownloadModel.getAssetNum();
        String borrower= assetDownloadModel.getBorrower();
        String borTime= assetDownloadModel.getBorTime();
        String price= assetDownloadModel.getPrice();
        String model= assetDownloadModel.getModel();
        String putinTime= assetDownloadModel.getPutinTime();
        String remarks= assetDownloadModel.getRemarks();
        String workless= assetDownloadModel.getWorkless();
        String assetTypeStr= assetDownloadModel.getAssetType();
        String snNum= assetDownloadModel.getSnNum();
        String templat = "";
        Assert anAssert = new Assert();
        DateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
        int num=analysisContext.readRowHolder().getRowIndex();
        if (assetDownloadModel.getDevName()!= null) {
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
                    templat = assetType.getAssetCode();
                } else {
                    throw new ExcelAnalysisException(num + "行" + "类型不存在");
                }
            } else {
                throw new ExcelAnalysisException(num + "行" + "类型 不能为空");
            }
            if (assetNum != null && assetNum.trim().length() > 0) {
                if (jpaAssert.findAssertByAssestnum(assetNum.trim()) != null) {
                    throw new ExcelAnalysisException(num + "行的资产编号重复");
                } else {
                    boolean res = asmService.valid(assetNum, templat);
                    if (res) {
                        anAssert.setAssestnum(assetNum.trim());
                    } else {
                        throw new ExcelAnalysisException(num + "行的资产编号不匹配模板要求");
                    }
                }
            }
            if (model != null) {
                anAssert.setModel(model.trim());
            }
            if (snNum != null) {
                anAssert.setSnnum(snNum.trim());
            }
            if (price!= null) {
                try {
                    float pri=Float.parseFloat(price);
                }catch (Exception e){
                    throw new ExcelAnalysisException(num + "价格只能是数字或者小数");
                }
                anAssert.setPrice(price.trim());
            }
            if (putinTime != null) {   //到库时间
                try {
                    anAssert.setPutintime(new Date(format1.parse(putinTime.trim()).getTime()));
                } catch (ParseException e) {
                    throw new ExcelAnalysisException(num + "行的入库时间格式错误");
                }
            }
            if (borrower != null) {  //借用人
                List<Employee> employees = jpaEmployee.findEmployeesByEname(borrower.trim());

                if (employees.size() ==1) {
                    anAssert.setEmployeeByBorrower(employees.get(0));
                } else if(employees.size()==0){
                    throw new ExcelAnalysisException(num + "行的用户 " + borrower + " 不存在");
                }else if(employees.size()>1){
                    throw new ExcelAnalysisException(num + "行的用户 " + borrower + " 存在多个,请在ad域修改重名用户");
                }
            }
            if (borTime != null) {
                try {
                    anAssert.setBrotime(new Date(format1.parse(borTime.trim()).getTime()));
                } catch (ParseException e) {
                    throw new ExcelAnalysisException(num + "行的借用是时间格式错误");
                }
            }
            if (remarks != null) {
                anAssert.setRemarks(remarks.trim());
            }
            if (workless != null) {

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

        } else {
            throw new ExcelAnalysisException(num + "行" + "设备名称为空");

        }
        for (Assert o : listOthermeans) {
            if (o.getAssestnum().equals(anAssert.getAssestnum())) {
                throw new ExcelAnalysisException(num + "行" + "资产编号表格内重复");
            }
        }
        listOthermeans.add(anAssert);
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        super.onException(exception, context);
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
        for (Assert o : listOthermeans) {
            jpaAssert.save(o);
        }

    }
}
