package com.rbac.demo.service;

import com.rbac.demo.entity.Assert;
import com.rbac.demo.jpa.JpaAssert;
import com.rbac.demo.tool.ConvertStrForSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class AsmService {

    @Autowired
    private JpaAssert jpaAssert;

    public Map<String,String> valid(String inputCode, String tep){
        Map<String,String> map=new HashMap<>();
        String[] input=inputCode.split("-");
        String[] tp=tep.split("-");
        String engRegex="[A-Za-z]+";
        String numRegex="[0-9]+";
        String allZero="[0]+";

        if(input.length!=tp.length){
            map.put("error","编号输入不匹配模板！");
            return map;
        }else {
            for (int i=0;i<tp.length;i++){
                boolean isMatch=true;

                if(tp[i].length()!=input[i].length()){
                    map.put("error","编号输入不匹配模板！");
                    return map;
                }

                if(tp[i].contains("x")){
                    isMatch = Pattern.matches(engRegex, input[i]);
                }else if(tp[i].contains("0")){
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
        map.put("ok","校验成功");
        return map;
    }

    public Page<Assert> queryPage(String type, String isDam, String search, Pageable pageable){
        Page<Assert> page;
        String damFlag="0";
        if(!isDam.equals("完好")){
            damFlag="1";
        }
        if(search.equals("")){
            page=jpaAssert.findAssertsBytype(type,damFlag,pageable);
        }else {
            search= ConvertStrForSearch.getFormatedString(search);
            page=jpaAssert.findAssertsByAnameLikeAndDamFlag(search,damFlag,pageable);
            if(page.isEmpty()){
                page=jpaAssert.findAssertsByAssestnumLikeAndDamFlag(search,damFlag,pageable);
                if(page.isEmpty()){
                    page=jpaAssert.findAssertsByBorroworPingyinLikeAndDamFlag(search,damFlag,pageable);
                    if(page.isEmpty()){
                        page=jpaAssert.findAssertsByBorroworNameLikeAndDamFlag(search,damFlag,pageable);
                    }
                }
            }
        }
        return page;
    }

    public List<Assert> queryList(String type, String isDam, String search){
        List<Assert> list;
        String damFlag="0";
        if(!isDam.equals("完好")){
            damFlag="1";
        }
        if(search.equals("")){
            list=jpaAssert.findAssertsBytype(type,damFlag);
        }else {
            search= ConvertStrForSearch.getFormatedString(search);
            list=jpaAssert.findAssertsByAnameLikeAndDamFlag(search,damFlag);
            if(list.isEmpty()){
                list=jpaAssert.findAssertsByAssestnumLikeAndDamFlag(search,damFlag);
                if(list.isEmpty()){
                    list=jpaAssert.findAssertsByBorroworPingyinLikeAndDamFlag(search,damFlag);
                    if(list.isEmpty()){
                        list=jpaAssert.findAssertsByBorroworNameLikeAndDamFlag(search,damFlag);
                    }
                }
            }
        }
        return list;
    }
}
