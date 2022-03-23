package com.rbac.hcye_admin.dtree.imp;

import com.rbac.hcye_admin.dtree.Dtree;
import com.rbac.hcye_admin.entity.Resources;
import com.rbac.hcye_admin.entity.Role;
import com.rbac.hcye_admin.entity.Role2Resources;
import com.rbac.hcye_admin.jpa.JpaResources;
import com.rbac.hcye_admin.jpa.JpaRole2Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@Component
public class ResourceDtreeList {
    @Autowired
    private JpaRole2Resources jpaRole2Resources;
    @Autowired
    private JpaResources jpaResources;
    public List<Dtree> getDtree(Role role){
        List<Role2Resources> role2ResourcesList;
        List<Resources> selectedResource = null;
        if(role!=null){
            role2ResourcesList=jpaRole2Resources.findRole2ResourcesByRoleByRoleId(role);
            selectedResource=new ArrayList<>();
            for (Role2Resources role2Resources:role2ResourcesList){
                selectedResource.add(role2Resources.getResourcesByMenusId());
            }
        }
        List<Resources> list=jpaResources.findAll();



        List<Dtree> dtrees=new ArrayList<>();
        //-----------把group转换成tree对象------------------
        for (Resources resources:list){

            Dtree dtree=new Dtree();
            dtree.setId(resources.getId());
            if(role!=null){
                for(Resources resources1:selectedResource){
                    if(resources.getId()==resources1.getId()){
                        dtree.setCheckArr("1");
                    }
                }
            }
            Resources resourceByParentId=resources.getResourcesByParentId();
            if(resourceByParentId!=null){
                dtree.setParentId(resourceByParentId.getId());
            }else if(resourceByParentId==null){
                dtree.setParentId(0);
            }
            dtree.setTitle(resources.getDescription());
            dtrees.add(dtree);
        }
        //-----------------------------
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
