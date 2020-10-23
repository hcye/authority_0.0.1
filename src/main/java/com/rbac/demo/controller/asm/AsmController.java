package com.rbac.demo.controller.asm;
import com.rbac.demo.entity.*;
import com.rbac.demo.jpa.*;
import com.rbac.demo.service.AsmRecordService;
import com.rbac.demo.tool.ConvertStrForSearch;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class AsmController {
    private static final int pageSize=10;
    @Autowired
    private JpaAssetType jpaAssetType;
    @Autowired
    private JpaAssert jpaAssert;
    @Autowired
    private JpaResources jpaResources;
    @Autowired
    private JpaDevType jpaDevType;
    @Autowired
    private AsmRecordService asmRecordService;
    @Autowired
    private JpaOperatRecord jpaOperatRecord;

    @RequiresPermissions("asm:bro:view")
    @GetMapping("/asm/bro")
    public String broPage(Model model){
        //operator
        String loginUser= (String) SecurityUtils.getSubject().getPrincipal();  //操作者用户名
        List<AssetType> assertTypes = jpaAssetType.findAll();   //类型列表
        Pageable pageable=PageRequest.of(0,pageSize);   //初始化第一页
        List<String> assertNames=jpaAssert.getDistinctAssertNames(assertTypes.get(0));
        Page<Assert> asserts =jpaAssert.findAssertsByDevice(assertNames.get(0),pageable);  //初始化使用第一个类型，的第一个设备类型
        model.addAttribute("operator",loginUser);
        model.addAttribute("assertTypes",assertTypes);
        model.addAttribute("asserts",asserts);
        model.addAttribute("assertList",asserts.getContent());
        model.addAttribute("assertNames",assertNames);
        return "/asm/bro";
    }
    @RequiresPermissions("asm:ret:view")
    @GetMapping("/asm/ret")
    public String retPage(Model model){
        String loginUser= (String) SecurityUtils.getSubject().getPrincipal();  //操作者用户名
        model.addAttribute("operator",loginUser);
        return "/asm/ret";
    }

    @RequiresPermissions("asm:inp:view")
    @GetMapping("/asm/inp")
    public String inpPage(Model model){


        List<AssetType> types= jpaAssetType.findAssertType();
        String firstType=types.get(0).getTypeName();
        List<String> names=jpaDevType.findDevTypesNameByAssertType(firstType);
        DevType devType=jpaDevType.findDevTypeByDevName(names.get(0));
        String code=devType.getAssetNumTemplate();
        model.addAttribute("types",types);
        model.addAttribute("names",names);
        model.addAttribute("code",code);
        return "/asm/inp";
    }

    @RequiresPermissions("asm:list:view")
    @GetMapping("/asm/list")
    public String listPage(Model model){
        List<AssetType> types= jpaAssetType.findAssertType();
        model.addAttribute("types",types);
        return "/asm/list";
    }

    @RequiresPermissions("asm:type:view")
    @GetMapping("/asm/type")
    public String typePage(){

        return "/asm/type";
    }

    @RequiresPermissions("asm:devType:view")
    @GetMapping("/asm/dev_type")
    public String devTypePage(Model model){
        List<AssetType> list=jpaAssetType.findAll();
        model.addAttribute("list",list);
        return "/asm/devType";
    }

    @RequiresPermissions("asm:type:add")
    @GetMapping("/asm/add_type")
    public String addTypePage(Model model){
        List<Resources> list=jpaResources.findAll();
        model.addAttribute("list",list);
        return "/asm/add_type";
    }


    @RequiresPermissions("asm:log:view")
    @GetMapping("/asm/log")
    public String log(Model model){
        Pageable pageable=PageRequest.of(0,pageSize);
        Page<OperatRecord> records=jpaOperatRecord.findAll(pageable);
        model.addAttribute("page",records);
        model.addAttribute("action",AsmAction.getList());
        model.addAttribute("list",records.getContent());
        return "/asm/log";
    }

    @RequiresPermissions("asm:log:view")
    @GetMapping("/asm/log/queryPage")
    public String queryPage(String tp,String timeRange,String pre,String next,String jump,String pageNow,Model model) throws ParseException {

        Pageable pageable;
        Page<OperatRecord> page = null;
        int pageInt;
        try {
            pageInt =Integer.parseInt(pageNow);
        }catch (NumberFormatException e){
            return "redirect:/asm/log";
        }
        /**
         * 先判断是否是翻页
         * */
        if(!pre.equals("")||!next.equals("")||!jump.equals("")){
            pageInt=pageInt-1;
            pageable=PageRequest.of(pageInt,pageSize);
            if(!pre.equals("")){
                pageable=pageable.previousOrFirst();
            }else if(!next.equals("")){
                pageable=pageable.next();
            }
        }else {
            pageable=PageRequest.of(0,pageSize);
        }


        if(!tp.equals("")&&!timeRange.equals("")){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date timeStart=simpleDateFormat.parse(timeRange.split(" - ")[0]);
            java.util.Date timeEnd=simpleDateFormat.parse(timeRange.split(" - ")[1]);
            if(tp.equals("全部操作")){
                page=jpaOperatRecord.findOperatRecordsByActionTimeBetween(timeStart,timeEnd,pageable);
            }else {
                page=jpaOperatRecord.findOperatRecordsByActionAndActionTimeBetween(tp,timeStart,timeEnd,pageable);
            }
        }else if(!tp.equals("")&&timeRange.equals("")){

            if(tp.equals("全部操作")){
                page=jpaOperatRecord.findAll(pageable);
            }else {
                page=jpaOperatRecord.findOperatRecordsByAction(tp,pageable);
            }
        }

        List<String> list=AsmAction.getList();
        if (!tp.equals("全部操作")) {
            String selected = null;
            for (String str : list) {
                if (str.equals(tp)) {
                    list.remove(str);
                    selected = str;
                    break;
                }

            }
            list.add(0, selected);
        }
        model.addAttribute("action",list);
        model.addAttribute("list",page.getContent());
        model.addAttribute("page",page);
        model.addAttribute("timeRange",timeRange);
        return "/asm/log";
    }

    @RequiresPermissions("asm:type:edit")
    @GetMapping("/asm/edit_type")
    public String editTypePage(int id,Model model){
        List<Resources> list=jpaResources.findAll();
        AssetType type= jpaAssetType.findById(id).get();
        Resources ress=null;
        for (Resources res:list){
            if(res.getPermission().equals(type.getPermiCode())){
                ress=res;
                list.remove(res);
                break;
            }
        }
        list.add(0,ress);   //把已选内容前置
        model.addAttribute("tp",type);
        model.addAttribute("list",list);
        return "/asm/edit_type";
    }



    @RequiresPermissions("asm:type:edit")
    @GetMapping("/asm/edit_dev")
    public String editDev(int id,Model model){
        Assert anAssert= jpaAssert.findById(id).get();


        String name=anAssert.getAname();
        DevType devType=jpaDevType.findDevTypeByDevName(name);
        String temp=devType.getAssetNumTemplate();
        model.addAttribute("dev",anAssert);
        model.addAttribute("temp",temp);
        return "/asm/edit_dev";
    }

    @GetMapping("/asm/asmDoc")
    public String doc(){
        return "/asm/asmDoc";
    }

    @RequiresPermissions("asm:edit:btn")
    @GetMapping("/asm/save_dev")
    public String saveDev(int id,String types,String model,String price,String remarks,String sn,String num){
        Assert anAssert=jpaAssert.findById(id).get();
        anAssert.setAssetTypeByAssertType(jpaAssetType.findAssetTypeByName(types));
        anAssert.setModel(model);
        anAssert.setPrice(price);
        anAssert.setRemarks(remarks);
        anAssert.setSnnum(sn);
        anAssert.setAssestnum(num);
        jpaAssert.save(anAssert);
        asmRecordService.write(AsmAction.dev_edit,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute("user"),null,anAssert);
        return "redirect:/asm/list";
    }





}
