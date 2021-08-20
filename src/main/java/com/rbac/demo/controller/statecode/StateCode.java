package com.rbac.demo.controller.statecode;

import com.rbac.demo.entity.StateUrlSet;
import com.rbac.demo.jpa.JpaProjStatCode;
import com.rbac.demo.jpa.JpaStateCode;
import com.rbac.demo.jpa.JpaStateCodeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

//@RestController
//public class StateCode {
//    @Autowired
//    private JpaRtcBug jpaRtcBug;

@Controller
public class StateCode {
    @Autowired
    private JpaProjStatCode jpaProjStatCode;
    @Autowired
    private JpaStateCodeSet jpaStateCodeSet;
    @GetMapping("/stateCode/count")
    public String stateCode(Model model){
        List<String> gitProjs=null;
        List<String> svnProjs=null;
        gitProjs=jpaProjStatCode.getDistinctProjNameWhereTypeIsGit();
        svnProjs=jpaProjStatCode.getDistinctProjNameWhereTypeIsSvn();
        model.addAttribute("git_projs",gitProjs);
        model.addAttribute("svn_projs",svnProjs);
        return "codeCount/statecode";
    }
    @GetMapping("/stateCode/setCount")
    public String stateCodeSet(Model model){

        List<StateUrlSet> urlSets=jpaStateCodeSet.findAll();
        model.addAttribute("names",urlSets);
        return "codeCount/setstate";
    }
    @GetMapping("/stateCode/setAdd")
    public String addCodeSet(String setName,String setRemark){
        setName=setName.trim();
        setRemark=setRemark.trim();
        StateUrlSet stateUrlSet=new StateUrlSet();
        stateUrlSet.setRemark(setRemark);
        stateUrlSet.setSetName(setName);
        jpaStateCodeSet.save(stateUrlSet);
        return "redirect:/stateCode/setCount";
    }

    @GetMapping("/stateCode/addView")
    public String addView(){

        return "codeCount/add";
    }

}