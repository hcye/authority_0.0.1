package com.rbac.demo.dtree;

import com.rbac.demo.entity.Menus;
import com.rbac.demo.jpa.JpaMenus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class GetDtreeList {
    @Autowired
    JpaMenus jpaMenus;
    public List<Dtree> getDtrees( List<Menus> list1){
        List<Menus> list=jpaMenus.findAll();
        List<Dtree> dtrees=new ArrayList<>();
        for (Menus menus:list){
            Dtree dtree=new Dtree();
            dtree.setId(menus.getId());
            if(menus.getMenusByFatherId()!=null){
                dtree.setParentId(menus.getMenusByFatherId().getId());
            }
            dtree.setTitle(menus.getMenuName());
            dtrees.add(dtree);
        }
        dtrees.sort(new Comparator<Dtree>() {
            @Override
            public int compare(Dtree o1, Dtree o2) {
                return o1.getParentId()-o2.getParentId();
            }
        });

        for (int i=0;i<dtrees.size();i++){
            for (int j=0;j<dtrees.size();j++){
                if(dtrees.get(i).getId()==dtrees.get(j).getParentId()){
                    dtrees.get(i).getChildren().add(dtrees.get(j));
                }
            }
        }
        List<Dtree> list2=new ArrayList<>();
        for (Menus  menus:list1){
            for (Dtree dtree:dtrees){
                if(menus.getId()==dtree.getId()){
                    list2.add(dtree);
                }
            }
        }
        return list2;
    }
}
