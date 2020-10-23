package com.rbac.demo.entity;

import java.util.ArrayList;
import java.util.List;

public class AsmAction {
    public static final String dev_borrow="借用";
    public static final String dev_in="入库";
    public static final String dev_return="归还";
    public static final String dev_edit="编辑资产";
    public static final String dev_dam="资产报损";
    public static final String dev_add_type="增加资产类型";
    public static final String dev_del="删除资产";


    public static List<String> getList(){
        List<String> list=new ArrayList<>();
        list.add("全部操作");
        list.add(dev_borrow);
        list.add(dev_add_type);
        list.add(dev_in);
        list.add(dev_dam);
        list.add(dev_edit);
        list.add(dev_return);
        list.add(dev_del);
        return list;
    }
}
