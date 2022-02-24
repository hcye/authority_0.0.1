package com.rbac.demo.easyExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import org.springframework.stereotype.Component;

@Component
public class AssetDownloadModel extends BaseRowModel {
    @ExcelProperty(value = "资产编号",index = 0)
    private String assetNum="";
    @ExcelProperty(value = "设备名称",index = 1)
    private String devName="";
    @ExcelProperty(value = "型号",index=2)
    private String model="";
    @ExcelProperty(value = "供应商",index = 3)
    private String provider="";
    @ExcelProperty(value = "主体ID",index = 4)
    private String sysGroup="";
    @ExcelProperty(value = "SN号",index = 5)
    private String snNum="";
    @ExcelProperty(value = "单价",index = 6)
    private String price="";
    @ExcelProperty(value = "到库时间",index = 7)
    private String putinTime="";
    @ExcelProperty(value = "借用人",index = 8)
    private String borrower="";
    @ExcelProperty(value = "借用时间",index = 9)
    private String borTime="";
    @ExcelProperty(value = "备注",index = 10)
    private String remarks="";
    @ExcelProperty(value = "报废",index = 11)
    private String workless="";
    @ExcelProperty(value = "资产类型",index = 12)
    private String assetType="";




    @Override
    public String toString() {
        return "EasyExcelMapedModel{" +
                "assetNum='" + assetNum + '\'' +
                ", devName='" + devName + '\'' +
                ", model='" + model + '\'' +
                ", snNum='" + snNum + '\'' +
                ", price='" + price + '\'' +
                ", putinTime='" + putinTime + '\'' +
                ", borrower='" + borrower + '\'' +
                ", borTime='" + borTime + '\'' +
                ", remarks='" + remarks + '\'' +
                ", workless='" + workless + '\'' +
                ", assetType='" + assetType + '\'' +
                '}';
    }


    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getSysGroup() {
        return sysGroup;
    }

    public void setSysGroup(String sysGroup) {
        this.sysGroup = sysGroup;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }


    public AssetDownloadModel() {
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getAssetNum() {
        return assetNum;
    }

    public void setAssetNum(String assetNum) {
        this.assetNum = assetNum;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPutinTime() {
        return putinTime;
    }

    public void setPutinTime(String putinTime) {
        this.putinTime = putinTime;
    }

    public String getSnNum() {
        return snNum;
    }

    public void setSnNum(String snNum) {
        this.snNum = snNum;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getBorTime() {
        return borTime;
    }

    public void setBorTime(String borTime) {
        this.borTime = borTime;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public String getWorkless() {
        return workless;
    }
    public void setWorkless(String workless) {
        this.workless = workless;
    }




}
