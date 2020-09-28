package com.rbac.demo.controller.restController;

import com.rbac.demo.entity.Assert;
import com.rbac.demo.entity.AssetType;
import com.rbac.demo.entity.DevType;
import com.rbac.demo.entity.Employee;
import com.rbac.demo.jpa.JpaAssert;
import com.rbac.demo.jpa.JpaAssetType;
import com.rbac.demo.jpa.JpaDevType;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.service.TypeService;
import com.rbac.demo.service.WriteLog;
import com.rbac.demo.tool.ConvertStrForSearch;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

@RestController
public class AsmRestController {
    private static final int pageSize=10;
    @Autowired
    private JpaEmployee jpaEmployee;
    @Autowired
    private JpaAssert jpaAssert;
    @Autowired
    private JpaAssetType jpaAssetType;
    @Autowired
    private WriteLog writeLog;
    @Autowired
    private JpaDevType jpaDevType;

    @Autowired
    private TypeService typeService;
    //        {astType:type,name:name,search:searchKey,pre:pre,next:next,pageIndex:pageIndex},
    @PostMapping("/asm/queryPage")
    public Page<Assert> queryPage(String name,String search,String pre,String next,int pageIndex,String jumpFlag){
        /**
         *
         * 确定是否是翻页
         *
         * */
        Page<Assert> page;
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
        if(search.equals("")){
            page=jpaAssert.findAssertsByAname(name,pageable);
        }else {
            search=ConvertStrForSearch.getFormatedString(search);
            page=jpaAssert.findAssertsByAname(search,pageable);
            if(page.isEmpty()){
                page=jpaAssert.findAssertsByAssestnum(search,pageable);
            }
        }
        return page;
    }
    @PostMapping("/asm/operat")
    public Map<String, List<Assert>> borrow(String selectDevIds,String name,String actionFlag){
        Map<String,List<Assert>> map=new HashMap<>();
        String[] ids=selectDevIds.split(",");
        List<Employee> employees=jpaEmployee.findEmployeesByEname(name);
        Employee borrower=employees.get(0);
        if(actionFlag.equals("bo")){
            for (String str:ids){
                if(!str.trim().equals("")){
                    Assert ast=jpaAssert.findById(Integer.parseInt(str)).get();
                    ast.setEmployeeByBorrower(borrower);
                    jpaAssert.saveAndFlush(ast);
                }
            }
        }else if(actionFlag.equals("ret")){
            for (String str:ids){
                if(!str.trim().equals("")){
                    Assert ast=jpaAssert.findById(Integer.parseInt(str)).get();
                    ast.setEmployeeByBorrower(null);
                    jpaAssert.saveAndFlush(ast);
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
        List<Employee> employees=jpaEmployee.findEmployeesByEname(name);
        Employee returner=employees.get(0);
        List<Assert> asserts= (List<Assert>) returner.getAssertsById();
        map.put("devs",asserts);
        return map;
    }

    @PostMapping("/asm/addAssetType")
    public Map<String, String> getDevsNames(String devType,String template,String desc,String authority){
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
        assertType.setPermiCode(authority);
        jpaAssetType.save(assertType);
        map.put("ok","新增成功!");
        return map;
    }


    @PostMapping("/asm/saveAssetType")
    public Map<String, String> saveType(int id,String devType,String template,String desc,String authority){
        Map<String ,String> map=new HashMap<>();
        devType=devType.trim();
        if(authority.equals("")){
            map.put("error","权限字符不能为空!");
            return map;
        }
        AssetType type = jpaAssetType.findAssetTypeByName(devType);
        AssetType assertTypeOld= jpaAssetType.findById(id).get();
        if(type!=null&&type.getId()!=id){
            map.put("error","类型名称重复,修改失败!");
            return map;
        }
        assertTypeOld.setAssetCode(template);
        assertTypeOld.setRemarks(desc);
        assertTypeOld.setTypeName(devType);
        assertTypeOld.setPermiCode(authority);
        jpaAssetType.save(assertTypeOld);
        map.put("ok","修改成功!");
        return map;
    }

    @PostMapping("/asm/getDevNames")
    public Map<String, List<String>> addAssertType(String devType, Model model){
        Map<String,List<String>> map=new HashMap<>();
        AssetType assetType=jpaAssetType.findAssetTypeByName(devType);
        String code=assetType.getAssetCode();
        List<String> codes=new ArrayList<>();
        codes.add(code);
        List<String> names=jpaDevType.findDevTypesNameByAssertType(devType);
        map.put("name",names);
        map.put("code",codes);
        return map;
    }

    @PostMapping("/asm/addDevType")
    public Map<String, String> addDevType(String devType,String dev_name,String desc){
        Map<String,String> map=new HashMap<>();
        AssetType assertType= jpaAssetType.findAssetTypeByName(devType);
        DevType dtp=jpaDevType.findDevTypeByDevNameAndAssertType(dev_name,assertType);
        if(dtp!=null){

            map.put("error","资产名称重复！请重新填写");
            return map;
        }
        DevType devType1=new DevType();

        Timestamp ts=new Timestamp(new Date().getTime());
        String usname= (String) SecurityUtils.getSubject().getPrincipal();
        devType1.setCreateTime(ts);
        devType1.setCreator(usname);
        devType1.setRemarks(desc);
        devType1.setDevName(dev_name);
        devType1.setAssetTypeByAssertTypeId(assertType);
        jpaDevType.save(devType1);
        map.put("ok","增加完成");
        return map;
    }





    @PostMapping("/asm/getTypes")
    public Page<AssetType> getTypes(String name,String pre,String next,int pageNow){
        return  typeService.getPage( name, pre, next, pageNow);
    }

    @RequiresPermissions("asm:type:delete")
    @PostMapping("/asm/deleteType")
    public Map<String,String> deleteType(int id){
        Map<String,String> map=new HashMap<>();
        AssetType type= jpaAssetType.findById(id).get();
        Collection<Assert> asserts=type.getAssertsById();
        if(asserts.isEmpty()){
            type.setDevTypesById(null);
            type.setAssertsById(null);
            jpaAssetType.delete(type);
            map.put("ok","删除成功");
        }else {
            map.put("error","删除失败");
        }
        return map;
    }
    @PostMapping("/asm/validInputAssetNum")
    public Map<String,String> valid(String inputCode,String tep,String num,String model,String price){
        Map<String,String> map=new HashMap<>();
        String[] input=inputCode.split("-");
        String[] tp=tep.split("-");
        String engRegex="[A-Za-z]+";
        String numRegex="[0-9]+";
        String allZero="[0]+";
        if(model.equals("")){
            map.put("error","型号不能为空！");
            return map;
        }
        if(!Pattern.matches(numRegex, num)){
            map.put("error","数量必填且只接受数字！");
            return map;
        }
        if(!price.equals("")){
            if(!Pattern.matches(numRegex, price)){
                map.put("error","价格只接受数字！");
                return map;
            }
        }

        if(input.length!=tp.length){
            map.put("error","编号输入不匹配模板！");
            return map;
        }else {
            for (int i=0;i<tp.length;i++){
                boolean isMatch=true;
                if(tp[i].equals("x")){
                    isMatch = Pattern.matches(engRegex, input[i]);
                }else if(tp[i].equals("0")){
                    isMatch = Pattern.matches(numRegex, input[i]);
                    if(Pattern.matches(allZero, input[i])){
                        map.put("error","编号尾数不能全是0！");
                        return map;
                    }
                }
                if(!isMatch){
                    map.put("error","编号输入不匹配模板！");
                    return map;
                }
            }
        }
        map.put("ok","");
        return map;
    }

    @PostMapping("/asm/queryListPage")
    public Page<Assert> queryListPage(String type,String isDam,String search,String pre,String next,int pageIndex,String jumpFlag){
        /**
         *
         * 确定是否是翻页
         *
         * */



        Page<Assert> page;
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
        if(search.equals("")){
            if(isDam.equals("完好")){
                page=jpaAssert.findAssertsBytype(type,"0",pageable);
            }else {
                page=jpaAssert.findAssertsBytype(type,"1",pageable);
            }
        }else {
            search=ConvertStrForSearch.getFormatedString(search);
            page=jpaAssert.findAssertsByAnameLike(search,pageable);
            if(page.isEmpty()){
                page=jpaAssert.findAssertsByAssestnumLike(search,pageable);
                if(page.isEmpty()){
                    page=jpaAssert.findAssertsByBorroworPingyinLike(search,pageable);
                    if(page.isEmpty()){
                        page=jpaAssert.findAssertsByBorroworNameLike(search,pageable);
                    }
                }
            }
        }
        return page;
    }


    @PostMapping("/asm/validForBad")
    public Map<String,String> validForBad(int id){
        Map<String,String> map=new HashMap<>();
        Employee borrower=jpaAssert.findById(id).get().getEmployeeByBorrower();
        if(borrower!=null){
            map.put("error","设备被借出！归还后才能报损");
        }else {
            map.put("ok","校验成功");
        }
        return map;
    }
}
