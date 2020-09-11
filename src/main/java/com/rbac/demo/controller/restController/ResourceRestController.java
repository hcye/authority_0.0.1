package com.rbac.demo.controller.restController;

import com.rbac.demo.dtree.Dtree;
import com.rbac.demo.dtree.imp.ResourceDtreeList;
import com.rbac.demo.entity.Resources;
import com.rbac.demo.entity.Role;
import com.rbac.demo.jpa.JpaResources;
import com.rbac.demo.jpa.JpaRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ResourceRestController {
    @Autowired
    private JpaResources jpaResources;
    @Autowired
    private JpaRole jpaRole;
    @Autowired
    private ResourceDtreeList dtreeList;
    @PostMapping("/resource/getResourceTree")
    public List<Dtree> getTree(){
        return dtreeList.getDtree(null);
    }
    @PostMapping("/resource/getResourceTreeWithToolbar")
    public List<Dtree> getTree(int id) {
        Role role=jpaRole.findById(id).get();
        List<Dtree> dtrees=dtreeList.getDtree(role);
        return dtrees;
    }
    @PostMapping("/resource/getMenu")
    public Map<String, Resources> getMenu(int id){
        Map<String,Resources> map=new HashMap<>();
        Resources resources=jpaResources.findById(id).get();
        if(resources==null){
            return null;
        }else {
            map.put("menus",resources);
            return map;
        }
    }
    @PostMapping("/resource/validAddInfo")
    public Map<String, String> vaidData(String name,String identify){
        Map<String,String> map= new HashMap<>();

        System.out.println(name+"---"+identify);

        if (identify.trim().equals("")){
            map.put("error","权限字符必填!");
            return  map;
        }


        if (name.trim().equals("")){
            map.put("error","权限名必填!");
            return  map;
        }

        Resources resources = jpaResources.findResourcesByDescription(name.trim());
        Resources resources1 =jpaResources.findResourcesByPermission(identify.trim());

        if(resources!=null){
            map.put("error","权限名称重复!");
            return  map;
        }

        if(resources1!=null){
            map.put("error","权限名称重复!");
            return map;
        }

        map.put("ok","校验通过");
        return map;
    }
    @PostMapping("/resource/saveNewRes")
//    name:$("#name").val(),identify:$("#identify").val(),url:$("#url").val(),type:$("#type"),upperMenu:$("#upperMenu").val()
    public Map<String, String> save(String url,String type,String name,String identify,String upperMenu){
        Map<String,String> map= new HashMap<>();
        Resources resources=new Resources();
        resources.setUrl(url);
        resources.setDescription(name);
        resources.setPermission(identify);
        resources.setType(type);
        if(!upperMenu.trim().equals("")){
            Resources upperMenus = jpaResources.findResourcesByDescription(upperMenu);
            if(upperMenus!=null){
                resources.setResourcesByParentId(upperMenus);
            }
        }
        jpaResources.saveAndFlush(resources);
        map.put("info","ok");
        return map;
    }
}
