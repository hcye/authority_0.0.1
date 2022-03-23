package com.rbac.hcye_admin.tool;

import com.rbac.hcye_admin.dtree.Dtree;

import java.util.Comparator;

public class DtreeComparetor implements Comparator<Dtree> {
    @Override
    public int compare(Dtree o1, Dtree o2) {
        return o1.getId()-o2.getId();
    }
}
