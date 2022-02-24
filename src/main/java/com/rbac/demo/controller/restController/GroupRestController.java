package com.rbac.demo.controller.restController;

import com.rbac.demo.dtree.Dtree;
import com.rbac.demo.dtree.imp.GroupDtreeList;
import com.rbac.demo.entity.SysGroup;
import com.rbac.demo.jpa.JpaGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GroupRestController {
    @Autowired
    private GroupDtreeList groupDtreeList;
    @Autowired
    private JpaGroup jpaGroup;
    @PostMapping("/group/delete/valid")
    public Map<String,String> valid(int did){
        Map<String,String> map=new HashMap<>();
        SysGroup group =jpaGroup.findById(did).get();
        if(group.getEmployeesById().isEmpty()) {
            map.put("msg","ok");
            return map;
        }else {
            map.put("msg","false");
            return map;
        }
    }
    @PostMapping("/getGroupTree")
    public List<Dtree> getTree(){
        return groupDtreeList.getDtree();
    }
    @PostMapping("/queryDepInfo")
    public Map<String,String> getInfo(String did,String parentId){
/*        parentId 是本部门id
        did 是选择的上级部门id*/
        Map<String,String> map=new HashMap<>();
        int id=0;
        int pid=0;
        try {
            id=Integer.parseInt(did);
            pid=Integer.parseInt(parentId);
        }catch (Exception e){
            System.out.println(e+"后台传回部门id格式异常");
        }
        SysGroup group=jpaGroup.findById(id).get();
        if(pid!=0){
            SysGroup cuGroup=jpaGroup.findById(pid).get();
            if(id==pid){
                map.put("error","上级部门不能是自己！");
                return(map);
            }
            List<SysGroup> sysGroups= (List<SysGroup>) cuGroup.getSysGroupsById();
            for(SysGroup sysGroup:sysGroups){
                System.out.println(sysGroup.getGname());
            }
            if(!sysGroups.isEmpty()){
                boolean flag=false;
                for(SysGroup sysGroup:sysGroups){
                    //      System.out.println(pid+"-"+sysGroup.getId());
                    if(id==sysGroup.getId()){
                        flag=true;
                        break;
                    }
                }
                if(flag==true){
                    map.put("error","上级部门不能设置为子部门！");
                    return(map);
                }
            }

        }
        int count=jpaGroup.getEmpCountOfGroup(group);
        map.put("id",group.getId()+"");
        map.put("gname",group.getGname());
        map.put("createTime",new Date(group.getCreatTime().getTime()).toString());
        map.put("avalible",group.getAvalible()+"");
        String name;
        if(group.getSysGroupByParentId()==null){
            name="";
        }else {
            name=group.getSysGroupByParentId().getGname();
        }
        map.put("parent",name);
        map.put("leader",group.getLeader());
        map.put("count",count+"");
        return map;
    }

}
