package com.rbac.hcye_admin.controller.restController;

import com.rbac.hcye_admin.dtree.Dtree;
import com.rbac.hcye_admin.dtree.imp.GroupDtreeList;
import com.rbac.hcye_admin.entity.Assert;
import com.rbac.hcye_admin.entity.Employee;
import com.rbac.hcye_admin.entity.SysGroup;
import com.rbac.hcye_admin.jpa.JpaAssert;
import com.rbac.hcye_admin.jpa.JpaGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
    @Autowired
    private JpaAssert jpaAssert;
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

//    $.post("/group/edit/save", {did:$("#did").val(),upper_group:$("#sysGroup").val()
//            ,depName:$("#depName").val(),leader:$("#name").val(),dep_status:$("#dep_st").val()},



    @PostMapping("/group/edit/save")
    public Map<String,String> valid_dep_edit(int did,String upper_group,String depName,String leader,String dep_status){
        Map<String,String> map=new HashMap<>();
        depName=depName.trim();
        SysGroup group=jpaGroup.findById(did).get();
        if(!upper_group.equals("") && upper_group.contains("-")){
            String depId=upper_group.split("-")[0];
            SysGroup uppergroup=jpaGroup.findById(Integer.parseInt(depId)).get();
            group.setSysGroupByParentId(uppergroup);
        }
        // SysGroup uppergroup=jpaGroup.findSysGroupByGname(upperDep);
        group.setLeader(leader);
        group.setGname(depName);
        if(dep_status.equals("可用")){
            group.setAvalible((byte) 1);
        }else {
            group.setAvalible((byte) 0);
        }
        List<Assert> asserts= (List<Assert>) group.getAssertsById();
        for(Assert an:asserts){
            an.setSysGroupName(group.getGname());
            jpaAssert.save(an);
        }
        jpaGroup.saveAndFlush(group);
        map.put("success","");
        return map;

    }
    @PostMapping("/group/edit/valid")
    public Map<String,String> valid_dep_edit(int did,String upper_group,String depName){
        Map<String,String> map=new HashMap<>();
        depName=depName.trim();
        SysGroup group=jpaGroup.findById(did).get();
        if(!upper_group.equals("") && upper_group.contains("-")){
           String depId=upper_group.split("-")[0];
            SysGroup uppergroup=jpaGroup.findById(Integer.parseInt(depId)).get();
            if(depName!=null && !depName.equals("")){
                List<SysGroup> sysGroups= (List<SysGroup>) uppergroup.getSysGroupsById();
                for(SysGroup sysGroup:sysGroups){
                    if(depName.equals(sysGroup.getGname()) && sysGroup.getId()!=did){
                        map.put("error","同级部门名不能相同");
                        return map;
                    }
                }
            }else {
                map.put("error","部门名不为空");
                return map;
            }

        }
        map.put("success","");
        return map;

    }






}
