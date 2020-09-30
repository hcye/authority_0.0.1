package com.rbac.demo.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class AsmService {
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
}
