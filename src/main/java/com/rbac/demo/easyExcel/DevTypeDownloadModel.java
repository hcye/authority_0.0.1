package com.rbac.demo.easyExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;


public class DevTypeDownloadModel extends BaseRowModel {
    @ExcelProperty(value = "设备名称",index = 0)
    private String devName="";
    @ExcelProperty(value = "备注",index = 4)
    private String remarks="";
    @ExcelProperty(value = "创建日期",index = 3)
    private String createTime="";
    @ExcelProperty(value = "创建人",index = 2)
    private String creator="";
    @ExcelProperty(value = "编号模板",index = 1)
    private String assetNumTemplate="";

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getAssetNumTemplate() {
        return assetNumTemplate;
    }

    public void setAssetNumTemplate(String assetNumTemplate) {
        this.assetNumTemplate = assetNumTemplate;
    }
}
