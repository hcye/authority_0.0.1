package com.rbac.hcye_admin.easyExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.stereotype.Component;

@Component
public class UserModel {
    @ExcelProperty(value = "部门ID",index = 0)
    private String gourpId="";
    @ExcelProperty(value = "用户名",index = 1)
    private String ename="";
    @ExcelProperty(value = "登陆名",index = 2)
    private String loginName="";
    @ExcelProperty(value = "邮箱",index = 3)
    private String email="";
    @ExcelProperty(value = "性别",index = 4)
    private String sex="";

    public String getGourpId() {
        return gourpId;
    }

    public void setGourpId(String gourpId) {
        this.gourpId = gourpId;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


}
