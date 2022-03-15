package com.rbac.demo.controller.asm;
import com.rbac.demo.entity.*;
import com.rbac.demo.jpa.*;
import com.rbac.demo.service.AsmRecordService;
import com.rbac.demo.service.AsmService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotSerializeTransactionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AsmController {
    private static final int pageSize=10;
    @Autowired
    private JpaAssetType jpaAssetType;
    @Autowired
    private JpaEmployee jpaEmployee;
    @Autowired
    private JpaAssert jpaAssert;
    @Autowired
    private JpaResources jpaResources;
    @Autowired
    private JpaDevType jpaDevType;
    @Autowired
    private JpaGroup jpaGroup;
    @Autowired
    private AsmRecordService asmRecordService;
    @Autowired
    private JpaOperatRecord jpaOperatRecord;
    @Autowired
    private AsmService asmService;
    @Autowired
    private JpaSupplier jpaSupplier;
    @Autowired
    private JpaAssetAction jpaAssetAction;
    @RequiresPermissions("asm:bro:view")
    @GetMapping("/asm/bro")
    public String broPage(Model model){
        //operator
        List<AssetType> assetTypes=asmService.getPermitAsmAssetTypes();
        String loginUser= (String) SecurityUtils.getSubject().getPrincipal();  //操作者用户名
        Pageable pageable=PageRequest.of(0,pageSize);   //初始化第一页
        List<String> assertNames=null;
        if(assetTypes.size()>0){
           assertNames=jpaAssert.getDistinctAssertNames(assetTypes.get(0));
        }
        if(assertNames==null){
            assertNames=new ArrayList<>();
        }
        if(assertNames.size()>0){
            Page<Assert> asserts =jpaAssert.findAssertsByDevice(assertNames.get(0),assetTypes.get(0),pageable);  //初始化使用第一个类型，的第一个设备类型
            model.addAttribute("asserts",asserts);
            model.addAttribute("assertList",asserts.getContent());
        }
        model.addAttribute("operator",loginUser);
        model.addAttribute("assertTypes",assetTypes);
        model.addAttribute("assertNames",assertNames);
        return "asm/bro";
    }
    @RequiresPermissions("asm:ret:view")
    @GetMapping("/asm/ret")
    public String retPage(Model model){
        String loginUser= (String) SecurityUtils.getSubject().getPrincipal();  //操作者用户名
        model.addAttribute("operator",loginUser);
        return "asm/ret";
    }
    @GetMapping("/rtc/rtc")
    public String rtcPage(){
        return "rtc/file_convert";
    }


    @GetMapping("/asm/leading_out")
    public String leading_out_page(Model model){
        List<Suppplier> supppliers=jpaSupplier.findAll();
        List<AssetType> assetTypes=jpaAssetType.findAll();
        model.addAttribute("supppliers",supppliers);
        model.addAttribute("assetTypes",assetTypes);
        return "asm/leading_out";
    }

    @RequiresPermissions("asm:inp:view")
    @GetMapping("/asm/inp")
    public String inpPage(Model model){


        List<AssetType> assetTypes=asmService.getPermitAsmAssetTypes();
        List<Suppplier> supppliers=jpaSupplier.findAll();
        String firstType;
        if(assetTypes.size()==0){
            firstType="";
        }else {
            firstType=assetTypes.get(0).getTypeName();
        }
        List<String> names=jpaDevType.findDevTypesNameByAssertType(firstType);
        if(names.size()!=0){

            DevType devType=jpaDevType.findDevTypeByDevNameAndAssetTypeByAssertTypeId(names.get(0),assetTypes.get(0));
            String maxNum=asmService.getMaxAssetNum(devType);
            String code=devType.getAssetNumTemplate();
            model.addAttribute("code",code);
            model.addAttribute("maxNum",maxNum);
        }else {
            model.addAttribute("code","");
            model.addAttribute("maxNum","");
        }

        model.addAttribute("types",assetTypes);
        model.addAttribute("names",names);
        model.addAttribute("suppliers",supppliers);
        return "asm/inp";
    }

    @RequiresPermissions("asm:list:view")
    @GetMapping("/asm/list")
    public String listPage(Model model,String type,String isDam,String cuindex) throws UnsupportedEncodingException {
        List<AssetType> types= jpaAssetType.findAssertType();
        List<String> dam=new ArrayList<>();
        dam.add("完好");
        dam.add("损坏");
        if(type!=null&&isDam!=null){

           type=URLDecoder.decode(type,"UTF-8");
           isDam=URLDecoder.decode(isDam,"UTF-8");
           AssetType type1=jpaAssetType.findAssetTypeByName(type);
           for (AssetType assetType:types){
               if(assetType.getTypeName().equals(type)){
                   types.remove(assetType);
                   break;
               }
           }
           types.add(0,type1);
           for (String s:dam){
               if(s.equals(isDam)){
                   dam.remove(s);
                   break;
               }
           }
           dam.add(0,isDam);
        }
        model.addAttribute("types",types);
        model.addAttribute("dam",dam);
        model.addAttribute("cuindex",cuindex);
        return "asm/list";
    }

    @GetMapping("/asm/select_4db_zy")
    public String selectPage(Model model,String type,String isDam,String cu_row,String zy_or_db) throws UnsupportedEncodingException {
        String rows_str="";
        List<String> rows=new ArrayList<>();
        for(String row:cu_row.split(",")){
            if(!row.equals("")){
                rows.add(row);
            }
        }

        List<AssetType> types= jpaAssetType.findAssertType();
        List<String> dam=new ArrayList<>();
        dam.add("完好");
        dam.add("损坏");
        model.addAttribute("types",types);
        model.addAttribute("dam",dam);
        if(rows.size()>0){
            for(String str:rows){
                rows_str+=str+",";
            }
        }

        model.addAttribute("asset_selected",rows_str);
        if(zy_or_db.equals("zhuanyi")){
            return "asm/select_asset_zhuanyi";
        }else {
            return "asm/select_asset_diaobo";
        }

    }

    @RequiresPermissions("asm:type:view")
    @GetMapping("/asm/type")
    public String typePage(){

        return "asm/type";
    }

    @RequiresPermissions("asm:devType:view")
    @GetMapping("/asm/dev_type")
    public String devTypePage(Model model){
        List<AssetType> list=jpaAssetType.findAll();
        model.addAttribute("list",list);
        return "asm/devType";
    }

    @RequiresPermissions("asm:supplier:view")
    @GetMapping("/asm/supplier")
    public String supplier(){
        return "asm/supplier/supplier";
    }

    @RequiresPermissions("asm:supplier:add")
    @GetMapping("/asm/supplier_add")
    public String supplier_add(){
        return "asm/supplier/supplier_add";
    }

    @RequiresPermissions("asm:supplier:delete")
    @GetMapping("/supplier/del")
    public String supplier_del(int id,Model model){
        try {
            jpaSupplier.deleteById(id);
        }catch (Exception e){
            model.addAttribute("err",e);
            return "/supplier/error";
        }
        return "redirect:/asm/supplier";
    }
    @RequiresPermissions("asm:supplier:edit")
    @GetMapping("/supplier/edit")
    public String firm_edit(int id, Model model){

        Suppplier suppplier=jpaSupplier.findById(id).get();
        model.addAttribute("supplier",suppplier);
        return "asm/supplier/supplier_edit";
    }
    @RequiresPermissions("asm:type:add")
    @GetMapping("/asm/add_type")
    public String addTypePage(Model model){
    //    List<Resources> list=jpaResources.findAll();
        //model.addAttribute("list",list);
        return "asm/add_type";
    }


    @RequiresPermissions("asm:log:view")
    @GetMapping("/asm/log")
    public String log(Model model){
        Pageable pageable=PageRequest.of(0,pageSize);
        Page<OperatRecord> records=jpaOperatRecord.findAll(pageable);
        model.addAttribute("page",records);
        model.addAttribute("action",AsmAction.getList());
        model.addAttribute("list",records.getContent());
        return "asm/log";
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
        return "asm/log";
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
        return "asm/edit_type";
    }



    @RequiresPermissions("asm:edit:btn")
    @GetMapping("/asm/edit_dev")
    public String editDev(int id,String type,String isDam,String cuindex,Model model){
        Assert anAssert= jpaAssert.findById(id).get();
        String name=anAssert.getAname();
        DevType devType=jpaDevType.findDevTypeByDevNameAndAssetTypeByAssertTypeId(name,anAssert.getAssetTypeByAssertType());
        String temp=devType.getAssetNumTemplate();
        List<Suppplier> supppliers=jpaSupplier.findAll();
        Suppplier cu_sup=anAssert.getSuppplierBySupplier();
        if(cu_sup!=null){
            for(Suppplier suppplier:supppliers){
                if(suppplier.equals(cu_sup)){
                    supppliers.remove(suppplier);
                    break;
                }
            }
            supppliers.add(0,cu_sup);

        }


        model.addAttribute("dev",anAssert);
        model.addAttribute("type",type);
        model.addAttribute("isDam",isDam);
        model.addAttribute("temp",temp);
        model.addAttribute("cuindex",cuindex);
        model.addAttribute("suppliers",supppliers);
        return "asm/edit_dev";
    }


    @GetMapping("/asm/record_view")
    public String record_view(int id,String recAct,Model model){
        Assert anAssert= jpaAssert.findById(id).get();
        String name=anAssert.getAname();
        DevType devType=jpaDevType.findDevTypeByDevNameAndAssetTypeByAssertTypeId(name,anAssert.getAssetTypeByAssertType());
        String temp=devType.getAssetNumTemplate();
        List<AssetAction> assetActions=jpaAssetAction.findAll();
        List<AssetRecord> assetRecords= (List<AssetRecord>) anAssert.getAssetRecordsById();
        List<AssetRecord> assetRecordList=new ArrayList<>();
        if(recAct!=null && recAct.equals("所有履历")){
            for (AssetRecord record:assetRecords){
                if(record.getAssetActionByAssetAction().getAssetAction().equals(recAct)){
                    assetRecordList.add(record);
                }
            }
        }else {
            assetRecordList=assetRecords;
        }

        model.addAttribute("dev",anAssert);
        model.addAttribute("temp",temp);
        model.addAttribute("assetRecords",assetRecordList);
        model.addAttribute("astAct",assetActions);
        return "asm/asset_record";
    }

    @RequiresPermissions("asm:exchange:view")
    @GetMapping("/asm/exchange")
    public String exchange(Model model){
        Employee employee= (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
        List<AssetType> types=jpaAssetType.findAll();
        model.addAttribute("oper",employee);
        model.addAttribute("assertTypes",types);
        return "asm/exchange/exchange_req";
    }

    @RequiresPermissions("asm:operate:view")
    @GetMapping("/asm/operate")
    public String operate_page(Model model){

        return "asm/asm_operate";
    }

    @RequiresPermissions("asm:zhuanyi.view")
    @GetMapping("/asm/zhuanyi")
    public String zhuanyi(Model model,String selected,String asset_selected){
        List<Assert> asserts=new ArrayList<>();
        Set<String> ids =new LinkedHashSet<>();

        if(selected!=null && !selected.equals("")){
            /*for(String an:selected.split(",")){
                asserts.add(jpaAssert.findById(Integer.parseInt(an)).get());
            }*/
            for(String an:selected.split(",")){
                ids.add(an);
            }
            if(asset_selected!=null && !asset_selected.equals("")){
                for (String an:asset_selected.split(",")){
                    if(!an.equals("")){
                        ids.add(an);
                    }
                }
            }
        }

        for(String as:ids){
           asserts.add(jpaAssert.findById(Integer.parseInt(as)).get());
        }
        List<Assert> asserts_reverse=new ArrayList<>();
        for(int i=asserts.size()-1;i>=0;i--){
          asserts_reverse.add(asserts.get(i));
        }

        model.addAttribute("selected",asserts_reverse);
        return "asm/asset_zhuanyi";
    }

    @GetMapping("/asm/do_zhuanyi")
    public String do_zhuanyi(Model model,String assets,String group_with_id){
      //  List<Assert> asserts=new ArrayList<>();
        for(String aid:assets.split(",")){
            String gid=group_with_id.split("-")[0];
            SysGroup sysGroup=jpaGroup.findById(Integer.parseInt(gid)).get();
            Assert anAssert=jpaAssert.findById(Integer.parseInt(aid)).get();
            anAssert.setSysGroupName(sysGroup.getGname());
            anAssert.setSysGroupBySysGroup(sysGroup);
            Assert ast=jpaAssert.save(anAssert);
            asmRecordService.createAndSaveAssetRecord(AssetAction.zhuanyi,ast,null,sysGroup);
      //      asserts.add(anAssert);
        }
      //  model.addAttribute("selected",asserts);
        return "asm/asset_zhuanyi";
    }

    @RequiresPermissions("asm:diaobo:view")
    @GetMapping("/asm/diaobo")
    public String diaobo(Model model,String selected,String asset_selected){
        List<Assert> asserts=new ArrayList<>();
        Set<String> ids =new LinkedHashSet<>();

        if(selected!=null && !selected.equals("")){
            for(String an:selected.split(",")){
                if(!an.equals("")){
                    ids.add(an);
                }
            }
            if(asset_selected!=null && !asset_selected.equals("")){
                for (String an:asset_selected.split(",")){
                    if(!an.equals("")){
                        ids.add(an);
                    }
                }
            }
        }

        for(String as:ids){
            asserts.add(jpaAssert.findById(Integer.parseInt(as)).get());
        }
        List<Assert> asserts_reverse=new ArrayList<>();
        for(int i=asserts.size()-1;i>=0;i--){
            asserts_reverse.add(asserts.get(i));
        }


        model.addAttribute("selected",asserts_reverse);
        return "asm/asset_diaobo";
    }

    @GetMapping("/asm/do_diaobo")
    public String do_diaobo(Model model,String assets,String emp_with_id){
      //  List<Assert> asserts=new ArrayList<>();
        for(String aid:assets.split(",")){
            String loginname=emp_with_id.split("-")[1];
            Employee employee=jpaEmployee.findEmployeeByLoginName(loginname);
            Assert anAssert=jpaAssert.findById(Integer.parseInt(aid)).get();
            anAssert.setEmployeeByBorrower(employee);
            Assert ast=jpaAssert.save(anAssert);
            asmRecordService.createAndSaveAssetRecord(AssetAction.diaobo,ast,employee,null);
       //     asserts.add(anAssert);
        }
      //  model.addAttribute("selected",asserts);
        return "asm/asset_diaobo";
    }

    @RequiresPermissions("asm:exchange:view")
    @GetMapping("/asm/exchange_resp")
    public String exchangeResp(Model model){
        Employee employee= (Employee) SecurityUtils.getSubject().getSession().getAttribute("user");
        List<AssetType> types=jpaAssetType.findAll();
        model.addAttribute("oper",employee);
        model.addAttribute("assertTypes",types);
        return "asm/exchange/exchange_resp";
    }


    @RequiresPermissions("asm:edit:btn")
    @GetMapping("/asm/save_dev")
    public String saveDev(int id,String types,String model,String price,String remarks,String sn,String num,String list_type,
                          String list_isDam,String cuindex,String new_bro,String supplier,String sysGroup) throws UnsupportedEncodingException {
        Assert anAssert=jpaAssert.findById(id).get();
        anAssert.setAssetTypeByAssertType(jpaAssetType.findAssetTypeByName(types));
        anAssert.setModel(model);
        anAssert.setPrice(price);
        anAssert.setRemarks(remarks);
        anAssert.setSnnum(sn);
        anAssert.setAssestnum(num);
        for(Suppplier suppplier:jpaSupplier.findAll()){
            if(suppplier.getSupplierName().equals(supplier)){
                anAssert.setSuppplierBySupplier(suppplier);
                break;
            }
        }
        if(sysGroup==null || sysGroup.equals("")){
            anAssert.setSysGroupBySysGroup(null);
            anAssert.setSysGroupName("");
        }else {
            if(sysGroup.contains("-")){
                String groupId=sysGroup.split("-")[0];
                SysGroup group=jpaGroup.findById(Integer.parseInt(groupId)).get();
                anAssert.setSysGroupBySysGroup(group);
                anAssert.setSysGroupName(group.getGname());
            }
        }
        if(new_bro==null || new_bro.equals("")) {
            anAssert.setEmployeeByBorrower(null);
        }else {
            if(new_bro.contains("-")){
                String py=new_bro.split("-")[1];
                Employee employee=jpaEmployee.findEmployeeByLoginName(py);
                anAssert.setEmployeeByBorrower(employee);
            }
        }
        jpaAssert.save(anAssert);
        asmRecordService.write(AsmAction.dev_edit,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute("user"),null,anAssert,"");
        String type=URLEncoder.encode(list_type,"UTF-8");
        String isDam=URLEncoder.encode(list_isDam,"UTF-8");
        return "redirect:/asm/list?type="+type+"&isDam="+isDam+"&cuindex="+cuindex;
//        String type,String isDam,String search,String pre,String next,int pageIndex,String jumpFlag
    }

    /**
     *
     * 下载模板
     *
     * */
    @GetMapping("asm/outputTemplate")
    public void exportAssetModel(HttpServletResponse response) throws FileNotFoundException {
        OutputStream stream = null;
        try {
            stream =new BufferedOutputStream(response.getOutputStream()) ;

        } catch (IOException e) {
            e.printStackTrace();
        }
        String path= ClassUtils.getDefaultClassLoader().getResource("static/excel").getPath();   //上传资源到项目路径的路径获得

        File file=new File(path+"/"+"moban1.xlsx");
//        InputStream inputStream =new FileInputStream(file);
        InputStream inputStream =this.getClass().getClassLoader().getResourceAsStream("static/excel/moban1.xlsx");
        response.setHeader("Content-disposition", "attachment; filename=" + "template.xlsx");
        response.setContentType("application/octet-stream;charset=UTF-8");//设置类型
        response.setHeader("Pragma", "No-cache");//设置头
        response.setHeader("Cache-Control", "no-cache");//设置头
        response.setDateHeader("Expires", 0);//设置日期头
        byte[] bytes=new byte[1024];
        try {
            //除放在static下和templates下的资源
            //idea必须使用项目路径，即以src开头的路径

            //jspringboot文件下载，的输入流只能按以上方式获得
            int i=0;
            while(((i=inputStream.read(bytes))!=-1)){
                stream.write(bytes,0,i);
            }
            inputStream.close();
            stream.flush();
            stream.close();
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }

    @GetMapping("asm/outputUserTemplate")
    public void exportUserModel(HttpServletResponse response) throws FileNotFoundException {
        OutputStream stream = null;
        try {
            stream =new BufferedOutputStream(response.getOutputStream()) ;

        } catch (IOException e) {
            e.printStackTrace();
        }
        String path= ClassUtils.getDefaultClassLoader().getResource("static/excel").getPath();   //上传资源到项目路径的路径获得

        File file=new File(path+"/"+"user_mb.xlsx");
//        InputStream inputStream =new FileInputStream(file);
        InputStream inputStream =this.getClass().getClassLoader().getResourceAsStream("static/excel/user_mb.xlsx");
        response.setHeader("Content-disposition", "attachment; filename=" + "template.xlsx");
        response.setContentType("application/octet-stream;charset=UTF-8");//设置类型
        response.setHeader("Pragma", "No-cache");//设置头
        response.setHeader("Cache-Control", "no-cache");//设置头
        response.setDateHeader("Expires", 0);//设置日期头
        byte[] bytes=new byte[1024];
        try {
            //除放在static下和templates下的资源
            //idea必须使用项目路径，即以src开头的路径

            //jspringboot文件下载，的输入流只能按以上方式获得
            int i=0;
            while(((i=inputStream.read(bytes))!=-1)){
                stream.write(bytes,0,i);
            }
            inputStream.close();
            stream.flush();
            stream.close();
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }
}
