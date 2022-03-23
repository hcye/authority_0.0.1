package com.rbac.hcye_admin.controller.restController;

import com.rbac.hcye_admin.dtree.Dtree;
import com.rbac.hcye_admin.dtree.imp.ResourceDtreeList;
import com.rbac.hcye_admin.entity.Resources;
import com.rbac.hcye_admin.entity.Role;
import com.rbac.hcye_admin.entity.Role2Resources;
import com.rbac.hcye_admin.jpa.JpaResources;
import com.rbac.hcye_admin.jpa.JpaRole;
import com.rbac.hcye_admin.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ResourceRestController {
    @Autowired
    private RoleService roleService;
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
    @RequiresPermissions("asm:menu:view")
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
    public Map<String, String> vaidData(int id,String name,String identify){
        Resources resourcesEdit=null;
        if(id!=0){
            resourcesEdit=jpaResources.findById(id).get();
        }
        Map<String,String> map= new HashMap<>();


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
            if(id!=0){
                if(!resourcesEdit.equals(resources)){
                    map.put("error","权限名称重复!");
                    return  map;
                }
            }else if(id==0){
                map.put("error","权限名称重复!");
                return  map;
            }
        }

        if(resources1!=null){
            if(id!=0){
                if(!resourcesEdit.equals(resources1)){
                    map.put("error","权限字符重复!");
                    return  map;
                }
            }else if(id==0){
                map.put("error","权限字符重复!");
                return map;
            }
        }

        map.put("ok","校验通过");
        return map;
    }
    @PostMapping("/resource/saveNewRes")
//    name:$("#name").val(),identify:$("#identify").val(),url:$("#url").val(),type:$("#type"),upperMenu:$("#upperMenu").val()
    public Map<String, String> save(int id,String url,String type,String name,String identify,String upperMenu){
        Resources resources;
        if(id==0){
            resources=new Resources();
        }else {
            resources=jpaResources.findById(id).get();
        }
        Map<String,String> map= new HashMap<>();

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
        //为管理员添加新增权限
        jpaResources.saveAndFlush(resources);
        roleService.adminAddMenu(resources);

        map.put("info","ok");
        return map;
    }
    @RequiresPermissions("asm:menus:delete")
    @PostMapping("/resource/delete")
    public Map<String,String> delete(int id){
        Map<String,String> map= new HashMap<>();
        Resources resources=jpaResources.findById(id).get();
        List<Role2Resources> role2ResourcesList = (List<Role2Resources>) resources.getRole2ResourcesById();
        if(resources.getResourcesById().size()>0){
            map.put("error","权限有子权限，无法删除！");
            return map;
        }
        if(role2ResourcesList!=null&&role2ResourcesList.size()>0){
            String baseString="权限被";
            for (Role2Resources role2Resources:role2ResourcesList){
                Role role=role2Resources.getRoleByRoleId();
                if(role.getAuthorityCode().equals("administrator")){
                    continue;
                }
                baseString=baseString+role.getRname()+"，";
            }
            if(baseString.equals("权限被")){
                resources.setResourcesByParentId(null);
                roleService.adminRemoveMenu(resources);
                jpaResources.delete(resources);
                map.put("ok","删除成功");
                return map;
            }
            map.put("error",baseString+"引用，无法删除！");

        }else {
            resources.setResourcesByParentId(null);
            //移除管理员的引用
            roleService.adminRemoveMenu(resources);
            jpaResources.delete(resources);
            map.put("ok","删除成功");
        }
        return map;
    }
    @RequiresPermissions("asm:menus:edit")
    @PostMapping("/resource/edit")
//    name:$("#name").val(),identify:$("#identify").val(),url:$("#url").val(),type:$("#type"),upperMenu:$("#upperMenu").val()
    public Map<String, String> edit(int id,String url,String type,String name,String identify,String upperMenu){
        Map<String,String> map= new HashMap<>();
        Resources resources=jpaResources.findById(id).get();
        return map;
    }
}
