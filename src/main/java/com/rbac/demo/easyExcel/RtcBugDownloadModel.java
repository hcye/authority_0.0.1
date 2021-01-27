package com.rbac.demo.easyExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.stereotype.Component;

@Component
public class RtcBugDownloadModel {
    @ExcelProperty(value = "标识",index = 0)
    private String bugFlag;
    @ExcelProperty(value = "课题来源",index = 1)
    private String bugSrc;
    @ExcelProperty(value = "所属模块【必填】",index = 2)
    private String bugModule;
    @ExcelProperty(value = "严重性",index = 3)
    private String bugSeriousness;
    @ExcelProperty(value = "优先级",index = 4)
    private String bugPriority;
    @ExcelProperty(value = "出现频率",index = 5)
    private String bugFrequence;
    @ExcelProperty(value = "关联用例【必填】",index = 6)
    private String bugRelative;
    @ExcelProperty(value = "摘要【必填】",index = 7)
    private String bugSummary;
    @ExcelProperty(value = "描述【必填】",index = 8)
    private String bugDesc;
    @ExcelProperty(value = "状态",index = 9)
    private String bugStatus;
    @ExcelProperty(value = "创建者",index = 10)
    private String bugCreater;
    @ExcelProperty(value = "所有者",index = 11)
    private String bugOwner;
    @ExcelProperty(value = "发现版本【必填】",index = 12)
    private String bugVersion;
    @ExcelProperty(value = "分类1",index = 13)
    private String bugClassfy1;
    @ExcelProperty(value = "分类2",index = 14)
    private String bugClassfy2;
    @ExcelProperty(value = "缺陷类型",index = 15)
    private String bugDefectType;
    @ExcelProperty(value = "发现阶段",index = 16)
    private String bugDiscoveryPhase;
    @ExcelProperty(value = "类别",index = 17)
    private String bugCate;
    @ExcelProperty(value = "类型",index = 18)
    private String bugType;

    public String getBugFlag() {
        return bugFlag;
    }

    public void setBugFlag(String bugFlag) {
        this.bugFlag = bugFlag;
    }
    public String getBugSrc() {
        return bugSrc;
    }

    public void setBugSrc(String bugSrc) {
        this.bugSrc = bugSrc;
    }

    public String getBugModule() {
        return bugModule;
    }

    public void setBugModule(String bugModule) {
        this.bugModule = bugModule;
    }

    public String getBugSeriousness() {
        return bugSeriousness;
    }

    public void setBugSeriousness(String bugSeriousness) {
        this.bugSeriousness = bugSeriousness;
    }

    public String getBugPriority() {
        return bugPriority;
    }

    public void setBugPriority(String bugPriority) {
        this.bugPriority = bugPriority;
    }

    public String getBugFrequence() {
        return bugFrequence;
    }

    public void setBugFrequence(String bugFrequence) {
        this.bugFrequence = bugFrequence;
    }

    public String getBugRelative() {
        return bugRelative;
    }

    public void setBugRelative(String bugRelative) {
        this.bugRelative = bugRelative;
    }

    public String getBugSummary() {
        return bugSummary;
    }

    public void setBugSummary(String bugSummary) {
        this.bugSummary = bugSummary;
    }

    public String getBugDesc() {
        return bugDesc;
    }

    public void setBugDesc(String bugDesc) {
        this.bugDesc = bugDesc;
    }

    public String getBugStatus() {
        return bugStatus;
    }

    public void setBugStatus(String bugStatus) {
        this.bugStatus = bugStatus;
    }

    public String getBugCreater() {
        return bugCreater;
    }

    public void setBugCreater(String bugCreater) {
        this.bugCreater = bugCreater;
    }

    public String getBugOwner() {
        return bugOwner;
    }

    public void setBugOwner(String bugOwner) {
        this.bugOwner = bugOwner;
    }

    public String getBugVersion() {
        return bugVersion;
    }

    public void setBugVersion(String bugVersion) {
        this.bugVersion = bugVersion;
    }

    public String getBugClassfy1() {
        return bugClassfy1;
    }

    public void setBugClassfy1(String bugClassfy1) {
        this.bugClassfy1 = bugClassfy1;
    }

    public String getBugClassfy2() {
        return bugClassfy2;
    }

    public void setBugClassfy2(String bugClassfy2) {
        this.bugClassfy2 = bugClassfy2;
    }

    public String getBugDefectType() {
        return bugDefectType;
    }

    public void setBugDefectType(String bugDefectType) {
        this.bugDefectType = bugDefectType;
    }

    public String getBugDiscoveryPhase() {
        return bugDiscoveryPhase;
    }

    public void setBugDiscoveryPhase(String bugDiscoveryPhase) {
        this.bugDiscoveryPhase = bugDiscoveryPhase;
    }

    public String getBugCate() {
        return bugCate;
    }

    public void setBugCate(String bugCate) {
        this.bugCate = bugCate;
    }

    public String getBugType() {
        return bugType;
    }

    public void setBugType(String bugType) {
        this.bugType = bugType;
    }


}
