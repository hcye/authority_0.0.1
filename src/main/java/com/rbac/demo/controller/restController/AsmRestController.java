package com.rbac.demo.controller.restController;

import com.rbac.demo.entity.Assert;
import com.rbac.demo.entity.Employee;
import com.rbac.demo.jpa.JpaAssert;
import com.rbac.demo.jpa.JpaEmployee;
import com.rbac.demo.service.WriteLog;
import com.rbac.demo.tool.ConvertStrForSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AsmRestController {
    private static final int pageSize=10;
    @Autowired
    private JpaEmployee jpaEmployee;
    @Autowired
    private JpaAssert jpaAssert;
    @Autowired
    private WriteLog writeLog;
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

    @PostMapping("/asm/getDevNames")
    public Map<String, List<String>> getDevsNames(String devType){
        Map<String,List<String>> map=new HashMap<>();
        List<String> names=jpaAssert.findDistinctNamesByType(devType);
        map.put("name",names);
        return map;
    }



}
