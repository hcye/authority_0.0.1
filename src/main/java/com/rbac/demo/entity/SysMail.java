package com.rbac.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "sys_mail", schema = "mydb1", catalog = "")
public class SysMail {
    private int id;
    private String host;
    private String senderAddr;
    private String senderAccont;
    private String senderPwd;
    private String remark;
    private String forwhat;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "host")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Basic
    @Column(name = "forwhat")
    public String getForwhat() {
        return forwhat;
    }

    public void setForwhat(String forwhat) {
        this.forwhat = forwhat;
    }

    @Basic
    @Column(name = "senderAddr")
    public String getSenderAddr() {
        return senderAddr;
    }

    public void setSenderAddr(String senderAddr) {
        this.senderAddr = senderAddr;
    }

    @Basic
    @Column(name = "senderAccont")
    public String getSenderAccont() {
        return senderAccont;
    }

    public void setSenderAccont(String senderAccont) {
        this.senderAccont = senderAccont;
    }

    @Basic
    @Column(name = "senderPwd")
    public String getSenderPwd() {
        return senderPwd;
    }

    public void setSenderPwd(String senderPwd) {
        this.senderPwd = senderPwd;
    }

    @Basic
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysMail sysMail = (SysMail) o;

        if (id != sysMail.id) return false;
        if (host != null ? !host.equals(sysMail.host) : sysMail.host != null) return false;
        if (senderAddr != null ? !senderAddr.equals(sysMail.senderAddr) : sysMail.senderAddr != null) return false;
        if (senderAccont != null ? !senderAccont.equals(sysMail.senderAccont) : sysMail.senderAccont != null)
            return false;
        if (senderPwd != null ? !senderPwd.equals(sysMail.senderPwd) : sysMail.senderPwd != null) return false;
        if (remark != null ? !remark.equals(sysMail.remark) : sysMail.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + (senderAddr != null ? senderAddr.hashCode() : 0);
        result = 31 * result + (senderAccont != null ? senderAccont.hashCode() : 0);
        result = 31 * result + (senderPwd != null ? senderPwd.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }
}
