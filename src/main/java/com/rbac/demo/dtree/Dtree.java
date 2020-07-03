package com.rbac.demo.dtree;

import java.util.ArrayList;
import java.util.List;

public class Dtree implements Comparable<Dtree>{
    @Override
    public String toString() {
        return "Dtree{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", checkArr='" + checkArr + '\'' +
                ", parentId=" + parentId +
                ", children=" + children +
                '}';
    }

    private int id=0;
    private String title;
    private String checkArr="0";
    private int parentId;
    private List<Dtree> children=new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCheckArr() {
        return checkArr;
    }

    public void setCheckArr(String checkArr) {
        this.checkArr = checkArr;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public List<Dtree> getChildren() {
        return children;
    }

    public void setChildren(List<Dtree> children) {
        this.children = children;
    }




    @Override
    public int compareTo(Dtree o) {
        return o.getId()-id;
    }
}
