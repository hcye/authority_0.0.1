package com.rbac.hcye_admin.controller.asm;
import com.rbac.hcye_admin.entity.*;
import com.rbac.hcye_admin.jpa.*;
import com.rbac.hcye_admin.service.AsmRecordService;
import com.rbac.hcye_admin.service.AsmService;
import com.rbac.hcye_admin.service.PermissionService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    private JpaStoreLocate jpaStoreLocate;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private JpaAssetCheck jpaAssetCheck;


    @Autowired
    private JpaCheckHistory jpaCheckHistory;

    @Autowired
    private JpaAssetCkRecord jpaAssetCkRecord;
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
            if(maxNum.equals("")){
                maxNum="1";
            }
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

    @RequiresPermissions("asm:kucun:view")
    @GetMapping("/asm/stock")
    public String stockPage(Model model,String cuindex) {
        List<AssetType> types= jpaAssetType.findAssertType();
        List<DevType> devTypes=jpaDevType.findDevTypesByAssertType(types.get(0).getTypeName());
        model.addAttribute("dev_types",devTypes);
        model.addAttribute("types",types);
        model.addAttribute("cuindex",cuindex);
        return "asm/stock";
    }

    @GetMapping("/asm/select_4db_zy")
    public String selectPage(Model model,String cu_row,String zy_or_db) {
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

    @GetMapping("/asm/asset_check")
    public String assetCheck(Model model,String selected_id,String selected_remark,String start,String stop_check) {
        String usname= (String) SecurityUtils.getSubject().getPrincipal();
        List<AssetType> types = jpaAssetType.findAssertType();
        List<String> dam = new ArrayList<>();
        dam.add("完好");
        dam.add("损坏");
        model.addAttribute("types", types);
        model.addAttribute("dam", dam);

        if (start != null && start.equals("on")) {
            Employee starter = (Employee) SecurityUtils.getSubject().getSession().getAttribute(usname);
            AssetCheck ac = new AssetCheck();
            ac.setCheckDate(new java.sql.Date(System.currentTimeMillis()));
            ac.setStatus("on");
            ac.setStater(starter.getId());
            ac.setStarter_name(starter.getLoginName());
            jpaAssetCheck.saveAndFlush(ac);
            return "asm/check/asset_check";
        }
        Employee cu_user = (Employee) SecurityUtils.getSubject().getSession().getAttribute(usname);
        List<AssetCheck> assetChecks = jpaAssetCheck.findAll();
        AssetCheck assetCheck_1 = null;
        if (assetChecks.size() > 0) {
            for (AssetCheck assetCheck : assetChecks) {
                // if login user is the starter user into the asset check page
                if (assetCheck.getStatus().equals("on") && (cu_user.getId()) == assetCheck.getStater()) {
                    assetCheck_1 = assetCheck;
                }
            }
        }
        if(assetCheck_1==null){
            return "asm/check/start_check";
        }
        if (stop_check != null && stop_check.equals("true")) {
            List<AssetCkRecord> assetCkRecords=jpaAssetCkRecord.findAssetCkRecordsByAssetCheckByAssetCheck(assetCheck_1);
            jpaAssetCkRecord.deleteAll(assetCkRecords);
            jpaAssetCheck.delete(assetCheck_1);
            return "asm/check/start_check";
        }
        if (start != null && start.equals("on")) {
            if (assetCheck_1 == null) {
                return "asm/check/start_check";
            }
        }

        if (selected_id != null && selected_remark != null) {
            String[] ids = selected_id.split(",");
            String[] selected_remarks = selected_remark.split(",");
            int remarks_len=selected_remarks.length;
            List<String> remarks=new ArrayList<>();
            if(remarks_len<ids.length){

                int cha=ids.length-remarks_len;
                if(remarks_len>0){
                    for(int i=0;i< remarks_len;i++){
                        remarks.add(selected_remarks[i]);
                    }
                    for(int i=0;i<cha;i++){
                        remarks.add("");
                    }
                }else if (remarks_len==0){
                    for (int i=0;i<ids.length;i++){
                        remarks.add("");
                    }
                }
            }else {
                for(int i=0;i<remarks_len;i++){
                    remarks.add(selected_remarks[i]);
                }
            }

            for (int i = 0;i<ids.length;i++){
                if(ids[i].equals("")){
                    return "asm/check/asset_check";
                }
                Assert anAssert=jpaAssert.findById(Integer.parseInt(ids[i])).get();
                AssetCkRecord assetCkRecord=new AssetCkRecord();
                assetCkRecord.setAssetCheckByAssetCheck(assetCheck_1);
                assetCkRecord.setAssetName(anAssert.getAname());
                assetCkRecord.setAssetId(anAssert.getId());
                assetCkRecord.setAssetCode(anAssert.getAssestnum());
                if(!remarks.get(i).equals("")){
                    assetCkRecord.setCheckRemark(selected_remarks[i]);
                }
                jpaAssetCkRecord.save(assetCkRecord);
            }
        }

        return "asm/check/asset_check";
    }
    @GetMapping("/asm/check_history")
    public String check_history(Model model,String show_list,String history_id) {
        List<CheckHistory> checkHistories = jpaCheckHistory.findAll();
        if (show_list != null) {
            List<String> operator_names = new ArrayList<>();
            for (CheckHistory checkHistory : checkHistories) {
                Employee operator = jpaEmployee.findById(checkHistory.getCheckOperator()).get();
                operator_names.add(operator.getEname());
            }
            model.addAttribute("list", checkHistories);
            return "asm/check/check_history";
        } else if (history_id != null) {
            CheckHistory checkHistory = jpaCheckHistory.findById(Integer.parseInt(history_id)).get();
            String[] kongIds = checkHistory.getCheckKongIds().split(",");
            String[] acks = checkHistory.getCheckRecordsIds().split(",");
            List<Assert> asserts = new ArrayList<>();
            List<AssetCkRecord> assetCkRecords = new ArrayList<>();
            for (int i = 0; i < kongIds.length; i++) {
                if (!kongIds[i].equals("")) {
                    Assert anAssert = jpaAssert.findById(Integer.parseInt(kongIds[i])).get();
                    asserts.add(anAssert);
                }
            }
            for (int i = 0; i < acks.length; i++) {
                if (!acks[i].equals("")) {
                    AssetCkRecord assetCkRecord = jpaAssetCkRecord.findById(Integer.parseInt(acks[i])).get();
                    assetCkRecords.add(assetCkRecord);
                }
            }
            model.addAttribute("ast",asserts);
            model.addAttribute("ack",assetCkRecords);
            return "asm/check/history_detail";
        }
        return "";
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
//-------------------------------------
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
            return "/asm/supplier/error";
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

//    ------------------------------------------------

    @RequiresPermissions("asm:locate:view")
    @GetMapping("/asm/locate")
    public String locate(){
        return "asm/locate/locate";
    }

    @RequiresPermissions("asm:locate:add")
    @GetMapping("/asm/locate_add")
    public String locate_add(){
        return "asm/locate/locate_add";
    }

    @RequiresPermissions("asm:locate:del")
    @GetMapping("/locate/del")
    public String locate_del(int id,Model model){
        try {
            if(jpaAssetType.findAssetTypesByDefaultLocate(id).size()!=0 || jpaAssert.findAssertsByLocate(id).size()!=0){
                model.addAttribute("err","仓库被使用不能删除!!!");
                return "/asm/supplier/error";
            }
            jpaStoreLocate.deleteById(id);
        }catch (Exception e){
            model.addAttribute("err",e);
            return "/asm/supplier/error";
        }
        return "redirect:/asm/locate";
    }

    @RequiresPermissions("asm:locate:edit")
    @GetMapping("/locate/edit")
    public String locate_edit(int id, Model model){

        StoreLocate storeLocate=jpaStoreLocate.findById(id).get();
        model.addAttribute("store",storeLocate);
        return "asm/locate/locate_edit";
    }

    @RequiresPermissions("asm:type:add")
    @GetMapping("/asm/add_type")
    public String addTypePage(Model model){
    //    List<Resources> list=jpaResources.findAll();
        //model.addAttribute("list",list);
        List<StoreLocate> storeLocates=jpaStoreLocate.findAll();
        model.addAttribute("stores",storeLocates);
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
        List<StoreLocate> storeLocates=jpaStoreLocate.findAll();
        Resources ress=null;
        for (Resources res:list){
            if(res.getPermission().equals(type.getPermiCode())){
                ress=res;
                list.remove(res);
                break;
            }
        }

        if(type.getDefaultLocate()!=null){
            int repo_id=type.getDefaultLocate();
            StoreLocate locate=jpaStoreLocate.findById(repo_id).get();
            for(StoreLocate st:storeLocates){
                if(st.getId()==repo_id){
                    storeLocates.remove(st);
                    break;
                }
            }
            storeLocates.add(0,locate);
        }
        list.add(0,ress);   //把已选内容前置
        model.addAttribute("tp",type);
        model.addAttribute("list",list);
        model.addAttribute("store",storeLocates);
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
        List<StoreLocate> storeLocates=jpaStoreLocate.findAll();
        List<String> str_stores=new ArrayList<>();
        if(anAssert.getLocate()!=null){
            int locate_id=anAssert.getLocate();
            StoreLocate now=null;
            for(StoreLocate storeLocate:storeLocates){
                if(storeLocate.getId()==locate_id){
                    now=storeLocate;
                    continue;
                }
                str_stores.add(storeLocate.getLocate());
            }
            str_stores.add(0,now.getLocate());
        }else {
            for(StoreLocate storeLocate:storeLocates){
                str_stores.add(storeLocate.getLocate());
            }
            str_stores.add(0,"");
        }
        model.addAttribute("dev",anAssert);

        List<AssetType> assetTypes=jpaAssetType.findAll();
        assetTypes.remove(type);
        model.addAttribute("type",type);
        model.addAttribute("all_types",assetTypes);
        model.addAttribute("isDam",isDam);
        model.addAttribute("temp",temp);
        model.addAttribute("cuindex",cuindex);
        model.addAttribute("stores",str_stores);
        model.addAttribute("suppliers",supppliers);
        return "asm/edit_dev";
    }



    @GetMapping("/asm/add_dev_type")
    public String add_dev_view(Model model){
        List<AssetType> types=jpaAssetType.findAll();
        List<AssetType> permitTypes=new ArrayList<>();
        for (AssetType type:types){
            if(permissionService.isPermit(type.getPermiCode())){
                permitTypes.add(type);
            }
        }
        model.addAttribute("types",permitTypes);
        return "asm/add_devtype";
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
        if(recAct!=null && !recAct.equals("所有履历")){
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
        String usname= (String) SecurityUtils.getSubject().getPrincipal();
        Employee employee= (Employee) SecurityUtils.getSubject().getSession().getAttribute(usname);
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
        List<AssetType> assetTypes=jpaAssetType.findAll();
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
        model.addAttribute("types",assetTypes);
        return "asm/asset_zhuanyi";
    }

    @GetMapping("/asm/do_zhuanyi")
    public String do_zhuanyi(String assets,String type_with_id){
      //  List<Assert> asserts=new ArrayList<>();
        for(String aid:assets.split(",")){
            String tid=type_with_id.split("-")[0];
            AssetType assetType=jpaAssetType.findById(Integer.parseInt(tid)).get();
            Assert anAssert=jpaAssert.findById(Integer.parseInt(aid)).get();
            anAssert.setAssetTypeByAssertType(assetType);
            Assert ast=jpaAssert.save(anAssert);
            asmRecordService.createAndSaveAssetRecord(AssetAction.zhuanyi,ast,null,assetType);
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
    public String do_diaobo(String assets,String emp_with_id){
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
        String usname= (String) SecurityUtils.getSubject().getPrincipal();
        Employee employee= (Employee) SecurityUtils.getSubject().getSession().getAttribute(usname);
        List<AssetType> types=jpaAssetType.findAll();
        model.addAttribute("oper",employee);
        model.addAttribute("assertTypes",types);
        return "asm/exchange/exchange_resp";
    }

    @GetMapping("/asm/check_done")
    public String check_done(Model model){
        String usname= (String) SecurityUtils.getSubject().getPrincipal();
        Employee check_operater= (Employee) SecurityUtils.getSubject().getSession().getAttribute(usname);
        List<AssetCheck> assetChecks=jpaAssetCheck.findAll();
        AssetCheck cu_assetCheck=null;
        for(AssetCheck assetCheck:assetChecks){
            if(assetCheck.getStatus()!=null && assetCheck.getStatus().equals("on") && assetCheck.getStater()
                    ==check_operater.getId()){
                cu_assetCheck=assetCheck;
                break;
            }
        }
        List<AssetCkRecord> assetCkRecords=jpaAssetCkRecord.findAssetCkRecordsByAssetCheckByAssetCheck(cu_assetCheck);

        List<AssetCkRecord> recordsWithRemark=new ArrayList<>();
        for(AssetCkRecord assetCkRecord:assetCkRecords){
            if(assetCkRecord.getCheckRemark()!=null && !assetCkRecord.getCheckRemark().trim().equals("")){
                recordsWithRemark.add(assetCkRecord);
            }
        }

        List<AssetType> types = jpaAssetType.findAssertType();
        String assetIds="";
        String recIds="";

        List<AssetType> typeList=new ArrayList<>();
        for(AssetType type:types){
            if(SecurityUtils.getSubject().isPermitted(type.getPermiCode())){
                typeList.add(type);
            }
        }
        for(AssetCkRecord assetCkRecord:recordsWithRemark){
            recIds+=","+assetCkRecord.getId();
        }
        List<Assert> pankong=new ArrayList<>();
        for(AssetType type:typeList){
            List<Assert> asserts=jpaAssert.findAssertByAssetType_without_damflag(type.getTypeName());
            for(int i=asserts.size()-1;i>=0;i--){
                for(AssetCkRecord ack:assetCkRecords){
                        if(ack.getAssetCode().equals(asserts.get(i).getAssestnum())){
                            asserts.remove(asserts.get(i));
                            break;
                        }
                    }
                }
            pankong.addAll(asserts);
            }


        cu_assetCheck.setStatus("close");
        jpaAssetCheck.save(cu_assetCheck);
        for(Assert ast:pankong){
            assetIds+=","+ast.getId();
        }
        CheckHistory checkHistory=new CheckHistory();
        checkHistory.setCheckKongIds(assetIds);
        checkHistory.setCheckRecordsIds(recIds);
        checkHistory.setCheckTime(new java.sql.Date(System.currentTimeMillis()));
        checkHistory.setCheckOperator(check_operater.getId());
        checkHistory.setOperatorName(check_operater.getEname());
        jpaCheckHistory.save(checkHistory);
        model.addAttribute("rec",recordsWithRemark);
        model.addAttribute("types", types);

        return "asm/check/asset_check_done";
    }

    @RequiresPermissions("asm:edit:btn")
    @GetMapping("/asm/save_dev")
    public String saveDev(int id,String types,String model,String price,String remarks,String sn,String num,String list_type,
                          String list_isDam,String cuindex,String new_bro,String supplier,String zhuanyi,String locate,String inp_time,String img) throws UnsupportedEncodingException, ParseException {
        String usname= (String) SecurityUtils.getSubject().getPrincipal();
        Assert anAssert=jpaAssert.findById(id).get();
        anAssert.setAssetTypeByAssertType(jpaAssetType.findAssetTypeByName(types));
        anAssert.setModel(model);
        if(img.contains("http")){
            anAssert.setAssertPic(img);
        }
        anAssert.setPrice(price);
        anAssert.setRemarks(remarks);
        anAssert.setSnnum(sn);
        anAssert.setAssestnum(num);
        boolean move_flag=false;
        if(locate!=null && !locate.equals("") ){
            if(anAssert.getLocate()!=null){
                if(jpaStoreLocate.findStoreLocatesByLocate(locate.trim()).get(0).getId() != anAssert.getLocate()){
                    anAssert.setLocate(jpaStoreLocate.findStoreLocatesByLocate(locate.trim()).get(0).getId());
                    move_flag=true;
                }
            }else {
                anAssert.setLocate(jpaStoreLocate.findStoreLocatesByLocate(locate.trim()).get(0).getId());
            }
        }
        if(inp_time!=null && !inp_time.equals("")){
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            Date inp_date=format.parse(inp_time);
            anAssert.setPutintime(new java.sql.Date(inp_date.getTime()));
        }

        for(Suppplier suppplier:jpaSupplier.findAll()){
            if(suppplier.getSupplierName().equals(supplier)){
                anAssert.setSuppplierBySupplier(suppplier);
                break;
            }
        }
        if(zhuanyi!=null && !zhuanyi.equals("")){
            if(zhuanyi.contains("-")){
                String groupId=zhuanyi.split("-")[0];
                AssetType assetType=jpaAssetType.findById(Integer.parseInt(groupId)).get();
                AssetType cast=anAssert.getAssetTypeByAssertType();
                if(assetType!=cast){
                    asmRecordService.createAndSaveAssetRecord(AssetAction.zhuanyi,anAssert,null,assetType);
                }
                anAssert.setAssetTypeByAssertType(assetType);
            }
        }
        if(new_bro==null || new_bro.equals("")) {

            Employee borrower=anAssert.getEmployeeByBorrower();
            if(borrower!=null){
                asmRecordService.createAndSaveAssetRecord(AssetAction.retrun_asset,anAssert,borrower,null);
            }
            anAssert.setEmployeeByBorrower(null);
        }else {
            if(new_bro.contains("-")){
                String py=new_bro.split("-")[1];
                Employee employee=jpaEmployee.findEmployeeByLoginName(py);
                Employee cu_borrower=anAssert.getEmployeeByBorrower();
                if(cu_borrower!=null && cu_borrower!=employee){
                    asmRecordService.createAndSaveAssetRecord(AssetAction.retrun_asset,anAssert,cu_borrower,null);
                    asmRecordService.createAndSaveAssetRecord(AssetAction.borrow,anAssert,employee,null);
                } else if (cu_borrower==null) {
                    asmRecordService.createAndSaveAssetRecord(AssetAction.borrow,anAssert,employee,null);
                }
                anAssert.setEmployeeByBorrower(employee);
            }
        }
//
        jpaAssert.saveAndFlush(anAssert);
        if(move_flag){
            asmRecordService.createAndSaveAssetRecord(AssetAction.move,anAssert,null,null);
        }
        asmRecordService.write(AsmAction.dev_edit,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute(usname),null,anAssert,"");
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
