package com.rbac.hcye_admin.controller.restController;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.rbac.hcye_admin.easyExcel.*;
import com.rbac.hcye_admin.entity.*;
import com.rbac.hcye_admin.jpa.*;
import com.rbac.hcye_admin.service.*;
import com.rbac.hcye_admin.tool.ConvertStrForSearch;
import org.apache.commons.collections.IteratorUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;


import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class AsmRestController {
    private static final int pageSize=15;
    @Autowired
    private JpaEmployee jpaEmployee;
    @Autowired
    private JpaAssert jpaAssert;

    @Autowired
    private JpaAssetCheck jpaAssetCheck;

    @Autowired
    private JpaAssetCkRecord jpaAssetCkRecord;
    @Autowired
    private JpaAssetType jpaAssetType;
    @Autowired
    private JpaGroup jpaGroup;
    @Autowired
    private WriteLog writeLog;
    @Autowired
    private JpaDevType jpaDevType;
    @Autowired
    private JpaRtcBug jpaRtcBug;
    @Autowired
    private JpaOperatRecord jpaOperatRecord;
    @Autowired
    private AsmService asmService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private AsmRecordService asmRecordService;
    @Autowired
    private JpaExchangeDevs jpaExchangeDevs;
    @Autowired
    private JpaMail jpaMail;
    @Autowired
    private JpaResources jpaResources;
    @Autowired
    private JpaSupplier jpaSupplier;
    @Autowired
    private JpaAssetRecord jpaAssetRecord;
    @Autowired
    private  JpaAssetAction jpaAssetAction;
    @Autowired
    private  PermissionService permissionService;
    @Autowired
    private  JpaRole jpaRole;
    @Autowired
    private  JpaRole2Resources jpaRole2Resources;
    @PostMapping("/asm/queryPage")
    public Map<String,List<Object>> queryPage(String type,String name,String search,String pre,String next,int pageIndex,String jumpFlag){
        /**
         *
         * 确定是否是翻页
         *
         * */

        Map<String,List<Object>> map=new HashMap<>();
        List<Employee> bros=new ArrayList<>();
        Page<Assert> page;
        Pageable pageable;
        List<Page<Assert>> pages=new ArrayList<>();
        if(!pre.equals("")||!next.equals("")||!jumpFlag.equals("")){
            pageIndex=pageIndex-1;
            pageable=PageRequest.of(pageIndex,pageSize);
            if(!pre.equals("")){
                pageable=pageable.previousOrFirst();
            }else if(!next.equals("")){
                pageable=pageable.next();
            }
        }else {
            pageable=PageRequest.of(0,pageSize);
        }
        if(search.equals("")){
            page=jpaAssert.findAssertsByAnameAndAssetType(name,type,pageable);
        }else {
            search=ConvertStrForSearch.getFormatedString(search);
            page=jpaAssert.findAssertsByAnameAndAssetType(search,type,pageable);
            if(page.isEmpty()){
                page=jpaAssert.findAssertsByAssestnumAndAssetType(search,type,pageable);
            }
        }
        List<Assert> asserts=page.getContent();
        for (Assert ast:asserts){
            Employee bro=ast.getEmployeeByBorrower();
            if(bro==null){
                bros.add(null);
            }else {
                bros.add(bro);
            }
        }



        pages.add(page);
        map.put("page", Collections.singletonList(pages));
        map.put("emp", Collections.singletonList(bros));
        return map;
    }
    @PostMapping("/asm/queryExchangePage")
    public Map<String,List<Object>> queryExchangePage(String type,String search,String pre,String next,int pageIndex,String jumpFlag){
        /**
         *
         * 确定是否是翻页
         *
         * */

        Map<String,List<Object>> map=new HashMap<>();
        List<DevType> devTypes=jpaDevType.findDevTypesByAssertType(type);
        List<Employee> bros=new ArrayList<>();
        List<Employee> me=new ArrayList<>();
        Page<Assert> page;
        Pageable pageable;
        List<Page<Assert>> pages=new ArrayList<>();
        if(!pre.equals("")||!next.equals("")||!jumpFlag.equals("")){
            pageIndex=pageIndex-1;
            pageable=PageRequest.of(pageIndex,pageSize);
            if(!pre.equals("")){
                pageable=pageable.previousOrFirst();
            }else if(!next.equals("")){
                pageable=pageable.next();
            }
        }else {
            pageable=PageRequest.of(0,pageSize);
        }
        if(search.equals("")){
            page=jpaAssert.findAssertsBytype(type,"0",pageable);
        }else {
            search=ConvertStrForSearch.getFormatedString(search);
            page=jpaAssert.findAssertsByAnameAndAssetType(search,type,pageable);
            if(page.isEmpty()){
                page=jpaAssert.findAssertsByAssestnumAndAssetType(search,type,pageable);
            }
        }


        List<Assert> asserts=page.getContent();
        for (Assert ast:asserts){
            Employee bro=ast.getEmployeeByBorrower();
            if(bro==null){
                bros.add(null);
            }else {
                bros.add(bro);
            }
        }

        String usname= (String) SecurityUtils.getSubject().getPrincipal();
        me.add((Employee) SecurityUtils.getSubject().getSession().getAttribute(usname));
        pages.add(page);
        map.put("page", Collections.singletonList(pages));
        map.put("emp", Collections.singletonList(bros));
        map.put("devs", Collections.singletonList(devTypes));
        map.put("me", Collections.singletonList(me));
        return map;
    }


    @PostMapping("/asm/operat")
    public Map<String, List<Assert>> borrow(String selectDevIds,String name,String actionFlag){
        Map<String,List<Assert>> map=new HashMap<>();
        String[] ids=selectDevIds.split(",");

        String[] res=name.split("-");
        Employee borrower=null;
        if(res.length==2){
            borrower=jpaEmployee.findEmployeeByLoginName(name.split("-")[1]);
        }else if(res.length>2){
            String login_name="";
            for (int i=1;i<res.length-1;i++){
                login_name=login_name+res[i]+"-";
            }
            login_name=login_name+res[res.length-1];
            borrower=jpaEmployee.findEmployeeByLoginName(login_name);
        }
        String usname= (String) SecurityUtils.getSubject().getPrincipal();
        if(actionFlag.equals("bo")){
            for (String str:ids){
                if(!str.trim().equals("")){
                    Assert ast=jpaAssert.findById(Integer.parseInt(str)).get();
                    ast.setEmployeeByBorrower(borrower);

                    asmRecordService.write(AsmAction.dev_borrow,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute(usname),borrower,ast,"");
                    Assert anAssert=jpaAssert.save(ast);
                    asmRecordService.createAndSaveAssetRecord(AssetAction.borrow,anAssert,borrower,null);
                }
            }
        }else if(actionFlag.equals("ret")){
            for (String str:ids){
                if(!str.trim().equals("")){
                    Assert ast=jpaAssert.findById(Integer.parseInt(str)).get();
                    ast.setEmployeeByBorrower(null);
                    asmRecordService.write(AsmAction.dev_return,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute(usname),borrower,ast,"");
                    Assert anAssert=jpaAssert.save(ast);
                    asmRecordService.createAndSaveAssetRecord(AssetAction.retrun_asset,anAssert,borrower,null);
                }
            }
        }
        List<Assert> asserts= (List<Assert>) borrower.getAssertsById();
        map.put("allDev",asserts);
        return map;
    }

    @PostMapping("/asm/devs")
    public Map<String, List<Assert>> getDevs(String name){
        Map<String,List<Assert>> map=new HashMap<>();
        if(name.equalsIgnoreCase("")||!name.contains("-")){
            return map;
        }
        List<AssetType> assetTypes=asmService.getPermitAsmAssetTypes();
        String[] res=name.split("-");
        Employee returner=null;
        if(res.length==2){
            returner=jpaEmployee.findEmployeeByLoginName(name.split("-")[1]);
        }else if(res.length>2){
            String login_name="";
            for (int i=1;i<res.length-1;i++){
                login_name=login_name+res[i]+"-";
            }
            login_name=login_name+res[res.length-1];
            returner=jpaEmployee.findEmployeeByLoginName(login_name);
        }
        List<Assert> assertsHasPermit=new ArrayList<>();
        if(returner==null){
            return map;
        }
        List<Assert> asserts= (List<Assert>) returner.getAssertsById();
        for(int i=0;i<asserts.size();i++){
            for (int j=0;j<assetTypes.size();j++){
                if(asserts.get(i).getAssetTypeByAssertType().equals(assetTypes.get(j))){
                    assertsHasPermit.add(asserts.get(i));
                }
            }
        }

        map.put("devs",assertsHasPermit);
        return map;
    }

    @PostMapping("/asm/addAssetType")
    public Map<String, String> getDevsNames(String devType,String template,String desc){
        Map<String ,String> map=new HashMap<>();
        devType=devType.trim();
        AssetType type = jpaAssetType.findAssetTypeByName(devType);
        if(type!=null){
            map.put("error","类型名称重复!");
            return map;
        }
        Timestamp ts=new Timestamp(new Date().getTime());
        String usname= (String) SecurityUtils.getSubject().getPrincipal();
        AssetType assertType=new AssetType();
        assertType.setAssetCode(template);
        assertType.setCreateTime(ts);
        assertType.setCreator(usname);
        assertType.setRemarks(desc);
        assertType.setTypeName(devType);
        /*
        * 生成权限字符，
        * 利用这个权限字符创建资源对象
        * 保存权限字符。
        * */
        Random r =new Random(100);
        String permissionCode="";
        while (true){
            int rand=r.nextInt(1000);
            permissionCode="asm:oper:"+rand;
            if(jpaResources.findResourcesByPermission(permissionCode)!=null){
                continue;
            }else{
                break;
            }
        }

        Resources resources=new Resources();
        resources.setResourcesByParentId(jpaResources.findById(79).get());
        resources.setDescription(devType);
        resources.setPermission(permissionCode);
        resources.setType("按钮");
        jpaResources.saveAndFlush(resources);

        assertType.setPermiCode(permissionCode);
        jpaAssetType.saveAndFlush(assertType);
        Session session=SecurityUtils.getSubject().getSession();
        Employee employee= (Employee) session.getAttribute(usname);
        permissionService.addPermissionToUser(employee,resources);
        asmRecordService.write(AsmAction.dev_add_type,new Timestamp(new java.util.Date().getTime()),
                (Employee) SecurityUtils.getSubject().getSession().getAttribute(usname),
                null,null,devType);
        map.put("ok","新增成功!");
        return map;
    }


    @RequiresPermissions("asm:devType:edit")
    @PostMapping("/asm/editDevType")
    public Map<String, String> valid(int id,String input,boolean exc){
        Map<String ,String> map=new HashMap<>();
        DevType devType=jpaDevType.findById(id).get();
        AssetType assetType=devType.getAssetTypeByAssertTypeId();
        String name=devType.getDevName();
        List<Assert> list = jpaAssert.findAssertsByAnameAndAssetType(name,assetType);
        if(exc==true&&devType.getExchangeable().equals("0")){
            devType.setExchangeable("1");
            jpaDevType.save(devType);
            map.put("ok","该设备已经可以流转！");
            return map;
        }else if(exc==false&&devType.getExchangeable().equals("1")){
            devType.setExchangeable("0");
            jpaDevType.save(devType);
            map.put("ok","设备被禁止流转！");
            return map;
        }
        if(!list.isEmpty()){
            map.put("error","设备类型关联有设备不能修改！");
            return map;
        }else {
            String tem=devType.getAssetTypeByAssertTypeId().getAssetCode();
            if(asmService.valid(input,tem)){
                devType.setAssetNumTemplate(input);
                jpaDevType.save(devType);
                map.put("ok","修改成功！");

            }else {
                map.put("error","输入不匹配资产类型约束！");
            }
            return map;
        }

    }
    @RequiresPermissions("asm:type:edit")
    @PostMapping("/asm/saveAssetType")
    public Map<String, String> saveType(int id,String devType,String template,String desc){
        Map<String ,String> map=new HashMap<>();
        devType=devType.trim();
//        if(authority.equals("")){
//            map.put("error","权限字符不能为空!");
//            return map;
//        }
        AssetType type = jpaAssetType.findAssetTypeByName(devType);
        AssetType assertTypeOld= jpaAssetType.findById(id).get();
        if(type!=null&&type.getId()!=id){
            map.put("error","类型名称重复,修改失败!");
            return map;
        }
        assertTypeOld.setAssetCode(template);
        assertTypeOld.setRemarks(desc);
        assertTypeOld.setTypeName(devType);
        jpaAssetType.save(assertTypeOld);
        map.put("ok","修改成功!");
        return map;
    }

    @PostMapping("/asm/getDevNames")
    public Map<String, List<String>> get(String TpName,String getTypesOnly){
        Map<String,List<String>> map=new HashMap<>();
        AssetType assetType=jpaAssetType.findAssetTypeByName(TpName);
        if(getTypesOnly!=null){
            List<String> names=jpaDevType.findDevTypesNameByAssertType(TpName);
            map.put("name",names);
            return map;
        }
        String code=assetType.getAssetCode();
        List<String> codes=new ArrayList<>();
        List<String> devCodes=new ArrayList<>();
        List<String> names=jpaDevType.findDevTypesNameByAssertType(TpName);
        if(names.size()!=0){
            DevType devType1=jpaDevType.findDevTypeByDevNameAndAssetTypeByAssertTypeId(names.get(0),assetType);

            devCodes.add(devType1.getAssetNumTemplate());
        }

        codes.add(code);
        map.put("name",names);
        map.put("code",codes);
        map.put("devCode",devCodes);
        return map;
    }
    @RequiresPermissions("asm:devType:add")
    @PostMapping("/asm/addDevType")
    public Map<String, String> addDevType(String devType,String dev_name,String desc,String temp,String exc){
        Map<String,String> map=new HashMap<>();
        AssetType assertType= jpaAssetType.findAssetTypeByName(devType);
        String assetCode=assertType.getAssetCode();
        String[] assetCodes=assetCode.split("-");
        int len=assetCodes.length;
        String tail=assetCodes[len-1];
        tail=tail.replace("0","9");
        temp=temp+"-"+tail;
        boolean b=asmService.valid(temp,assetCode);


        if(!b){
            map.put("error","设备类型编码不匹配资产类型编码");
            return map;
        }
        DevType dtp=jpaDevType.findDevTypeByDevNameAndAssertType(dev_name,assertType);
        if(dtp!=null){
            map.put("error","资产名称重复！请重新填写");
            return map;
        }
        List<DevType> types=jpaDevType.findDevTypesByAssetNumTemplate(temp);
        if(types.size()>0){
            map.put("error","编码模板重复！请重新填写");
            return map;
        }

        DevType devType1=new DevType();
        if(exc.equals("是")){
            devType1.setExchangeable("1");
        }else {
            devType1.setExchangeable("0");
        }
        Timestamp ts=new Timestamp(new Date().getTime());
        String usname= (String) SecurityUtils.getSubject().getPrincipal();
        devType1.setCreateTime(ts);
        devType1.setCreator(usname);
        devType1.setRemarks(desc);
        devType1.setDevName(dev_name);
        devType1.setAssetNumTemplate(temp);
        devType1.setAssetTypeByAssertTypeId(assertType);
        jpaDevType.save(devType1);
        map.put("ok","增加完成");
        return map;
    }
    @PostMapping("/asm/getTypes")
    public Page<AssetType> getTypes(String name,String pre,String next,int pageNow){
        return  typeService.getTypePage( name, pre, next, pageNow);
    }

    @PostMapping("/asm/scarp")
    public Map<String,String> scarp(int id,String reason){
        Assert anAssert=jpaAssert.findById(id).get();
        anAssert.setWorkless("1");
        AssetRecord assetRecord=new AssetRecord();
        assetRecord.setAssetActionByAssetAction(jpaAssetAction.findAssetActionByAssetAction("报废"));
        assetRecord.setActDetail(reason);
        assetRecord.setAssertByAsset(anAssert);
        jpaAssetRecord.save(assetRecord);
        Map<String,String> map=new HashMap<>();
        map.put("success","success");
        return map;
    }

    @PostMapping("/asm/maintain")
    public Map<String,String> maintain(int id,String reason){
        Assert anAssert=jpaAssert.findById(id).get();
        anAssert.setMaintain("1");
        AssetRecord assetRecord=new AssetRecord();
        assetRecord.setAssetActionByAssetAction(jpaAssetAction.findAssetActionByAssetAction("维修"));
        assetRecord.setActDetail(reason);
        assetRecord.setAssertByAsset(anAssert);
        assetRecord.setStartTime(new java.sql.Date(new Date().getTime()));
        jpaAssetRecord.save(assetRecord);
        Map<String,String> map=new HashMap<>();
        map.put("success","success");
        return map;
    }
    // exit maintain
    @PostMapping("/asm/emaintain")
    public Map<String,String> emaintain(int id,String reason){
        Assert anAssert=jpaAssert.findById(id).get();
        anAssert.setMaintain("0");
        AssetRecord assetRecord=new AssetRecord();
        assetRecord.setAssetActionByAssetAction(jpaAssetAction.findAssetActionByAssetAction("维修"));
        assetRecord.setActDetail(reason);
        assetRecord.setAssertByAsset(anAssert);
        assetRecord.setStartTime(new java.sql.Date(new Date().getTime()));
        jpaAssetRecord.save(assetRecord);
        Map<String,String> map=new HashMap<>();
        map.put("success","success");
        return map;
    }


    @PostMapping("/asm/getTypeNames")
    public Map<String,List<AssetType>> getTypes(){
        Map<String,List<AssetType>> map=new HashMap<>();
        List<AssetType> types=jpaAssetType.findAll();
        List<AssetType> permitTypes=new ArrayList<>();
        for (AssetType type:types){
            if(permissionService.isPermit(type.getPermiCode())){
                permitTypes.add(type);
            }
        }
        map.put("types",permitTypes);
        return  map;

    }



/*    @PostMapping("/supplier/add_commit")
    public Map<String,String> supplier_add(String name,String remark){
        Map<String,String> map=new HashMap<>();
        if(jpaSupplier.findSupppliersBySupplierName(name)!=null){
            map.put("ERROR","添加失败，同名供应商已存在！");
            return map;
        }
        Suppplier suppplier=new Suppplier();
        suppplier.setSupplierName(name);
        suppplier.setRemark(remark);
        jpaSupplier.save(suppplier);
        map.put("SUCCESS","添加成功");
        return map;
    }*/

    @RequestMapping("/supplier/getAll")
    public Map<String, List<Object>> getAllSupplier(String searchFlag,String name,String pre,String next,String pageIndex){
        Map<String,List<Object>> map=new HashMap<>();

        int pagenow=0;
        int pageSize=10;
        Page<Suppplier> supppliers = null;
        //初始页
        if(pageIndex.equals("")){
            Pageable pageable=PageRequest.of(pagenow,pageSize);
            supppliers =jpaSupplier.findAll(pageable);
        }else
            //搜索
            if(!searchFlag.equals("")){
                name=ConvertStrForSearch.getFormatedString(name);
                Pageable pageable=PageRequest.of(pagenow,pageSize);
                supppliers=jpaSupplier.findSupppliersBySupplierNameLike(name,pageable);
            }else
                //普通翻页
                if(name.equals("")){
                    Pageable pageable=PageRequest.of(Integer.parseInt(pageIndex)-1,pageSize);
                    if(pre.equals("1")){
                        pageable=pageable.previousOrFirst();
                    }
                    if (next.equals("1")){
                        pageable=pageable.next();
                    }
                    supppliers=jpaSupplier.findAll(pageable);

                }else
                    //带参数翻页
                    if(!name.equals("")){
                        name=ConvertStrForSearch.getFormatedString(name);
                        Pageable pageable=PageRequest.of(Integer.parseInt(pageIndex)-1,pageSize);
                        if(pre.equals("1")){
                            pageable=pageable.previousOrFirst();
                        }
                        if (next.equals("1")){
                            pageable=pageable.next();
                        }
                        supppliers=jpaSupplier.findSupppliersBySupplierNameLike(name,pageable);
                    }
        map.put("suppliers", Collections.singletonList(supppliers));
        return map;
    }

    @PostMapping("/supplier/add_commit")
    public Map<String,String> supplier_add(String name,String remark){
        Map<String,String> map=new HashMap<>();
        if(!jpaSupplier.findSupppliersBySupplierName(name).isEmpty()){
            map.put("ERROR","添加失败，同名品牌已存在！");
            return map;
        }
        Suppplier suppplier=new Suppplier();
        suppplier.setSupplierName(name);
        suppplier.setRemark(remark);
        jpaSupplier.save(suppplier);
        map.put("SUCCESS","添加成功");
        return map;
    }

    @PostMapping("/supplier/edit_commit")
    public Map<String,String> supplier_edit(int id,String name,String remark){
        Map<String,String> map=new HashMap<>();
        Suppplier suppplier=jpaSupplier.findById(id).get();
        List<Suppplier> supppliers=jpaSupplier.findSupppliersBySupplierName(name);

        if(!supppliers.isEmpty()&&supppliers.size()>0&&supppliers.get(0).getId()!=id){
            map.put("ERROR","修改失败，同名品牌已存在！");
            return map;
        }

        suppplier.setRemark(remark);
        suppplier.setSupplierName(name);
        jpaSupplier.save(suppplier);
        map.put("SUCCESS","编辑成功");
        return map;
    }

    @PostMapping("/asm/getTypeName")
    public Map<String,String> getName(String name){
        Map<String,String> map=new HashMap<>();
        map.put("code",jpaAssetType.findAssetTypeByName(name).getAssetCode());
        return  map;
    }

    @PostMapping("/asm/validAssetNum")
    public Map<String,String> valid(String num,int id){
        Map<String,String> map=new HashMap<>();
        Assert anAssert=jpaAssert.findById(id).get();

        String name=anAssert.getAname();
        DevType devType=jpaDevType.findDevTypeByDevNameAndAssetTypeByAssertTypeId(name,anAssert.getAssetTypeByAssertType());
        String template=devType.getAssetNumTemplate();
        boolean flag=asmService.validDevTypeNum(num,template);
        boolean repeatFlag=false;
        if(anAssert.getAssestnum().equalsIgnoreCase(num)){
            repeatFlag=true;
        }else {
            repeatFlag=asmService.validRepeat(num);
        }
        if(flag){
            map.put("ok","校验正确");
        }else {
            map.put("error","输入的资产编码不匹配设备类型约束！");
        }
        if(repeatFlag){
            map.put("ok","校验正确");
        }else {
            map.put("error","输入的资产编号重复！");
        }
        return  map;
    }


    @PostMapping("/asm/getDevTypes")
    public Page<DevType> getDevTypes(String name,String pre,String next,int pageNow,String type){
        return  typeService.getDevTypePage( name, pre, next, pageNow,type);
    }

    @RequiresPermissions("asm:type:delete")
    @PostMapping("/asm/deleteType")
    public Map<String,String> deleteType(int id){
        Map<String,String> map=new HashMap<>();
        AssetType type= jpaAssetType.findById(id).get();

        Collection<Assert> asserts=type.getAssertsById();
        if(asserts.isEmpty()){
            String permiCode=type.getPermiCode();
            Resources resources=jpaResources.findResourcesByPermission(permiCode);
            if(resources!=null){
                permissionService.cleanPermission(resources);
            }
            type.setDevTypesById(null);
            type.setAssertsById(null);
            jpaAssetType.delete(type);
            map.put("ok","删除成功");
        }else {
            map.put("error","删除失败");
        }
        return map;
    }

    @RequiresPermissions("asm:devType:delete")
    @PostMapping("/asm/deleteDevType")
    public Map<String,String> deleteDevType(int id){
        Map<String,String> map=new HashMap<>();
        DevType type= jpaDevType.findById(id).get();
        AssetType assetType=type.getAssetTypeByAssertTypeId();
        List<Assert> list=jpaAssert.findAssertsByAnameAndAssetType(type.getDevName(),assetType);
        if(list.isEmpty()){
            type.setAssetTypeByAssertTypeId(null);
            jpaDevType.delete(type);
            map.put("ok","删除成功");
        }else {
            map.put("error","删除失败");
        }
        return map;
    }



    @PostMapping("/asm/getDevNumTemplate")
    public Map<String, String> getNum(String devName,String assetType){
        Map<String,String> map=new HashMap<>();
        DevType devType=jpaDevType.findDevTypeByDevNameAndAssetTypeByAssertTypeId(devName,jpaAssetType.findAssetTypeByName(assetType));
        String max=asmService.getMaxAssetNum(devType);
        String devTypeAssetNumTemplate=devType.getAssetNumTemplate();
        map.put("code",devTypeAssetNumTemplate);
        map.put("max",max);
        return map;
    }
    @PostMapping("/supplier/get_supplier")
    public Map<String, List<Suppplier>> getSupplier(){
        List<Suppplier> supppliers=jpaSupplier.findAll();
        Map<String,List<Suppplier>> map=new HashMap<>();
        map.put("suppliers",supppliers);
        return map;
    }

    @PostMapping("/asm/validInputAssetNum")
    public Map<String,String> valid(String inputCode,String tep,String num,String model,String price){
        Map<String,String> map=new HashMap<>();
        String numRegex="[0-9]+";
        String priceRegex="[0-9]+[\\.]?[0-9]*";
        if(model.equals("")){
            map.put("error","型号不能为空！");
            return map;
        }
        if(!Pattern.matches(numRegex, num)){
            map.put("error","数量必填且只接受数字！");
            return map;
        }
        if(!price.trim().equals("")){
            if(!Pattern.matches(priceRegex, price)){
                map.put("error","价格只接受数字！");
                return map;
            }
        }

        boolean validRes=asmService.validDevTypeNum(inputCode, tep);
        if(validRes){
            map.put("ok","编号校验成功");
        }else {
            map.put("error","编号校验失败");
        }
        return map;
    }

    @RequestMapping("/asm/queryListPage")
    public Map<String,List<Object>> queryListPage(String type,String isDam,String search,String pre,String next,int pageIndex,String jumpFlag) throws UnsupportedEncodingException {
        /**
         *
         * 确定是否是翻页
         *
         * */

        Map<String,List<Object>> map=new HashMap<>();
        List<Employee> bros=new ArrayList<>();
        List<Page<Assert>> pages=new ArrayList<>();
        Pageable pageable;
        if(!pre.equals("")||!next.equals("")||!jumpFlag.equals("")){
            pageIndex=pageIndex-1;
            pageable=PageRequest.of(pageIndex,pageSize);
            if(!pre.equals("")){
                pageable=pageable.previousOrFirst();
            }else if(!next.equals("")){
                pageable=pageable.next();
            }
        }else {
            pageable=PageRequest.of(0,pageSize);
        }
        Page<Assert> page = asmService.queryPage(type,isDam,search,pageable);
        List<Assert> asserts=page.getContent();
        for (Assert ast:asserts){
            Employee bro=ast.getEmployeeByBorrower();
            if(bro==null){
                bros.add(null);
            }else {
                bros.add(bro);
            }
        }
        pages.add(page);
        map.put("emp", Collections.singletonList(bros));
        map.put("page", Collections.singletonList(pages));
        return map;
    }


    @RequestMapping("/asm/queryDevTypeByAssetType")
    public Map<String,List<DevType>> queryDevTypeByAssetType(String type) {

        Map<String,List<DevType>> map=new HashMap<>();
        List<DevType> devTypes=jpaDevType.findDevTypesByAssertType(type);
        map.put("devs", devTypes);
        return map;
    }
    @RequestMapping("/asm/queryStockList")
    public Map<String,List<Object>> queryStockPage(String type,String dtype,String pre,String next,int pageIndex,String jumpFlag) {
        /**
         *
         * 确定是否是翻页
         *
         * */

        Map<String,List<Object>> map=new HashMap<>();
        List<Page<Assert>> pages=new ArrayList<>();
        Pageable pageable;
        if(!pre.equals("")||!next.equals("")||!jumpFlag.equals("")){
            pageIndex=pageIndex-1;
            pageable=PageRequest.of(pageIndex,pageSize);
            if(!pre.equals("")){
                pageable=pageable.previousOrFirst();
            }else if(!next.equals("")){
                pageable=pageable.next();
            }
        }else {
            pageable=PageRequest.of(0,pageSize);
        }
        Page<Assert> page = asmService.queryStockPage(type,dtype,pageable);
        pages.add(page);
        map.put("page", Collections.singletonList(pages));
        return map;
    }
    @RequestMapping("/asm/search_asset4db_zy")
    public Map<String,List<Object>> search4db_zy(String type,String isDam,String search){


        Map<String,List<Object>> map=new HashMap<>();
        List<Employee> bros=new ArrayList<>();
        List<Assert> asserts  = asmService.queryList(type,isDam,search);
        for (Assert ast:asserts){
            Employee bro=ast.getEmployeeByBorrower();
            if(bro==null){
                bros.add(null);
            }else {
                bros.add(bro);
            }
        }
        map.put("emp", Collections.singletonList(bros));
        map.put("asserts", Collections.singletonList(asserts));
        return map;
    }


    @RequestMapping("/asm/search_asset4check")
    public Map<String,List<Object>> search4check(String type,String isDam,String search){
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
        Map<String,List<Object>> map=new HashMap<>();
        List<Employee> bros=new ArrayList<>();
        List<Assert> asserts  = asmService.queryList(type,isDam,search);
        int asset_size=asserts.size();
        if(assetCkRecords.size()>0) {
            for (int i = asset_size - 1; i >= 0; i--) {
                for (AssetCkRecord assetCkRecord : assetCkRecords) {
                    if (asserts.get(i).getAssestnum().equals(assetCkRecord.getAssetCode())) {
                        asserts.remove(asserts.get(i));
                        break;
                    }
                }
            }
        }

        for (Assert ast:asserts){
            Employee bro=ast.getEmployeeByBorrower();
            if(bro==null){
                bros.add(null);
            }else {
                bros.add(bro);
            }
        }
        map.put("emp", Collections.singletonList(bros));
        map.put("asserts", Collections.singletonList(asserts));
        return map;
    }


    @RequestMapping("/asm/check_done_list")
    public Map<String,List<Object>> check_done(String type){
        String loginUserName= (String) SecurityUtils.getSubject().getPrincipal();
        Employee check_operater= (Employee) SecurityUtils.getSubject().getSession().getAttribute(loginUserName);
        List<AssetCheck> assetChecks=jpaAssetCheck.findAll();
        AssetCheck cu_assetCheck=null;
        for(int i=assetChecks.size()-1;i>=0;i--){
            AssetCheck assetCheck=assetChecks.get(i);
            if(assetCheck.getStatus()!=null && assetCheck.getStatus().equals("close") && assetCheck.getStater()
                    ==check_operater.getId()){
                cu_assetCheck=assetCheck;
                break;
            }
        }
        List<AssetCkRecord> assetCkRecords=jpaAssetCkRecord.findAssetCkRecordsByAssetCheckByAssetCheck(cu_assetCheck);
        Map<String,List<Object>> map=new HashMap<>();
        List<Employee> bros=new ArrayList<>();
        List<Assert> asserts  = asmService.queryList(type,"","");
        if(assetCkRecords.size()>0) {
            for (int i = asserts.size() - 1; i >= 0; i--) {
                for (AssetCkRecord assetCkRecord : assetCkRecords) {
                    if (asserts.get(i).getAssestnum().equals(assetCkRecord.getAssetCode())) {
                        asserts.remove(i);
                        break;
                    }
                }
            }
        }
        for (Assert ast:asserts){
            Employee bro=ast.getEmployeeByBorrower();
            if(bro==null){
                bros.add(null);
            }else {
                bros.add(bro);
            }
        }

        map.put("emp", Collections.singletonList(bros));
        map.put("asserts", Collections.singletonList(asserts));
        return map;
    }

    /**
     *
     * 报损校验
     *
     * */
    @RequiresPermissions("asm:dem:btn")
    @PostMapping("/asm/validForBad")
    public Map<String,String> validForBad(int id){
        Map<String,String> map=new HashMap<>();
        Assert anAssert=jpaAssert.findById(id).get();
        Employee borrower=anAssert.getEmployeeByBorrower();
        if(borrower!=null){
            map.put("error","设备被借出！归还后才能报损");
        }else {
            anAssert.setWorkless("1");
            anAssert.setDamagetime(new java.sql.Date(new java.util.Date().getTime()));
            jpaAssert.save(anAssert);
            String loginUserName= (String) SecurityUtils.getSubject().getPrincipal();
            asmRecordService.write(AsmAction.dev_dam,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute(loginUserName),null,anAssert,"");
            map.put("ok","校验成功");
        }
        return map;
    }
    @RequiresPermissions("asm:delete:btn")
    @PostMapping("/asm/validForDel")
    public Map<String,String> del(int id){
        Map<String,String> map=new HashMap<>();
        Assert anAssert=jpaAssert.findById(id).get();
        Employee borrower=anAssert.getEmployeeByBorrower();
/*        List<OperatRecord> list=jpaOperatRecord.findOperatRecordsByAssertByAssertAsset(anAssert);*/
        if(borrower!=null){
            map.put("error","设备被借出！归还后才能删除");
        }else {
            List<AssetRecord> assetRecords= (List<AssetRecord>) anAssert.getAssetRecordsById();
            jpaAssetRecord.deleteAll(assetRecords);
            jpaAssert.delete(anAssert);
            String loginUserName= (String) SecurityUtils.getSubject().getPrincipal();
            asmRecordService.write(AsmAction.dev_del,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute(loginUserName),null,null,"");
            map.put("ok","删除成功");
        }
        return map;
    }



    @RequiresPermissions("asm:exchange:view")
    @PostMapping("/asm/exchange_req")
    public Map<String,String> exchange(String  reason,String selectDevIds){
        String loginUserName= (String) SecurityUtils.getSubject().getPrincipal();
        Employee employee= (Employee) SecurityUtils.getSubject().getSession().getAttribute(loginUserName);
        SysMail sysMail=jpaMail.findSysMailByForwhat("asm");
        Map<String,String> map=new HashMap<>();
        EchangeDevs echangeDevs=new EchangeDevs();
        String[] ids=selectDevIds.split(",");
        Assert asset=null;
        for (String str:ids){
            if(str.length()>0){
                int id=Integer.parseInt(str);
                asset=jpaAssert.findById(id).get();
            }
        }
        echangeDevs.setReason(reason);
        echangeDevs.setSenderFK(employee);
        echangeDevs.setResiverFK(asset.getEmployeeByBorrower());
        echangeDevs.setSendTime(new Timestamp(new Date().getTime()));
        echangeDevs.setDevFK(asset);
        jpaExchangeDevs.save(echangeDevs);
        if(sysMail!=null){
//            String senderAddr,String senderAccount,String senderPwd,String host,String reciverAddr,String title,String textMsg
            try {
                Sendmail.send(sysMail.getSenderAddr(),sysMail.getSenderAccont(),sysMail.getSenderPwd(),sysMail.getHost(),asset.getEmployeeByBorrower().getEmail().trim(),
                        "设备流转申请","申请设备："+asset.getAname()+" 访问 http://asm.hsaecd.com 审批！");
            }catch (Exception e){
                map.put("ok","发送成功！但是通知邮件发送失败，请通知管理员");
                return map;
            }

        }
        map.put("ok","发送成功！");
        return map;
    }

    /**
     *
     * 批量导入
     *
     * */
    @Transactional
    @RequiresPermissions("asm:inp:view")
    @PostMapping("/asm/putin")
    public Map<String,String> putin(String type, String model, String price, String name, String encode,
                                    String num,String supplier){
        int nu=Integer.parseInt(num);
        String loginUserName= (String) SecurityUtils.getSubject().getPrincipal();
        Map<String,String> map=new HashMap<>();
        List<Assert> list=new ArrayList<>();
        AssetType assetType=jpaAssetType.findAssetTypeByName(type);
        String temp=assetType.getAssetCode();
        if(temp.equals("")&&encode.equals("")){
            for (int i=0;i<nu;i++){
                Assert ast=new Assert();
                ast.setAname(name);
                ast.setPrice(price);
                ast.setModel(model);
                ast.setAssetTypeByAssertType(assetType);
                Suppplier suppplier = jpaSupplier.findSupppliersBySupplierName(supplier).get(0);
                ast.setSuppplierBySupplier(suppplier);

                asmRecordService.write(AsmAction.dev_in,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute(loginUserName),null,null,"");
            }
          //  jpaAssert.saveAll(list);
            for(Assert ast:list){
                Assert anAssert=jpaAssert.save(ast);
                asmRecordService.createAndSaveAssetRecord(AssetAction.putin,anAssert,null,null);

            }

            map.put("ok","入库成功！");
            return map;
        }
        String[] encodes=encode.split("-");
        String zeroInhead="";
        if(encodes[encodes.length-1].charAt(0)=='0'){
            char[] chars=encodes[encodes.length-1].toCharArray();
            for (char c:chars){
                if(c=='0'){
                    zeroInhead=zeroInhead+"0";
                }else {
                    break;
                }
            }
        }
        if(zeroInhead.equals(encodes[encodes.length-1])){
            map.put("error","编号末尾不能全部是0");
            return map;
        }


        String head="";
        int lastIndexof=encode.lastIndexOf("-")+1;
        head=encode.substring(0,lastIndexof);
        int tail=Integer.parseInt(encodes[encodes.length-1]);
        int tailLen=encodes[encodes.length-1].length();

        for (int i=0;i<nu;i++){
            String tailStr=tail+"";
            Assert ast=new Assert();
            ast.setAname(name);
            ast.setPrice(price);
            ast.setModel(model);
            ast.setAssetTypeByAssertType(assetType);

            if((tailStr).length()>tailLen){
                map.put("error","编号超出限定范围，入库失败！");
                return map;
            }else {
                int zeroNum = tailLen - tailStr.length();
                for (int j = 0; j < zeroNum; j++) {
                    tailStr = "0" + tailStr;
                }
            }
            String code=head+tailStr;
            if(jpaAssert.findAssertByAssestnum(code)!=null){
                map.put("error","编号重复，入库失败！");
                return map;
            }
            ast.setAssestnum(code);
            tail++;

            list.add(ast);
    //        asmRecordService.write(AsmAction.dev_in,new Timestamp(new java.util.Date().getTime()), (Employee) SecurityUtils.getSubject().getSession().getAttribute("user"),null,null);
        }

        for(Assert ast:list){
            Assert anAssert=jpaAssert.save(ast);
            asmRecordService.createAndSaveAssetRecord(AssetAction.putin,anAssert,null,null);
        }
        map.put("ok","入库成功！");
        return map;
    }

    /**
     *
     * 上传资产excel
     *
     * */

    @PostMapping("asm/input")
    public Map<String,String> upload(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> json = new HashMap<>();
        request.setCharacterEncoding("UTF-8");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        /** 页面控件的文件流* */
        MultipartFile multipartFile = null;
        Map map = multipartRequest.getFileMap();
        Set set = map.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            multipartFile = (MultipartFile) map.get(iterator.next());
        }
        String filename = multipartFile.getOriginalFilename();
        if (!filename.contains("xlsx")) {
            json.put("error", "文件类型错误");
            return json;
        }
        InputStream inputStream;
        ExcelReader reader = null;
        try {

            inputStream = multipartFile.getInputStream();
            /**
             *
             * 使用easyExcel读上传的表格
             *
             * */


            try {
                reader = EasyExcel.read(inputStream, AssetDownloadModel.class, new ReadAssetEventListener(asmRecordService,jpaAssetRecord,jpaSupplier,jpaAssert, jpaEmployee, jpaAssetType, asmService, jpaOperatRecord,jpaDevType,jpaGroup)).excelType(ExcelTypeEnum.XLSX).build();
                ReadSheet readSheet = EasyExcel.readSheet(0).build();
                reader.read(readSheet);
            } catch (Exception e) {
                json.put("error", e.toString());
                return json;
            }

            /**
             *
             * */

            inputStream.close();
        } catch (Exception e) {
            json.put("error", e.toString());
            return json;
        } finally {
            if (reader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                reader.finish();
            }

        }
        json.put("ok", "上传成功");
        return json;
    }

//    type="+$("#dev_type").val()+"&isDam="+$("#is_damage").val()+"&search="+$("#keywords").val();

    @GetMapping("asm/out")
    public void exportExcel(String type,String isDam,String search,HttpServletResponse response) throws IOException {

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("export", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), AssetDownloadModel.class).sheet("01").doWrite(data(type,isDam,search));




    }

    @GetMapping("asm/out_types")
    public void exportExcel(HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("type_export", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), AssetTypeDownloadModel.class).sheet("01").doWrite(data());


    }

    @GetMapping("asm/out_DevTypes")
    public void exportDevExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("dev_type_export", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), DevTypeDownloadModel.class).sheet("01").doWrite(dev_data());


    }
    /**
     *
     * 把实体类数组转换成excel导出模板对象数组
     *
     * */

    private List<AssetTypeDownloadModel> data(){
        List<AssetType> list = jpaAssetType.findAll();
        List<AssetTypeDownloadModel> models=new ArrayList<>();
        for (AssetType tp:list){
            AssetTypeDownloadModel model=new AssetTypeDownloadModel();
            model.setTname(tp.getTypeName());
            model.setTemplate(tp.getAssetCode());
            model.setCreateTime(tp.getCreateTime().toString());
            if(tp.getRemarks()!=null){
                model.setRemark(tp.getRemarks());
            }
            model.setSign(tp.getPermiCode());
            models.add(model);
        }
        return models;
    }

    private List<DevTypeDownloadModel> dev_data(){
        List<DevType> list = jpaDevType.findAll();
        List<DevTypeDownloadModel> models=new ArrayList<>();
        for (DevType tp:list){
            DevTypeDownloadModel model=new DevTypeDownloadModel();
            model.setDevName(tp.getDevName());
            model.setAssetNumTemplate(tp.getAssetNumTemplate());
            if(tp.getCreateTime()!=null){
                model.setCreateTime(tp.getCreateTime().toString());
            }
            if(tp.getRemarks()!=null){
                model.setRemarks(tp.getRemarks());
            }
            if(tp.getCreator()!=null){
                model.setCreator(tp.getCreator());
            }
            models.add(model);
        }
        return models;
    }


    private List<AssetDownloadModel> data(String type, String isDam, String search){
        List<Assert> list = asmService.queryList(type,isDam,search);
        List<AssetDownloadModel> models=new ArrayList<>();
        for (Assert anAssert:list){
            AssetDownloadModel model=new AssetDownloadModel();

            if(anAssert.getAssestnum()!=null){
                model.setAssetNum(anAssert.getAssestnum());
            }
            if(anAssert.getAssetTypeByAssertType().getTypeName()!=null){
                model.setAssetType(anAssert.getAssetTypeByAssertType().getTypeName());
            }
            if(anAssert.getEmployeeByBorrower()!=null){
                model.setBorrower(anAssert.getEmployeeByBorrower().getEname());
            }
            if(anAssert.getSuppplierBySupplier()!=null){
                model.setProvider(anAssert.getSuppplierBySupplier().getSupplierName());
            }
            if(anAssert.getSysGroupBySysGroup()!=null){
                model.setSysGroup(anAssert.getSysGroupBySysGroup().getGname());
            }
            model.setDevName(anAssert.getAname());
            if(anAssert.getBrotime()!=null){
                model.setBorTime(anAssert.getBrotime().toString());
            }
            if(anAssert.getModel()!=null){
                model.setModel(anAssert.getModel());
            }

            String damFlag=anAssert.getWorkless();
            if(damFlag!=null){
                String dam;
                if(damFlag.equals("0")){
                    dam="完好";
                }else {
                    dam="报废";
                }
                model.setWorkless(dam);
            }

            if(anAssert.getPutintime()!=null){
                model.setPutinTime(anAssert.getPutintime().toString());
            }
            if(anAssert.getPrice()!=null){
                model.setPrice(anAssert.getPrice());
            }
            if(anAssert.getSnnum()!=null){
                model.setSnNum(anAssert.getSnnum());
            }
            if(anAssert.getRemarks()!=null){
                model.setRemarks(anAssert.getRemarks());
            }
            models.add(model);
        }
        return models;
    }



}
