package com.rbac.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "echange_devs", schema = "mydb1", catalog = "")
public class EchangeDevs {
    private int id;
    private Timestamp sendTime;
    private String reason="";
    private Employee senderFK;
    private Assert devFK;
    private String received="";
    private Timestamp receviedTime;
    private String isDone="0";






    private Employee resiverFK;

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
    @Column(name = "recevied_time")
    public Timestamp getReceviedTime() {
        return receviedTime;
    }
    @Basic
    @Column(name = "is_done")
    public String getIsDone() {
        return isDone;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }

    public void setReceviedTime(Timestamp receviedTime) {
        this.receviedTime = receviedTime;
    }

    @Basic
    @Column(name = "send_time")
    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    @Basic
    @Column(name = "received")
    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }


    @Basic
    @Column(name = "reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EchangeDevs that = (EchangeDevs) o;

        if (id != that.id) return false;
        if (sendTime != null ? !sendTime.equals(that.sendTime) : that.sendTime != null) return false;
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (sendTime != null ? sendTime.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sender_emp", referencedColumnName = "id")
    public Employee getSenderFK() {
        return senderFK;
    }

    public void setSenderFK(Employee senderFK) {
        this.senderFK = senderFK;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "recive_emp", referencedColumnName = "id")
    public Employee getResiverFK() {
        return resiverFK;
    }

    public void setResiverFK(Employee resiverFK) {
        this.resiverFK = resiverFK;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "exchange_dev", referencedColumnName = "id")
    public Assert getDevFK() {
        return devFK;
    }

    public void setDevFK(Assert devFK) {
        this.devFK = devFK;
    }
}
