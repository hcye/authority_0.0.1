package com.rbac.hcye_admin.controller.group;

import com.rbac.hcye_admin.entity.Employee;
import com.rbac.hcye_admin.entity.SysGroup;
import com.rbac.hcye_admin.jpa.JpaEmployee;
import com.rbac.hcye_admin.jpa.JpaGroup;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GroupController {
    @Autowired
    private JpaEmployee jpaEmployee;
    @Autowired
    private JpaGroup jpaGroup;
    @RequiresPermissions("asm:group:edit")
    @GetMapping("/group/edit")
    public String edit(int id, Model model){
        //本部门
        SysGroup group =jpaGroup.findById(id).get();
        String currentGroupName=group.getGname();
        //上级部门
        SysGroup parent=group.getSysGroupByParentId();
        String groupNames="";
        if(parent!=null){
            String parentGroupName=parent.getGname();
            groupNames=group.getSysGroupByParentId().getId()+"-"+group.getSysGroupByParentId().getGname();
        }

        //


        //移除本部门，把当前父部门调整到队列头


        List<String> status=new ArrayList<>();
        if(group.getAvalible()==1){
            status.add(0,"可用");
            status.add(1,"禁用");
        }else {
            status.add(0,"禁用");
            status.add(1,"可用");
        }
        String leader=group.getLeader();

        Employee lead;
        if(leader==null||leader.equals("")){
            lead=new Employee();
        }else {
            String loginName=leader.split("-")[1];
            lead=jpaEmployee.findEmployeeByLoginName(loginName);
        }
        model.addAttribute("leader",lead);
        model.addAttribute("status",status);
        model.addAttribute("parent",groupNames);
        model.addAttribute("group",group);
        return "group/edit";
    }
  /*  @RequestMapping("/group/edit/setLeader")
    public String setLeader(int id,String name){
        SysGroup group =jpaGroup.findById(id).get();
        Employee leader=jpaEmployee.findEmployeesByEname(name).get(0);
        group.setLeader(leader.getEname());
        jpaGroup.saveAndFlush(group);
        return "redirect:/group/edit?id="+id;
    }*/
  @RequiresPermissions("asm:group:edit")
    @PostMapping("/group/edit/saveChange")
    public String save(int did,String upperDep,String depName,String leader,String status){
        SysGroup group=jpaGroup.findById(did).get();
        if(!upperDep.equals("") && upperDep.contains("-")){
            String depId=upperDep.split("-")[0];
            SysGroup uppergroup=jpaGroup.findById(Integer.parseInt(depId)).get();
            group.setSysGroupByParentId(uppergroup);
        }
       // SysGroup uppergroup=jpaGroup.findSysGroupByGname(upperDep);
        group.setLeader(leader);
        group.setGname(depName);
        if(status.equals("可用")){
            group.setAvalible((byte) 1);
        }else {
            group.setAvalible((byte) 0);
        }

        jpaGroup.saveAndFlush(group);
        return "group/group";
    }
    @RequiresPermissions("asm:group:delete")
    @GetMapping("/group/delete")
    public String delete(int id, Model model){
        SysGroup group =jpaGroup.findById(id).get();
        List<Employee> employees= (List<Employee>) group.getEmployeesById();
        if(group.getEmployeesById().isEmpty()){
            jpaGroup.delete(group);
        }else {
            for(Employee e:employees){
                if(e.getOnjob().equals("0")){
                    throw new RuntimeException("部门内有对象无法删除");
                }
            }
            jpaEmployee.deleteAll(employees);
            jpaEmployee.flush();
            jpaGroup.delete(group);
         //   throw new RuntimeException("部门内有对象无法删除");
        }
     //   jpaGroup.saveAndFlush(group);
        return "group/group";
    }
    @RequiresPermissions("asm:group:add")
    @GetMapping("/group/addPage")
    public String jumpToAddPage(Model model){
        List<String> names = jpaGroup.getDistinctGourpName();
        model.addAttribute("names",names);
        return "group/add";
    }
    @RequiresPermissions("asm:group:add")
    @GetMapping("/group/add")
    public String add(String upperDep, String depName, String leader, String status, Model model){
        if(depName==null||depName.equals("")||upperDep.equals("")){
            List<String> names = jpaGroup.getDistinctGourpName();
            model.addAttribute("names",names);
            model.addAttribute("flag","×");
            model.addAttribute("errPrompt","部门名不为空！");
            return "group/add";
        }
        SysGroup newGroup=new SysGroup();
        String groupId=upperDep.split("-")[0];
        SysGroup upperGroup =jpaGroup.findById(Integer.parseInt(groupId)).get();
        newGroup.setGname(depName);
        newGroup.setSysGroupByParentId(upperGroup);
        newGroup.setLeader(leader);
        newGroup.setCreatTime(new Timestamp(new java.util.Date().getTime()));
        String creatorName = SecurityUtils.getSubject().getPrincipal().toString();
        Employee creator=jpaEmployee.findEmployeeByLoginName(creatorName);
        newGroup.setEmployeeByCreatorId(creator);
        //
        if(status.equals("正常")) {
            newGroup.setAvalible((byte) 1);
        }else {
            newGroup.setAvalible((byte) 0);
        }
        jpaGroup.saveAndFlush(newGroup);
        return "group/group";
    }

    @RequiresPermissions("asm:group:view")
    @GetMapping("/group/group")
    public String group(){
        return "group/group";
    }

    @RequiresPermissions("asm:group:view")
    @GetMapping("/group/selectGroupLeader")
    public String getLeader(){
        return "layer/layer_edit_groupLeader";
    }
}
