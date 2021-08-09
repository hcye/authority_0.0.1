package com.rbac.demo.controller.statecode;

import com.rbac.demo.jpa.JpaProjStatCode;
import com.rbac.demo.jpa.JpaStateCode;
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
}