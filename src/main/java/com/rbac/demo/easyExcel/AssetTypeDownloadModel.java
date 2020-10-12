package com.rbac.demo.easyExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class AssetTypeDownloadModel extends BaseRowModel {
    @ExcelProperty(value = "类型名称",index = 0)
    private String tname;
    @ExcelProperty(value = "编码模板",index = 1)
    private String template;
    @ExcelProperty(value = "创建时间",index=2)
    private String createTime;
    @ExcelProperty(value = "权限标识",index = 3)
    private String sign;
    @ExcelProperty(value = "备注",index = 4)
    private String remark;


    @Override
    public String toString() {
        return "AssetTypeDownloadModel{" +
                "tname='" + tname + '\'' +
                ", template='" + template + '\'' +
                ", createTime='" + createTime + '\'' +
                ", sign='" + sign + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
