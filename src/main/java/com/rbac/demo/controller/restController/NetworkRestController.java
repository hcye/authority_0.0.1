package com.rbac.demo.controller.restController;

import com.rbac.demo.entity.SwFirm;
import com.rbac.demo.entity.SwOidTemp;
import com.rbac.demo.entity.SwSwitch;
import com.rbac.demo.jpa.JpaSwFirm;
import com.rbac.demo.jpa.JpaSwOidTemp;
import com.rbac.demo.jpa.JpaSwSwitch;
import com.rbac.demo.tool.ConvertStrForSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class NetworkRestController {
    @Autowired
    private JpaSwFirm jpaSwFirm;
    @Autowired
    private JpaSwSwitch jpaSwSwitch;
    @Autowired
    private JpaSwOidTemp jpaSwOidTemp;
    @PostMapping("/sw/add_commit")
    public Map<String,String> sw_add(String block_up,String name,String level, String com, String ip, String firm, String remark, String location, String cascade){
        Map<String,String> map=new HashMap<>();
        if(jpaSwSwitch.findSwSwitchByIpAddr(ip)!=null){
            map.put("ERROR","添加失败，管理地址重复！");
            return map;
        }
        if(jpaSwSwitch.findSwSwitchByLabel(name)!=null){
            map.put("ERROR","添加失败，同名交换机已存在！");
            return map;
        }
        SwSwitch swSwitch=new SwSwitch();
        swSwitch.setIpAddr(ip);
        swSwitch.setSnmpComm(com);
        swSwitch.setCascadePort(cascade);
        swSwitch.setLocation(location);
        swSwitch.setLevel(level);
        swSwitch.setRemark(remark);
        swSwitch.setLabel(name);
        SwFirm firm1 =jpaSwFirm.findSwFirmByFname(firm);
        swSwitch.setSwFirmByFirm(firm1);
        swSwitch.setBlockUp(block_up);
        jpaSwSwitch.save(swSwitch);
        map.put("SUCCESS","添加成功");
        return map;
    }

    @PostMapping("/oid/add_commit")
    public Map<String,String> oid_add(String name, String oid_temp, String firm, String remark){
        Map<String,String> map=new HashMap<>();
        if(jpaSwOidTemp.findSwOidTempByOidName(name)!=null){
            map.put("ERROR","添加失败，同名oid已存在！");
            return map;
        }
        if(jpaSwOidTemp.findSwOidTempByOidTemp(oid_temp)!=null){
            map.put("ERROR","添加失败，相同oid已存在！");
            return map;
        }
        SwOidTemp swOidTemp=new SwOidTemp();

        swOidTemp.setRemark(remark);
        swOidTemp.setOidName(name);
        swOidTemp.setOidTemp(oid_temp);

        SwFirm firm1 =jpaSwFirm.findSwFirmByFname(firm);
        swOidTemp.setSwFirmBySwFirm(firm1);
        jpaSwOidTemp.save(swOidTemp);
        map.put("SUCCESS","添加成功");
        return map;
    }

    @PostMapping("/firm/add_commit")
    public Map<String,String> firm_add(String name,String remark){
        Map<String,String> map=new HashMap<>();
        if(jpaSwFirm.findSwFirmByFname(name)!=null){
            map.put("ERROR","添加失败，同名品牌已存在！");
            return map;
        }
        SwFirm swFirm=new SwFirm();
        swFirm.setFname(name);
        swFirm.setRemark(remark);
        jpaSwFirm.save(swFirm);
        map.put("SUCCESS","添加成功");
        return map;
    }

    @PostMapping("/sw/edit_commit")
    public Map<String,String> sw_edit(int id,String block_up,String name,String level, String com, String ip, String firm, String remark, String location, String cascade){
        Map<String,String> map=new HashMap<>();
        SwSwitch swSwitch=jpaSwSwitch.findById(id).get();
        if(jpaSwSwitch.findSwSwitchByIpAddr(ip)!=null&&jpaSwSwitch.findSwSwitchByIpAddr(ip).getId()!=id){
            map.put("ERROR","修改失败，管理地址重复！");
            return map;
        }
        if(jpaSwSwitch.findSwSwitchByLabel(name)!=null&&jpaSwSwitch.findSwSwitchByLabel(name).getId()!=id){
            map.put("ERROR","修改失败，同名交换机已存在！");
            return map;
        }
        swSwitch.setIpAddr(ip);
        swSwitch.setSnmpComm(com);
        swSwitch.setCascadePort(cascade);
        swSwitch.setLocation(location);
        swSwitch.setLevel(level);
        swSwitch.setRemark(remark);
        swSwitch.setLabel(name);
        SwFirm firm1 =jpaSwFirm.findSwFirmByFname(firm);
        swSwitch.setSwFirmByFirm(firm1);
        swSwitch.setBlockUp(block_up);
        jpaSwSwitch.save(swSwitch);
        map.put("SUCCESS","修改成功");
        return map;
    }

    @PostMapping("/oid/edit_commit")
    public Map<String,String> oid_edit(int id,String name, String oid_temp, String firm, String remark){
        Map<String,String> map=new HashMap<>();
        SwOidTemp swOidTemp=jpaSwOidTemp.findById(id).get();
        if(jpaSwOidTemp.findSwOidTempByOidName(name)!=null&&jpaSwOidTemp.findSwOidTempByOidName(name).getId()!=id){
            map.put("ERROR","修改失败，同名oid已存在！");
            return map;
        }
        if(jpaSwOidTemp.findSwOidTempByOidTemp(oid_temp)!=null&&jpaSwOidTemp.findSwOidTempByOidTemp(oid_temp).getId()!=id){
            map.put("ERROR","修改失败，相同oid模板已存在！");
            return map;
        }
        swOidTemp.setRemark(remark);
        swOidTemp.setOidName(name);
        swOidTemp.setOidTemp(oid_temp);
        SwFirm firm1 =jpaSwFirm.findSwFirmByFname(firm);
        swOidTemp.setSwFirmBySwFirm(firm1);
        jpaSwOidTemp.save(swOidTemp);
        map.put("SUCCESS","编辑成功");
        return map;
    }

    @PostMapping("/firm/edit_commit")
    public Map<String,String> firm_edit(int id,String name,String remark){
        Map<String,String> map=new HashMap<>();
        SwFirm swFirm=jpaSwFirm.findById(id).get();
        SwFirm firm=jpaSwFirm.findSwFirmByFname(name);
        if(firm!=null&&firm.getId()!=id){
            map.put("ERROR","修改失败，同名品牌已存在！");
            return map;
        }

        swFirm.setRemark(remark);
        swFirm.setFname(name);
        jpaSwFirm.save(swFirm);
        map.put("SUCCESS","编辑成功");
        return map;
    }

    @RequestMapping("/sw/getAll")
    public Map<String, List<Object>> getAll(String searchFlag,String name,String firm,String pre,String next,String pageIndex){
        Map<String,List<Object>> map=new HashMap<>();
        List<String> swFirms=new ArrayList<>();
        int pagenow=0;
        int pageSize=10;
        Page<SwSwitch> swSwitches = null;
        //初始页
        if(pageIndex.equals("")){
            Pageable pageable=PageRequest.of(pagenow,pageSize);
            swSwitches =jpaSwSwitch.findAll(pageable);
        }else
        //搜索
        if(!searchFlag.equals("")){
            name=ConvertStrForSearch.getFormatedString(name);
            Pageable pageable=PageRequest.of(pagenow,pageSize);
            if(firm.equals("全部")){
                swSwitches=jpaSwSwitch.findSwSwitchesByLabelLike(name,pageable);
            }else {
                swSwitches=jpaSwSwitch.findSwSwitchesByLabelLikeAndsAndfirm(name,firm,pageable);
            }
        }else
        //普通翻页
        if(name.equals("")&&firm.equals("全部")){
            Pageable pageable=PageRequest.of(Integer.parseInt(pageIndex)-1,pageSize);
            if(pre.equals("1")){
                pageable=pageable.previousOrFirst();
            }
            if (next.equals("1")){
                pageable=pageable.next();
            }
               swSwitches=jpaSwSwitch.findAll(pageable);

        }else
        //带参数翻页
        if(!name.equals("")&&firm.equals("全部")){
            name=ConvertStrForSearch.getFormatedString(name);
            Pageable pageable=PageRequest.of(Integer.parseInt(pageIndex)-1,pageSize);
            if(pre.equals("1")){
                pageable=pageable.previousOrFirst();
            }
            if (next.equals("1")){
                pageable=pageable.next();
            }
            swSwitches=jpaSwSwitch.findSwSwitchesByLabelLike(name,pageable);
        }else if(!firm.equals("全部")){
            name=ConvertStrForSearch.getFormatedString(name);
            Pageable pageable=PageRequest.of(Integer.parseInt(pageIndex)-1,pageSize);
            if(pre.equals("1")){
                 pageable=pageable.previousOrFirst();
            }
            if (next.equals("1")){
                pageable=pageable.next();
            }
            swSwitches=jpaSwSwitch.findSwSwitchesByLabelLikeAndsAndfirm(name,firm,pageable);
        }
        for(SwSwitch swSwitch:swSwitches.toList()){
            String fname=swSwitch.getSwFirmByFirm().getFname();
            swFirms.add(fname);
        }
        map.put("sws", Collections.singletonList(swSwitches));
        map.put("firms", Collections.singletonList(swFirms));
        return map;
    }

    @RequestMapping("/oid/getAll")
    public Map<String, List<Object>> getAllOidTmp(String searchFlag,String name,String firm,String pre,String next,String pageIndex){
        Map<String,List<Object>> map=new HashMap<>();
        List<String> swFirms=new ArrayList<>();
        int pagenow=0;
        int pageSize=10;
        Page<SwOidTemp> oidTemps = null;
        //初始页
        if(pageIndex.equals("")){
            Pageable pageable=PageRequest.of(pagenow,pageSize);
            oidTemps =jpaSwOidTemp.findAll(pageable);
        }else
            //搜索
            if(!searchFlag.equals("")){
                name=ConvertStrForSearch.getFormatedString(name);
                Pageable pageable=PageRequest.of(pagenow,pageSize);
                if(firm.equals("全部")){
                    oidTemps=jpaSwOidTemp.findSwOidTempsByOidNameLike(name,pageable);
                }else {
                    oidTemps=jpaSwOidTemp.findSwOidTempsByOidNameLikeAndSwFirm(name,firm,pageable);
                }
            }else
                //普通翻页
                if(name.equals("")&&firm.equals("全部")){
                    Pageable pageable=PageRequest.of(Integer.parseInt(pageIndex)-1,pageSize);
                    if(pre.equals("1")){
                        pageable=pageable.previousOrFirst();
                    }
                    if (next.equals("1")){
                        pageable=pageable.next();
                    }
                    oidTemps=jpaSwOidTemp.findAll(pageable);

                }else
                    //带参数翻页
                    if(!name.equals("")&&firm.equals("全部")){
                        name=ConvertStrForSearch.getFormatedString(name);
                        Pageable pageable=PageRequest.of(Integer.parseInt(pageIndex)-1,pageSize);
                        if(pre.equals("1")){
                            pageable=pageable.previousOrFirst();
                        }
                        if (next.equals("1")){
                            pageable=pageable.next();
                        }
                        oidTemps=jpaSwOidTemp.findSwOidTempsByOidNameLike(name,pageable);
                    }else if(!firm.equals("全部")){
                        name=ConvertStrForSearch.getFormatedString(name);
                        Pageable pageable=PageRequest.of(Integer.parseInt(pageIndex)-1,pageSize);
                        if(pre.equals("1")){
                            pageable=pageable.previousOrFirst();
                        }
                        if (next.equals("1")){
                            pageable=pageable.next();
                        }
                        oidTemps=jpaSwOidTemp.findSwOidTempsByOidNameLikeAndSwFirm(name,firm,pageable);
                    }
        for(SwOidTemp swOidTemp:oidTemps.toList()){
            String fname=swOidTemp.getSwFirmBySwFirm().getFname();
            swFirms.add(fname);
        }
        map.put("oids", Collections.singletonList(oidTemps));
        map.put("firms", Collections.singletonList(swFirms));
        return map;
    }
    @RequestMapping("/firm/getAll")
    public Map<String, List<Object>> getAllFirm(String searchFlag,String name,String pre,String next,String pageIndex){
        Map<String,List<Object>> map=new HashMap<>();

        int pagenow=0;
        int pageSize=10;
        Page<SwFirm> swFirms = null;
        //初始页
        if(pageIndex.equals("")){
            Pageable pageable=PageRequest.of(pagenow,pageSize);
            swFirms =jpaSwFirm.findAll(pageable);
        }else
            //搜索
            if(!searchFlag.equals("")){
                name=ConvertStrForSearch.getFormatedString(name);
                Pageable pageable=PageRequest.of(pagenow,pageSize);
                swFirms=jpaSwFirm.findSwFirmsByFnameLike(name,pageable);
            }else
                //普通翻页
                if(name.equals("")){
                    Pageable pageable=PageRequest.of(Integer.parseInt(pageIndex)-1,pageSize);
                    if(pre.equals("1")){
                        pageable=pageable.previousOrFirst();
                    }
                    if (next.equals("1")){
                        pageable=pageable.next();
                    }
                    swFirms=jpaSwFirm.findAll(pageable);

                }else
                    //带参数翻页
                    if(!name.equals("")){
                        name=ConvertStrForSearch.getFormatedString(name);
                        Pageable pageable=PageRequest.of(Integer.parseInt(pageIndex)-1,pageSize);
                        if(pre.equals("1")){
                            pageable=pageable.previousOrFirst();
                        }
                        if (next.equals("1")){
                            pageable=pageable.next();
                        }
                        swFirms=jpaSwFirm.findSwFirmsByFnameLike(name,pageable);
                    }
        map.put("firms", Collections.singletonList(swFirms));
        return map;
    }
}
