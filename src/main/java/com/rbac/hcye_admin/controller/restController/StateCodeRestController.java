package com.rbac.hcye_admin.controller.restController;

import com.rbac.hcye_admin.entity.StateCode;
import com.rbac.hcye_admin.entity.StateCodeProj;
import com.rbac.hcye_admin.entity.StateUrl;
import com.rbac.hcye_admin.entity.StateUrlSet;
import com.rbac.hcye_admin.jpa.JpaProjStatCode;
import com.rbac.hcye_admin.jpa.JpaStateCode;
import com.rbac.hcye_admin.jpa.JpaStateCodeSet;
import com.rbac.hcye_admin.jpa.JpaStateUrl;
import com.rbac.hcye_admin.tool.ExecShell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    @Autowired
    private JpaStateCodeSet jpaStateCodeSet;
    @Autowired
    private JpaStateUrl jpaStateUrl;
    @PostMapping("/stateCode/add_repo")
    public Map<String, String> add_repo(String name,String repo_url,String repo_type,String projName,String branch) {
        Map<String, String> map = new HashMap<>();
//        {name:$("#svn_repo_name").val(),repo_url:$("#svn_repo_locate").val(),repo_type:"svn",projName: $("#svn_proj_name").val()},
        if( name.trim().equals("") || repo_type.trim().equals("") || repo_url.trim().equals("") || projName.trim().equals("")){
            map.put("error","关键字不能为空！");
            return map;
        }
        name=name.trim();
        repo_url=repo_url.trim();
        repo_type=repo_type.trim();
        projName=projName.trim();
        if(branch!=null){
            branch=branch.trim();
        }
        StateCodeProj codeProj =jpaProjStatCode.findByProjNameAndAndProjType(projName,repo_type);
        List<StateCode> stateCodes = jpaStateCode.findAll();
        for (StateCode stateCode:stateCodes){
            if(name.equals(stateCode.getRepoName())){
                map.put("error","仓库名重复");
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
        /*
         * 关键字
         * name 仓库文件夹名
         * repo_url 仓库地址
         * codeProj 获得前缀目录
         * 同步利用ansible 同步数据
         * 在项目目录下创建 创建仓库目录
         * 进入仓库目录 pull 代码
         * ExecShell.execCommand("pull code");
         *
         * */
        if(branch==null){
            branch="";
        }
        String cmd="python3 /opt/bin/task.py makeRepo "+repo_url+" "+repo_type+" "+projName+" "+name+" "+"branch:"+branch;
        System.out.println(cmd);
        String res=ExecShell.execCommand(cmd);
        System.out.println(res);
        if(res.contains("error:clone")){
            map.put("error","新增失败");
            return map;
        }else {
            jpaStateCode.save(stateCode);
        }



        map.put("success","新增成功");
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
        name=name.trim();
        type=type.trim();
        StateCodeProj codeProj=jpaProjStatCode.findByProjNameAndAndProjType(name,type);
        if(codeProj!=null){
            map.put("error","项目名重复！");
            return map;
        }
        codeProj=new StateCodeProj();
        codeProj.setProjName(name);
        codeProj.setProjType(type);
        String cmd="python3 /opt/bin/task.py makeProject "+type+" "+name;
        String res=ExecShell.execCommand(cmd);
        System.out.println(res);
        System.out.println(cmd);
        if(res.contains("error")){
            map.put("error",res);
        }else {
            jpaProjStatCode.save(codeProj);
            map.put("success","新增项目成功");
        }


        /*
        * 添加项目
        * ExecShell.execCommand("pull code");
        *
        *
        * */

        return map;
    }

    @PostMapping("/stateCode/del_repo")
    public Map<String, String> delRepo(String name,String type,String repoName) {
        Map<String,String> map=new HashMap<>();
        if(name.equals("") || repoName.equals("")){
            map.put("error","关键字不为空！");
            return map;
        }
        name=name.trim();
        repoName=repoName.trim();
        try{
            StateCode stateCode=jpaStateCode.getRepoByTypeAndProjAndRepoName(type,name,repoName);
            String res=ExecShell.execCommand("python3 /opt/bin/task.py rmRepo "+type+" "+name+" "+repoName);
            System.out.println(res);
            if(res.contains("error")){
                map.put("error",res);
            }else {
                jpaStateCode.delete(stateCode);
                map.put("success","删除成功");
            }
            /**
             *
             *      ExecShell.execCommand("rm -rf 进入对应项目目录执行删除动作");
             *
             * */
            }
        catch (Exception e) {
            map.put("error","遇到错误:"+e);
        }
        return map;
    }

    @PostMapping("/stateCode/del_proj")
    public Map<String, String> delProj(String name,String type) {

        Map<String,String> map=new HashMap<>();
        if(name.equals("")){
            map.put("error","关键字不为空！");
            return map;
        }
        name=name.trim();
        try{
            StateCodeProj codeProj=jpaProjStatCode.findByProjNameAndAndProjType(name,type);
            List<StateCode> stateCodes= (List<StateCode>) codeProj.getStateCodesById();
            if(stateCodes.size()!=0){
                map.put("error","删除失败，项目非空，请清除仓库后再执行删除！");
            }else {
                String res=ExecShell.execCommand("python3 /opt/bin/task.py rmProject "+type+" "+name);
                System.out.println(res);
                if(res.contains("error")){
                    map.put("error",res);
                }else {
                    jpaProjStatCode.delete(codeProj);
                    map.put("success","删除成功");
                }

            }
          }
        catch (Exception e) {
            map.put("error","遇到错误:"+e);
        }

        return map;
    }

    @PostMapping("/stateCode/to_excel")
    public Map<String, String> toExcel(String path,String timeRange) {
        Map<String,String> map=new HashMap<>();
        if(path.equals("") || timeRange.equals("")){
            map.put("error","关键字不为空！");
            return map;
        }
        path=path.trim();
        timeRange=timeRange.trim();
        String start_time=timeRange.split(" - ")[0];
        String end_time=timeRange.split(" - ")[1];
        String[] paths=path.split("/");
        String type="";
        for(String p:paths){
            if(p.equals("git")){
                type="git";
                break;
            }
            if(p.equals("svn")){
                type="svn";
                break;
            }
        }


        try{
            String res=ExecShell.execCommand("python3 /opt/bin/task.py makeExcel_"+type+" "+path+" "+start_time+" "+end_time);
            System.out.println(res);
            if(res.contains("error")){
                map.put("error",res);

            }else {
                map.put("success","输出成功");
            }


        }
        catch (Exception e) {
            map.put("error","遇到错误:"+e);
        }
        return map;
    }

    @PostMapping("/stateCode/validSetName")
    public Map<String, String> addSet(String name) {
        Map<String,String> map=new HashMap<>();
        if(name==null || name.trim().equals("")){
            map.put("error","关键字不为空！");
            return map;
        }
        StateUrlSet urlSet =jpaStateCodeSet.findStateUrlSetBySetName(name);
        if(urlSet!=null){
            map.put("error","集合名重复！");
        }
        return map;
    }
    @PostMapping("/stateCode/addSetUrl")
    public Map<String, String> addRow(String name,String url,String remark,String setName) {
        Map<String,String> map=new HashMap<>();
        StateUrlSet set=jpaStateCodeSet.findStateUrlSetBySetName(setName);
        List<StateUrl> stateUrls=jpaStateUrl.findStateUrlsByStateUrlSetBySetFk(set);

        if(name==null || name.trim().equals("")){
            map.put("error","关键字不为空！");
            return map;
        }

        if(!url.contains("http://")){
            map.put("error","url格式不符合规则！");
            return map;
        }

        StateUrlSet urlSet =jpaStateCodeSet.findStateUrlSetBySetName(name);
        if(urlSet!=null){
            map.put("error","集合名重复！");
            return map;
        }

        for(StateUrl stateUrl:stateUrls){
            if(stateUrl.getUrlName().equals(name)){
                map.put("error","同一个集合下url名不能相同！");
                return map;
            }
        }

        StateUrl stateUrl=new StateUrl();
        stateUrl.setStateUrl(url);
        stateUrl.setStateUrlSetBySetFk(set);
        stateUrl.setUrlName(name);
        stateUrl.setRemark(remark);
        jpaStateUrl.save(stateUrl);
        map.put("success","新增成功！");
        return map;
    }

    @PostMapping("/stateCode/getSetRows")
    public Map<String, List<StateUrl>> getSetRows(String setName) {
        Map<String, List<StateUrl>> map=new HashMap<>();
        StateUrlSet set=null;
        if(setName.equals("")){
            List<StateUrlSet> urlSets=jpaStateCodeSet.findAll();
            set=urlSets.get(0);
        }else {
            set=jpaStateCodeSet.findStateUrlSetBySetName(setName);
        }
        List<StateUrl> stateUrls=jpaStateUrl.findStateUrlsByStateUrlSetBySetFk(set);


        map.put("urls",stateUrls);
        return map;
    }


    @PostMapping("/stateCode/delSetUrl")
    public Map<String, String> delSetRows(int id) {
        Map<String, String> map=new HashMap<>();
        try{
            jpaStateUrl.deleteById(id);
            map.put("success","删除成功");
        }catch (Exception e){
            System.out.println(e);
            map.put("error","删除失败");
        }
        return map;
    }


    @PostMapping("/stateCode/output")
    public Map<String, String> output(String name,String timeRange)  {
        Map<String, String> map=new HashMap<>();
        StateUrlSet urlSet=jpaStateCodeSet.findStateUrlSetBySetName(name);
        List<StateUrl> stateUrls= (List<StateUrl>) urlSet.getStateUrlsById();
        if(timeRange.equals("")){
            map.put("error","关键字不为空！");
            return map;
        }

        timeRange=timeRange.trim();
        String start_time=timeRange.split(" - ")[0];
        String end_time=timeRange.split(" - ")[1];


        for(StateUrl stateUrl:stateUrls){
            String path=stateUrl.getStateUrl();
            System.out.println(path);
            String[] paths=path.split("/");
            String type="";
            for(String p:paths){
                if(p.equals("git")){
                    type="git";
                    break;
                }
                if(p.equals("svn")){
                    type="svn";
                    break;
                }
            }


            try{
                String res=ExecShell.execCommand("python3 /opt/bin/task.py makeExcel_"+type+" "+path+" "+start_time+" "+end_time+" "+name);
                System.out.println(res);
                if(res.contains("error")){

                    map.put("error",res);
                    break;

                }else {
                    map.put("success","输出成功");
                }


            }
            catch (Exception e) {
                map.put("error","遇到错误:"+e);
                break;
            }
        }

        if(map.containsKey("success")){
            String res=ExecShell.execCommand("python3 /opt/bin/task.py package");
            if(res.contains("path:")){
                String target_file=res.split("path:")[1];
                System.out.println(target_file);
                map.put("success",target_file);
            }else {
                ExecShell.execCommand("python3 /opt/bin/task.py clean");

            }
        }else {
            String res1=ExecShell.execCommand("python3 /opt/bin/task.py clean");
            System.out.println(res1);

        }
        return map;
    }
    @PostMapping("/stateCode/update")
    public Map<String, String> update(String url) {
        Map<String, String> map=new HashMap<>();
        String[] paths=url.split("/");
        String type="";
        for(String p:paths){
            if(p.equals("git")){
                type="git";
                break;
            }
            if(p.equals("svn")){
                type="svn";
                break;
            }
        }

        try{
            String res=ExecShell.execCommand("python3 /opt/bin/task.py update_"+type+" "+url);
            System.out.println(res);
            if(res.contains("error")){
                map.put("error",res);
            }else {
                map.put("success","更新成功!");
            }
        }catch (Exception e){
            System.out.println(e);
            map.put("error","遇到错误"+e);
        }
        return map;
    }

    @GetMapping("/stateCode/export_zip")
    public void exportModel(HttpServletResponse response,String path) throws IOException {
        OutputStream stream = null;
        try {
            stream =new BufferedOutputStream(response.getOutputStream()) ;

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            stream.flush();
            stream.close();
        }
//        InputStream inputStream =new FileInputStream(file);
        File file=new File(path);
        InputStream inputStream = new FileInputStream(file);
        response.setHeader("Content-disposition", "attachment; filename=" + "release.zip");
        response.setContentType("application/octet-stream;charset=UTF-8");//设置类型
        response.setHeader("Pragma", "No-cache");//设置头
        response.setHeader("Cache-Control", "no-cache");//设置头
        response.setDateHeader("Expires", 0);//设置日期头
        byte[] bytes=new byte[1024];
        try {

            int i=0;
            while(((i=inputStream.read(bytes))!=-1)){
                stream.write(bytes,0,i);
            }

            // 清理输出目录
            String res=ExecShell.execCommand("python3 /opt/bin/task.py clean");
            System.out.println(res);
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            return;
        }finally {
            stream.flush();
            stream.close();
            inputStream.close();
        }
    }
   //$.post("/stateCode/getRepo_url", {repoName:$("#r_repo_name").val()},
   @PostMapping("/stateCode/getRepo_url")
   public Map<String, String> getRepo_url(String  repoName,String type,String proName) {
       Map<String, String> map=new HashMap<>();
       try{
           StateCode stateCode=jpaStateCode.getRepoByTypeAndProjAndRepoName(type,proName,repoName);
           String url=stateCode.getRepoUrl();
           map.put("url",url);
       }catch (Exception e){
           System.out.println(e);
           map.put("error","删除失败");
       }
       return map;
   }

    @PostMapping("/stateCode/delSet")
    public Map<String, String> delSet(String  name) {
        Map<String, String> map=new HashMap<>();
        try{
            StateUrlSet stateUrlSet=jpaStateCodeSet.findStateUrlSetBySetName(name);
            jpaStateCodeSet.delete(stateUrlSet);


        }catch (Exception e){
            System.out.println(e);
            map.put("error","删除失败,集合不为空！");
        }
        return map;
    }

    @PostMapping("/stateCode/setUrl_update")
    public Map<String, String> urlUpdate(String  name,String setUrl,String remark,int id,String setName) {
        Map<String, String> map=new HashMap<>();
        StateUrlSet stateUrlSet=jpaStateCodeSet.findStateUrlSetBySetName(setName);
        List<StateUrl> stateUrls= (List<StateUrl>) stateUrlSet.getStateUrlsById();
        // 查看同一集合下不同对象是否重名！
        for(StateUrl stateUrl:stateUrls){
            if(stateUrl.getUrlName().equals(name)&&id!=stateUrl.getId()){
                map.put("error","修改失败，重名");
                return map;
            }
        }
        StateUrl stateUrl=jpaStateUrl.findById(id).get();
        stateUrl.setUrlName(name);
        stateUrl.setStateUrl(setUrl);
        stateUrl.setRemark(remark);
        jpaStateUrl.save(stateUrl);
        map.put("success","修改成功！");
        return map;
    }

}
