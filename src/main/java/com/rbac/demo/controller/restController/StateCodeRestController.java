package com.rbac.demo.controller.restController;

import com.rbac.demo.entity.StateCode;
import com.rbac.demo.entity.StateCodeProj;
import com.rbac.demo.jpa.JpaProjStatCode;
import com.rbac.demo.jpa.JpaStateCode;
import com.rbac.demo.tool.ExecShell;
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
    @Autowired
    private JpaProjStatCode jpaProjStatCode;
    @PostMapping("/stateCode/add_repo")
    public Map<String, String> add_repo(String name,String repo_url,String repo_type,String projName) {
        Map<String, String> map = new HashMap<>();
        if( name.trim().equals("") || repo_type.trim().equals("") || repo_url.trim().equals("") || projName.trim().equals("")){
            map.put("error","关键字不能为空！");
            return map;
        }
        StateCodeProj codeProj =jpaProjStatCode.findByProjNameAndAndProjType(projName,repo_type);

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
        stateCode.setStateCodeProjByProjId(codeProj);
        jpaStateCode.save(stateCode);
        /*
        *
        * 同步利用ansible 同步数据
        * 在项目目录下创建 创建仓库目录
        * 进入仓库目录 pull 代码
        * ExecShell.execCommand("pull code");
        *
        * */

        map.put("success","新增成功，请给我们一定时间同步数据");
        return map;

    }

    @PostMapping("/stateCode/getProjs")
    public Map<String, List<String>> getProjs(String projType) {

        Map<String, List<String>> map = new HashMap<>();
        List<String> projs=jpaProjStatCode.getProjsByType(projType);
        map.put("types",projs);
        return map;
    }

    @PostMapping("/stateCode/getRepos")
    public Map<String, List<String>> getRepos(String projType,String projName) {

        Map<String, List<String>> map = new HashMap<>();
        List<String> repoNames=jpaStateCode.getReposByTypeAndProj(projType,projName);
        map.put("names",repoNames);
        return map;
    }

    @PostMapping("/stateCode/add_proj")
    public Map<String, String> addProj(String name,String type) {

        Map<String,String> map=new HashMap<>();
        if(name.trim().equals("")){
            map.put("error","项目名不为空！");
            return map;
        }
        StateCodeProj codeProj=jpaProjStatCode.findByProjNameAndAndProjType(name.trim(),type.trim());
        if(codeProj!=null){
            map.put("error","项目名重复！");
            return map;
        }
        codeProj=new StateCodeProj();
        codeProj.setProjName(name);
        codeProj.setProjType(type);
        jpaProjStatCode.save(codeProj);
        map.put("success","新增项目成功");

        /*
        *
        *
        *
        *
        * */
        return map;
    }

    @PostMapping("/stateCode/del_repo")
    public Map<String, String> delRepo(String name,String type,String repoName) {
        Map<String,String> map=new HashMap<>();
        try{
            StateCode stateCode=jpaStateCode.getRepoByTypeAndProjAndRepoName(type,name,repoName);
            /**
             *
             *      ExecShell.execCommand("rm -rf 进入对应项目目录执行删除动作");
             *
             * */
            jpaStateCode.delete(stateCode);
            map.put("success","删除成功");}
        catch (Exception e) {
            map.put("error","遇到错误:"+e);
        }


        /*
         *
         *    ExecShell.execCommand("mkdir 创建项目目录");
         *
         *
         * */
        return map;
    }

    @PostMapping("/stateCode/del_proj")
    public Map<String, String> delProj(String name,String type) {
        Map<String,String> map=new HashMap<>();
        try{
            StateCodeProj codeProj=jpaProjStatCode.findByProjNameAndAndProjType(name,type);
            List<StateCode> stateCodes= (List<StateCode>) codeProj.getStateCodesById();
            if(stateCodes.size()!=0){
                map.put("error","删除失败，项目非空，请清除仓库后再执行删除！");
            }else {
                /**
                 *
                 *      ExecShell.execCommand("rm -rf 进入对应项目目录执行删除动作");
                 *
                 * */
                jpaProjStatCode.delete(codeProj);
                map.put("success","删除成功");
            }
          }
        catch (Exception e) {
            map.put("error","遇到错误:"+e);
        }


        /*
         *
         *    ExecShell.execCommand("mkdir 创建项目目录");
         *
         *
         * */
        return map;
    }

    @PostMapping("/stateCode/to_excel")
    public Map<String, String> toExcel(String path,String timeRange) {
        Map<String,String> map=new HashMap<>();
        if(path.trim().equals("")){
            map.put("error","路径不能为空！");
        }
        try{
                /**
                 *
                 *      ExecShell.execCommand("执行生成excel动作");
                 *
                 * */
                map.put("success","输出成功");

        }
        catch (Exception e) {
            map.put("error","遇到错误:"+e);
        }
        return map;
    }

}
