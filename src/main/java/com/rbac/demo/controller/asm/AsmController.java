package com.rbac.demo.controller.asm;
import com.rbac.demo.entity.Assert;
import com.rbac.demo.entity.AssertType;
import com.rbac.demo.jpa.JpaAssert;
import com.rbac.demo.jpa.JpaAssertType;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class AsmController {
    private static final int pageSize=10;
    @Autowired
    private JpaAssertType jpaAssertType;
    @Autowired
    private JpaAssert jpaAssert;
    @GetMapping("/asm/bro")
    public String broPage(Model model){
        //operator
        String loginUser= (String) SecurityUtils.getSubject().getPrincipal();  //操作者用户名
        List<AssertType> assertTypes =jpaAssertType.findAll();   //类型列表
        Pageable pageable=PageRequest.of(0,pageSize);   //初始化第一页
        List<String> assertNames=jpaAssert.getDistinctAssertNames(assertTypes.get(0));
        Page<Assert> asserts =jpaAssert.findAssertsByDevice(assertNames.get(0),pageable);  //初始化使用第一个类型，的第一个设备类型
        model.addAttribute("operator",loginUser);
        model.addAttribute("assertTypes",assertTypes);
        model.addAttribute("asserts",asserts);
        model.addAttribute("assertList",asserts.getContent());
        model.addAttribute("assertNames",assertNames);
        return "/asm/bro";
    }
    @GetMapping("/asm/ret")
    public String retPage(Model model){
        String loginUser= (String) SecurityUtils.getSubject().getPrincipal();  //操作者用户名
        model.addAttribute("operator",loginUser);
        return "/asm/ret";
    }

    @GetMapping("/asm/inp")
    public String inpPage(Model model){


        List<String> types=jpaAssertType.findAssertTypeNames();
        model.addAttribute("types",types);

        return "/asm/inp";
    }
}
