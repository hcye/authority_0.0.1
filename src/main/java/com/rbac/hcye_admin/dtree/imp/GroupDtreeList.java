package com.rbac.hcye_admin.dtree.imp;

import com.rbac.hcye_admin.dtree.Dtree;
import com.rbac.hcye_admin.dtree.inter.DtreeListInter;
import com.rbac.hcye_admin.entity.SysGroup;
import com.rbac.hcye_admin.jpa.JpaGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@Component
public class GroupDtreeList implements DtreeListInter {
    @Autowired
    private JpaGroup jpaGroup;
    @Override
    public List<Dtree> getDtree() {
        List<SysGroup> list=jpaGroup.findAllExcludDeleted();
        List<Dtree> dtrees=new ArrayList<>();

        for (SysGroup group:list){
            Dtree dtree=new Dtree();
            dtree.setId(group.getId());
            SysGroup groupByParentId=group.getSysGroupByParentId();
            if(groupByParentId!=null){
                dtree.setParentId(groupByParentId.getId());
            }else if(groupByParentId==null){
                dtree.setParentId(0);
            }
            dtree.setTitle(group.getGname());
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
        List<Dtree> res=new ArrayList<>();
        for (Dtree dtree:dtrees){
            if(dtree.getParentId()==0){
                res.add(dtree);
            }
        }
        return res;
    }
}
