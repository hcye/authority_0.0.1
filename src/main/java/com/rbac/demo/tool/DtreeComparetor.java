package com.rbac.demo.tool;

import com.rbac.demo.dtree.Dtree;

import java.util.Comparator;

public class DtreeComparetor implements Comparator<Dtree> {
    @Override
    public int compare(Dtree o1, Dtree o2) {
        return o1.getId()-o2.getId();
    }
}
