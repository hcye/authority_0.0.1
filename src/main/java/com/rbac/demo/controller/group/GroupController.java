package com.rbac.demo.controller.group;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.SysGroup;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.jpa.JpaGroup;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
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
    @RequestMapping("/group/edit")
    public String edit(int id, Model model){
        //本部门
        SysGroup group =jpaGroup.findById(id).get();
        String currentGroupName=group.getGname();
        //上级部门
        SysGroup parent=group.getSysGroupByParentId();
        String parentGroupName=parent.getGname();
        //
        List<String> groupNames=jpaGroup.getDistinctGourpName();
        //移除本部门，把当前父部门调整到队列头
        groupNames.remove(currentGroupName);
        groupNames.remove(parentGroupName);
        groupNames.add(0,parentGroupName);

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
        if(leader==null){
            lead=new Employee();
        }else {
            lead=jpaEmployee.findEmployeesByEname(leader).get(0);
        }
        model.addAttribute("leader",lead);
        model.addAttribute("status",status);
        model.addAttribute("parent",groupNames);
        model.addAttribute("group",group);
        return "/group/edit";
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
        SysGroup uppergroup=jpaGroup.findSysGroupByGname(upperDep);
        group.setLeader(leader);
        group.setGname(depName);
        group.setSysGroupByParentId(uppergroup);
        if(status.equals("可用")){
            group.setAvalible((byte) 1);
        }else {
            group.setAvalible((byte) 0);
        }

        jpaGroup.saveAndFlush(group);
        return "/group/group";
    }
    @RequiresPermissions("asm:group:delete")
    @RequestMapping("/group/delete")
    public String delete(int id, Model model){
        SysGroup group =jpaGroup.findById(id).get();
        if(group.getEmployeesById().isEmpty()){
            group.setDeleteFlag((byte) 1);
        }else {
            throw new RuntimeException("部门内有对象无法删除");
        }
        jpaGroup.saveAndFlush(group);
        return "/group/group";
    }
    @RequiresPermissions("asm:group:add")
    @RequestMapping("/group/addPage")
    public String jumpToAddPage(Model model){
        List<String> names = jpaGroup.getDistinctGourpName();
        model.addAttribute("names",names);
        return "/group/add";
    }
    @RequiresPermissions("asm:group:add")
    @RequestMapping("/group/add")
    public String add(String upperDep, String depName, String leader, String status, Model model, HttpSession session){
        if(depName==null||depName.equals("")||jpaGroup.findSysGroupByGname(depName)!=null){
            List<String> names = jpaGroup.getDistinctGourpName();
            model.addAttribute("names",names);
            model.addAttribute("flag","×");
            model.addAttribute("errPrompt","部门名重复,添加失败！");
            return "/group/add";
        }
        SysGroup newGroup=new SysGroup();
        SysGroup upperGroup =jpaGroup.findSysGroupByGname(upperDep);
        newGroup.setGname(depName);
        newGroup.setSysGroupByParentId(upperGroup);
        newGroup.setLeader(leader);
        newGroup.setCreatTime(new Timestamp(new java.util.Date().getTime()));
        //
        String creatorName = SecurityUtils.getSubject().getPrincipal().toString();
        Employee creator=jpaEmployee.findEmployeesByEname(creatorName).get(0);
        newGroup.setEmployeeByCreatorId(creator);

        //
        if(status.equals("正常")) {
            newGroup.setAvalible((byte) 1);
        }else {
            newGroup.setAvalible((byte) 0);
        }
        jpaGroup.saveAndFlush(newGroup);
        return "/group/group";
    }

    @RequiresPermissions("asm:group:view")
    @GetMapping("/group/group")
    public String group(){
        return "/group/group";
    }

    @RequiresPermissions("asm:group:view")
    @GetMapping("/group/selectGroupLeader")
    public String getLeader(){
        return "/layer/layer_edit_groupLeader";
    }
}
