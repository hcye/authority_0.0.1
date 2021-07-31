package com.rbac.demo.controller.restController;

import com.rbac.demo.entity.Assert;
import com.rbac.demo.entity.EchangeDevs;
import com.rbac.demo.entity.Employee;
import com.rbac.demo.entity.StateCode;
import com.rbac.demo.jpa.JpaStateCode;
import com.rbac.demo.service.RoleService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StateCodeRestController {
    @Autowired
    private JpaStateCode jpaStateCode;
    @PostMapping("/stateCode/add_repo")
    public Map<String, String> add_repo(String name,String repo_url,String repo_type) {
        Map<String, String> map = new HashMap<>();
        List<StateCode> stateCodes = jpaStateCode.findAll();
        for (StateCode stateCode:stateCodes){
            if(name.equals(stateCode.getRepoName())){
                map.put("error","仓库名重复");
                return map;
            }
            if(repo_url.equals(stateCode.getRepoUrl())){
                map.put("error","仓库地址已存在");
                return map;
            }
        }
        Date date=new Date();
        java.sql.Date sqlDate=new java.sql.Date(date.getTime());
        StateCode stateCode=new StateCode();
        stateCode.setAddTime(sqlDate);
        stateCode.setRepoName(name);
        stateCode.setRepoUrl(repo_url);
        stateCode.setRepoType(repo_type);
        jpaStateCode.save(stateCode);
        map.put("success","新增成功，请给我们一定时间同步数据");
        return map;

    }

}
