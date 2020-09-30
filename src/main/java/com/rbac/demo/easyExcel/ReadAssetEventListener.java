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
import java.util.List;
import java.util.Map;

@Component
public class ReadAssetEventListener extends AnalysisEventListener<EasyExcelMapedModel> {
    private JpaAssert jpaAssert;
    private JpaEmployee jpaEmployee;
    private JpaAssetType jpaAssetType;
    private AsmService asmService;
    private JpaOperatRecord jpaOperatRecord;
    private List<Assert> listOthermeans = new ArrayList<>();

    public ReadAssetEventListener(JpaAssert jpaAssert,JpaEmployee jpaEmployee,JpaAssetType jpaAssetType,AsmService asmService,JpaOperatRecord jpaOperatRecord) {
        this.jpaAssert=jpaAssert;
        this.jpaEmployee=jpaEmployee;
        this.jpaAssetType=jpaAssetType;
        this.jpaOperatRecord=jpaOperatRecord;
        this.asmService=asmService;
    }

    @Override
    public void invoke(EasyExcelMapedModel easyExcelMapedModel, AnalysisContext analysisContext) {
        String devName=easyExcelMapedModel.getDevName();
        String assetNum=easyExcelMapedModel.getAssetNum();
        String borrower=easyExcelMapedModel.getBorrower();
        String borTime=easyExcelMapedModel.getBorTime();
        String model=easyExcelMapedModel.getModel();
        String putinTime=easyExcelMapedModel.getPutinTime();
        String remarks=easyExcelMapedModel.getRemarks();
        String workless=easyExcelMapedModel.getWorkless();
        String assetType=easyExcelMapedModel.getAssetType();
        String snNum=easyExcelMapedModel.getSnNum();
        String templat = "";
        Assert anAssert = new Assert();
        DateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
        int num=analysisContext.readRowHolder().getRowIndex();

        System.out.println(num+"行编号");
        System.out.println(easyExcelMapedModel+"自动封装类");
/*        if (easyExcelMapedModel.getDevName()!= null) {
            anAssert.setAname(strings.get(1).trim());
            AssetType assetType = null;
            if (strings.get(10) != null && strings.get(10).length() > 0) {
                List<AssetType> types = jpaAssetType.findAll();
                boolean flag = false;
                for (AssetType s : types) {
                    if (s.getTypeName().equals(strings.get(10).trim())) {
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
            if (strings.get(0) != null && strings.get(0).length() > 0) {
                if (jpaAssert.findAssertByAssestnum(strings.get(0).trim()) != null) {
                    throw new ExcelAnalysisException(num + "行的资产编号重复");
                } else {
                    Map<String, String> res = asmService.valid(strings.get(0), templat);
                    if (res.get("error") == null) {
                        anAssert.setAssestnum(strings.get(0).trim());
                    } else {
                        throw new ExcelAnalysisException(num + "行的资产编号不匹配模板要求");
                    }
                }
            }
            if (strings.get(2) != null) {
                anAssert.setModel(strings.get(2).trim());
            }
            if (strings.get(3) != null) {
                anAssert.setSnnum(strings.get(3).trim());
            }
            if (strings.get(4) != null) {
                anAssert.setPrice(strings.get(4).trim());
            }
            if (strings.get(5) != null) {   //到库时间
                try {
                    anAssert.setPutintime(new Date(format1.parse(strings.get(5).trim()).getTime()));
                } catch (ParseException e) {
                    throw new ExcelAnalysisException(num + "行的入库时间格式错误");
                }
            }
            if (strings.get(6) != null) {  //借用人
                List<Employee> employees = jpaEmployee.findEmployeesByEname(strings.get(6).trim());
                if (employees.size() > 0) {
                    anAssert.setEmployeeByBorrower(employees.get(0));
                } else {
                    throw new ExcelAnalysisException(num + "行的用户 " + strings.get(6) + " 不存在");
                }
            }
            if (strings.get(7) != null) {
                try {
                    anAssert.setBrotime(new Date(format1.parse(strings.get(7).trim()).getTime()));
                } catch (ParseException e) {
                    throw new ExcelAnalysisException(num + "行的借用是时间格式错误");
                }
            }
            if (strings.get(8) != null) {
                anAssert.setRemarks(strings.get(8).trim());
            }
            if (strings.get(9) != null) {

                if (strings.get(9).trim().equals("报废")) {
                    anAssert.setWorkless("1");
                } else if (strings.get(9).trim().equals("完好")) {
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
        }*/
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
