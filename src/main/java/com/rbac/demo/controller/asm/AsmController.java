package com.rbac.demo.controller.asm;
import com.rbac.demo.entity.Assert;
import com.rbac.demo.entity.AssetType;
import com.rbac.demo.jpa.JpaAssert;
import com.rbac.demo.jpa.JpaAssetType;
import com.rbac.demo.jpa.JpaDevType;
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
    private JpaAssetType jpaAssetType;
    @Autowired
    private JpaAssert jpaAssert;

    @Autowired
    private JpaDevType jpaDevType;
    @GetMapping("/asm/bro")
    public String broPage(Model model){
        //operator
        String loginUser= (String) SecurityUtils.getSubject().getPrincipal();  //操作者用户名
        List<AssetType> assertTypes = jpaAssetType.findAll();   //类型列表
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


        List<String> types= jpaAssetType.findAssertTypeNames();
        String firstType=types.get(0);
        List<String> names=jpaDevType.findDevTypesNameByAssertType(firstType);
        AssetType assetType=jpaAssetType.findAssetTypeByName(firstType);
        String code=assetType.getAssetCode();
        model.addAttribute("types",types);
        model.addAttribute("names",names);
        model.addAttribute("code",code);
        return "/asm/inp";
    }

    @GetMapping("/asm/list")
    public String listPage(Model model){

        List<String> types= jpaAssetType.findAssertTypeNames();
        model.addAttribute("types",types);

        return "/asm/list";
    }

    @GetMapping("/asm/type")
    public String typePage(){

        return "/asm/type";
    }

    @GetMapping("/asm/add_type")
    public String addTypePage(){

        return "/asm/add_type";
    }


    @GetMapping("/asm/edit_type")
    public String editTypePage(int id,Model model){
        AssetType type= jpaAssetType.findById(id).get();
        model.addAttribute("tp",type);
        return "/asm/edit_type";
    }


    @GetMapping("/asm/putin")
    public String putin(String type, String model, String price, String name, String encode, String num, Model model1){
        String[] encodes=encode.split("-");
        String zeroInhead="";
        if(encodes[encodes.length-1].charAt(0)=='0'){
            char[] chars=encodes[encodes.length-1].toCharArray();
            for (char c:chars){
                if(c=='0'){
                    zeroInhead=zeroInhead+"0";
                }else {
                    break;
                }
            }
        }
        if(zeroInhead.equals(encodes[encodes.length-1])){
            model1.addAttribute("error","出现未知错误");
            return "redirect:/asm/inp";
        }
        int nu=Integer.parseInt(num);
        AssetType assetType=jpaAssetType.findAssetTypeByName(type);

        String head="";
        int lastIndexof=encode.lastIndexOf("-")+1;
        head=encode.substring(0,lastIndexof);
        int tail=Integer.parseInt(encodes[encodes.length-1]);

        for (int i=0;i<nu;i++){
            Assert ast=new Assert();
            ast.setAname(name);
            ast.setPrice(price);
            ast.setModel(model);
            ast.setAssetTypeByAssertType(assetType);
            String code=head+zeroInhead+tail;
            ast.setAssestnum(code);
            tail++;
            jpaAssert.save(ast);
        }

        return "redirect:/asm/inp";
    }
//    document.location.href="/asm/putin?type="+$("#tp").val()+"&model="+$("#model").val()+"&price="+$("#price").val()+"&name="+$("#name").val()
//                            +"&encode="+$("#encode").val()+"&num="+$("#num").val();
}