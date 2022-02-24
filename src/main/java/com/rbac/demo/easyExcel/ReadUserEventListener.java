package com.rbac.demo.easyExcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.OperatRecord;
import com.rbac.demo.entity.SysGroup;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.jpa.JpaGroup;
import com.rbac.demo.jpa.JpaOperatRecord;
import com.rbac.demo.service.Chinese2Eng;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReadUserEventListener extends AnalysisEventListener<UserModel> {
    private JpaGroup jpaGroup;
    private JpaEmployee jpaEmployee;
    private JpaOperatRecord jpaOperatRecord;
    private List<Employee> employees=new ArrayList<Employee>();
    public ReadUserEventListener(JpaGroup jpaGroup, JpaEmployee jpaEmployee,JpaOperatRecord jpaOperatRecord){
        this.jpaGroup=jpaGroup;
        this.jpaEmployee=jpaEmployee;
        this.jpaOperatRecord=jpaOperatRecord;
    }

    @Override
    public void invoke(UserModel data, AnalysisContext context) {
        String depid=data.getGourpId().trim();
        String ename=data.getEname().trim();
        String loginName=data.getLoginName().trim();
        String email= data.getEmail().trim();
        String sex=data.getSex().trim();
        String pingyin;
        Employee employee=new Employee();
        int num=context.readRowHolder().getRowIndex();

        if(depid!=null&& !depid.equals("")){
            SysGroup group =jpaGroup.findById(Integer.parseInt(depid)).get();
            if(group==null){
                throw new ExcelAnalysisException(num + "行" + "部门不存在，检查部门id是否正确");
            }else {
                employee.setEdepart(group.getGname());
                employee.setSysGroupByGroupId(group);
            }
        }else {
            throw new ExcelAnalysisException(num + "行" + "部门id为空");
        }

        if(ename!=null && !ename.equals("")){
            List<Employee> employees=jpaEmployee.findEmployeesByEname(ename);
            if(employees.size()!=0){
                throw new ExcelAnalysisException(num + "行" + "用户名重复");
            }else {
                employee.setEname(ename);
                pingyin=Chinese2Eng.convertHanzi2Pinyin(ename,true);
            }
        }else {
            throw new ExcelAnalysisException(num + "行" + "用户名为空");
        }

        if(loginName!=null && !loginName.equals("")){
            Employee employee1=jpaEmployee.findEmployeeByLoginName(loginName);
            if(employee1!=null){
                throw new ExcelAnalysisException(num + "行" + "登陆名重复");
            }else {
                if(jpaEmployee.findEmployeeByLoginName(pingyin)!=null){
                    throw new ExcelAnalysisException(num + "行" + "请手动设置登陆名");
                }else {
                    employee.setLoginName(pingyin);
                }
            }
        }else {
            loginName=Chinese2Eng.convertHanzi2Pinyin(ename,true);
            employee.setLoginName(loginName);
        }

        if(email!=null && !email.equals("")){
            employee.setEmail(email);
        }

        if (sex!=null && !sex.equals("男") && !sex.equals("女")){
            employee.setSex("女");
        }

        for (Employee employee1:employees){
               if(employee1.getEname().equals(employee.getEname())) {
                   throw new ExcelAnalysisException(num + "行" + "表格内用户名重复");
               }else if(employee1.getLoginName().equals(employee.getLoginName())){
                   throw new ExcelAnalysisException(num + "行" + "表格内登陆名重复");
               }
        }
        employee.setPingyin(pingyin);
        employee.setOnjob("0");
        employees.add(employee);

    }

    @Transactional(rollbackFor = ExcelAnalysisException.class)
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        Employee employee = (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
        OperatRecord record = new OperatRecord();
        record.setAction("上传");
        record.setActionTime(new Timestamp(new java.util.Date().getTime()));
        record.setEmployeeByDealer(employee);
        jpaOperatRecord.save(record);
        jpaEmployee.saveAll(employees);
    }
}
