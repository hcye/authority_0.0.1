package com.rbac.demo.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rtc_bug", schema = "mydb1", catalog = "")
public class RtcBug {
    private int id;
    private String bugFlag;
    private String bugSrc;
    private String bugModule;
    private String bugSeriousness;
    private String bugPriority;
    private String bugFrequence;
    private String bugRelative;
    private String bugSummary;
    private String bugDesc;
    private String bugStatus;
    private String bugCreater;
    private String bugOwner;
    private String bugVersion;
    private String bugClassfy1;
    private String bugClassfy2;
    private String bugDefectType;
    private String bugDiscoveryPhase;
    private String bugCate;
    private String bugType;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "bug_flag", nullable = true, length = 255)
    public String getBugFlag() {
        return bugFlag;
    }

    public void setBugFlag(String bugFlag) {
        this.bugFlag = bugFlag;
    }

    @Basic
    @Column(name = "bug_src", nullable = true, length = 255)
    public String getBugSrc() {
        return bugSrc;
    }

    public void setBugSrc(String bugSrc) {
        this.bugSrc = bugSrc;
    }

    @Basic
    @Column(name = "bug_module", nullable = true, length = 255)
    public String getBugModule() {
        return bugModule;
    }

    public void setBugModule(String bugModule) {
        this.bugModule = bugModule;
    }

    @Basic
    @Column(name = "bug_seriousness", nullable = true, length = 255)
    public String getBugSeriousness() {
        return bugSeriousness;
    }

    public void setBugSeriousness(String bugSeriousness) {
        this.bugSeriousness = bugSeriousness;
    }

    @Basic
    @Column(name = "bug_priority", nullable = true, length = 255)
    public String getBugPriority() {
        return bugPriority;
    }

    public void setBugPriority(String bugPriority) {
        this.bugPriority = bugPriority;
    }

    @Basic
    @Column(name = "bug_frequence", nullable = true, length = 255)
    public String getBugFrequence() {
        return bugFrequence;
    }

    public void setBugFrequence(String bugFrequence) {
        this.bugFrequence = bugFrequence;
    }

    @Basic
    @Column(name = "bug_relative", nullable = true, length = 255)
    public String getBugRelative() {
        return bugRelative;
    }

    public void setBugRelative(String bugRelative) {
        this.bugRelative = bugRelative;
    }

    @Basic
    @Column(name = "bug_summary", nullable = true, length = 255)
    public String getBugSummary() {
        return bugSummary;
    }

    public void setBugSummary(String bugSummary) {
        this.bugSummary = bugSummary;
    }

    @Basic
    @Column(name = "bug_desc", nullable = true, length = 1024)
    public String getBugDesc() {
        return bugDesc;
    }

    public void setBugDesc(String bugDesc) {
        this.bugDesc = bugDesc;
    }

    @Basic
    @Column(name = "bug_status", nullable = true, length = 255)
    public String getBugStatus() {
        return bugStatus;
    }

    public void setBugStatus(String bugStatus) {
        this.bugStatus = bugStatus;
    }

    @Basic
    @Column(name = "bug_creater", nullable = true, length = 255)
    public String getBugCreater() {
        return bugCreater;
    }

    public void setBugCreater(String bugCreater) {
        this.bugCreater = bugCreater;
    }

    @Basic
    @Column(name = "bug_owner", nullable = true, length = 255)
    public String getBugOwner() {
        return bugOwner;
    }

    public void setBugOwner(String bugOwner) {
        this.bugOwner = bugOwner;
    }

    @Basic
    @Column(name = "bug_version", nullable = true, length = 255)
    public String getBugVersion() {
        return bugVersion;
    }

    public void setBugVersion(String bugVersion) {
        this.bugVersion = bugVersion;
    }

    @Basic
    @Column(name = "bug_classfy1", nullable = true, length = 255)
    public String getBugClassfy1() {
        return bugClassfy1;
    }

    public void setBugClassfy1(String bugClassfy1) {
        this.bugClassfy1 = bugClassfy1;
    }

    @Basic
    @Column(name = "bug_classfy2", nullable = true, length = 255)
    public String getBugClassfy2() {
        return bugClassfy2;
    }

    public void setBugClassfy2(String bugClassfy2) {
        this.bugClassfy2 = bugClassfy2;
    }

    @Basic
    @Column(name = "bug_defect_type", nullable = true, length = 255)
    public String getBugDefectType() {
        return bugDefectType;
    }

    public void setBugDefectType(String bugDefectType) {
        this.bugDefectType = bugDefectType;
    }

    @Basic
    @Column(name = "bug_discovery_phase", nullable = true, length = 255)
    public String getBugDiscoveryPhase() {
        return bugDiscoveryPhase;
    }

    public void setBugDiscoveryPhase(String bugDiscoveryPhase) {
        this.bugDiscoveryPhase = bugDiscoveryPhase;
    }

    @Basic
    @Column(name = "bug_cate", nullable = true, length = 255)
    public String getBugCate() {
        return bugCate;
    }

    public void setBugCate(String bugCate) {
        this.bugCate = bugCate;
    }

    @Basic
    @Column(name = "bug_type", nullable = true, length = 255)
    public String getBugType() {
        return bugType;
    }

    public void setBugType(String bugType) {
        this.bugType = bugType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RtcBug rtcBug = (RtcBug) o;
        return id == rtcBug.id &&
                Objects.equals(bugFlag, rtcBug.bugFlag) &&
                Objects.equals(bugSrc, rtcBug.bugSrc) &&
                Objects.equals(bugModule, rtcBug.bugModule) &&
                Objects.equals(bugSeriousness, rtcBug.bugSeriousness) &&
                Objects.equals(bugPriority, rtcBug.bugPriority) &&
                Objects.equals(bugFrequence, rtcBug.bugFrequence) &&
                Objects.equals(bugRelative, rtcBug.bugRelative) &&
                Objects.equals(bugSummary, rtcBug.bugSummary) &&
                Objects.equals(bugDesc, rtcBug.bugDesc) &&
                Objects.equals(bugStatus, rtcBug.bugStatus) &&
                Objects.equals(bugCreater, rtcBug.bugCreater) &&
                Objects.equals(bugOwner, rtcBug.bugOwner) &&
                Objects.equals(bugVersion, rtcBug.bugVersion) &&
                Objects.equals(bugClassfy1, rtcBug.bugClassfy1) &&
                Objects.equals(bugClassfy2, rtcBug.bugClassfy2) &&
                Objects.equals(bugDefectType, rtcBug.bugDefectType) &&
                Objects.equals(bugDiscoveryPhase, rtcBug.bugDiscoveryPhase) &&
                Objects.equals(bugCate, rtcBug.bugCate) &&
                Objects.equals(bugType, rtcBug.bugType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bugFlag, bugSrc, bugModule, bugSeriousness, bugPriority, bugFrequence, bugRelative, bugSummary, bugDesc, bugStatus, bugCreater, bugOwner, bugVersion, bugClassfy1, bugClassfy2, bugDefectType, bugDiscoveryPhase, bugCate, bugType);
    }
}
