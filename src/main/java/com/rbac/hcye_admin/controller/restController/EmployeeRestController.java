package com.rbac.hcye_admin.controller.restController;

import ch.ethz.ssh2.crypto.digest.MD5;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.rbac.hcye_admin.easyExcel.ReadUserEventListener;
import com.rbac.hcye_admin.easyExcel.UserModel;
import com.rbac.hcye_admin.entity.*;
import com.rbac.hcye_admin.jpa.*;
import com.rbac.hcye_admin.service.Chinese2Eng;
import com.rbac.hcye_admin.service.UpdateUserDB;
import com.rbac.hcye_admin.service.UserService;
import com.rbac.hcye_admin.shiro.ShiroUtils;
import com.rbac.hcye_admin.tool.ConvertStrForSearch;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

@RestController
public class EmployeeRestController {
    @Autowired
    private JpaOperatRecord jpaOperatRecord;
    @Autowired
    private JpaRole jpaRole;
    @Autowired
    private JpaEmployee jpaEmployee;
    @Autowired
    private JpaUser2Role jpaUser2Role;
    @Autowired
    private UserService service;
    @Autowired
    private JpaGroup jpaGroup;
    @Autowired
    private JpaAdinfo jpaAdinfo;
    @PostMapping("/user/findUserByNameLike")
    public List<String> findUsers(String name){
        List<String> names=new ArrayList<>();
        name= ConvertStrForSearch.getFormatedString(name);
        List<Employee> employees;
        employees=jpaEmployee.findEmployeesByEnameLike(name);
        if(employees.size()==0){
            employees=jpaEmployee.findEmployeesByPingyinLike(name);
        }
        String str="";
        for (Employee employee:employees){
            str=employee.getEname()+"-"+employee.getLoginName();
            names.add(str);
        }
        return names;
    }

    @PostMapping("/user/findUserByNameLikeForReturn")
    public List<String> findUsersForReturn(String name){
        List<String> names=new ArrayList<>();
        name= ConvertStrForSearch.getFormatedString(name);
        List<Employee> employees;
        employees=jpaEmployee.findEmployeesByEnameLikeForReturn(name);
        if(employees.size()==0){
            employees=jpaEmployee.findEmployeesByPingyinLikeForReturn(name);
        }
        String str="";
        for (Employee employee:employees){
            str=employee.getEname()+"-"+employee.getLoginName();
            names.add(str);
        }
        return names;
    }

    @PostMapping("/user/doEdit")
//    id:$("#id").val(),email:$("#email").val(),sex:$("#sex").val(),status:$("#status"),group:$("#group"),pwd:$("#pwd"),roles:$("#roles").val()
    public Map<String,String> editUser(int id,String email,String sex,String status,String group,String pwd,String roles){
        Map<String,String> map=new HashMap<>();

        SysGroup sysGroup =jpaGroup.findSysGroupByGname(group);
        String[] rolesList=roles.split(",");

        Employee employee=jpaEmployee.findById(id).get();
        List<Role> cu_roles=jpaEmployee.findRoleByEmployee(employee);
        boolean flag=false;
        if(cu_roles.size()!=0){
            for(Role role1:cu_roles){
                if(role1.getRname().equals("超级管理员")){
                    flag=true;
                }
            }
        }
        if(status.equals("启用")){
            employee.setStatus((byte) 0);
        }else {
            employee.setStatus((byte) 1);
        }
        employee.setEmail(email);
        employee.setSex(sex);
        employee.setSysGroupByGroupId(sysGroup);
        if(!pwd.equals("")){
            pwd= ShiroUtils.encryption(pwd, ByteSource.Util.bytes(employee.getPingyin()).toHex());
            employee.setPwd(pwd);
        }
        /**
         * 删除原来的权限
         *
         * */
        List<User2Role> user2RolesBinded=jpaUser2Role.findUser2RolesByEmployeeByUserId(employee);
        jpaUser2Role.deleteAll(user2RolesBinded);
        /**
         * 更新权限
         * */

        List<Role> all_roles=new ArrayList<>();
        for(String str:rolesList){
            if(str.equals("")){
                continue;
            }
            Role role=jpaRole.findById(Integer.parseInt(str)).get();
            all_roles.add(role);
        }
        if(flag==true){
            all_roles.add(jpaRole.findById(1).get());
        }

        for(Role role:all_roles){
            User2Role user2Role=new User2Role();
            user2Role.setRoleByRoleId(role);
            user2Role.setEmployeeByUserId(employee);
            jpaUser2Role.save(user2Role);
        }

        jpaEmployee.save(employee);
        map.put("ok","ok");
        return map;
    }
    @PostMapping("/user/doAdd")
//    id:$("#id").val(),email:$("#email").val(),sex:$("#sex").val(),status:$("#status"),group:$("#group"),pwd:$("#pwd"),roles:$("#roles").val()
    public Map<String,String> addUser(String name,String email,String sex,String status,String group,String pwd,String roles){
        Map<String,String> map=new HashMap<>();
        String lname=Chinese2Eng.convertHanzi2Pinyin(name,true);
        Employee employeec=jpaEmployee.findEmployeeByLoginName(lname);

        if(employeec!=null&&employeec.getId()!=0){
            map.put("error","登录名重复");
            return map;
        }
        SysGroup sysGroup =jpaGroup.findSysGroupByGname(group);
        String[] rolesList=roles.split(",");

        Employee employee=new Employee();
        if(status.equals("启用")){
            employee.setStatus((byte) 0);
        }else {
            employee.setStatus((byte) 1);
        }
        String py=Chinese2Eng.convertHanzi2Pinyin(name,true);
        employee.setOnjob("0");
        employee.setPingyin(py);
        employee.setEdepart(group);
        employee.setLoginName(lname);
        employee.setEname(name);
        employee.setEmail(email);
        employee.setSex(sex);
        employee.setSysGroupByGroupId(sysGroup);
        if(!pwd.equals("")){
            pwd= ShiroUtils.encryption(pwd, ByteSource.Util.bytes(employee.getPingyin()).toHex());
            employee.setPwd(pwd);
        }

        /**
         * 新增权限
         * */
        Employee employee_saved=jpaEmployee.save(employee);
        for(String str:rolesList){
            if(str.equals("")){
                continue;
            }
            Role role=jpaRole.findById(Integer.parseInt(str)).get();
            User2Role user2Role=new User2Role();
            user2Role.setRoleByRoleId(role);
            user2Role.setEmployeeByUserId(employee_saved);
            jpaUser2Role.save(user2Role);
        }
        jpaEmployee.save(employee);
        map.put("ok","ok");
        return map;
    }
    @PostMapping("/user/getPage")
    public Page<Employee> turn(String depId, String pageInput, String turnFlag, String keyWord, String refreshFlag) throws NamingException {
        Page<Employee> page= service.getPage( depId, pageInput, turnFlag,keyWord,refreshFlag);
        return page;
    }

    @PostMapping("/user/validForDel")
    public Map<String,String> valid(int id) {
        Map<String,String> map=new HashMap<>();
        Employee employee=jpaEmployee.findEmployeeById(id);
        if (employee.getAssertsById().size()>0){
            map.put("error","用户保有资产，请归还后再试");
        }else {
            List<User2Role> user2Roles=jpaUser2Role.findUser2RolesByEmployeeByUserId(employee);
            for (User2Role user2Role:user2Roles){
                jpaUser2Role.delete(user2Role);
            }
            List<OperatRecord> operatRecords=jpaOperatRecord.findOperatRecordsByEmployeeByAssertEmp(employee);
            for (OperatRecord record:operatRecords){
                jpaOperatRecord.delete(record);
            }
            List<OperatRecord> operatRecordss=jpaOperatRecord.findOperatRecordsByEmployeeByDealer(employee);
            for (OperatRecord record:operatRecordss){
                jpaOperatRecord.delete(record);
            }
            employee.setExchangeResiver(null);
            employee.setExchangeSender(null);
            employee.setOperatRecordsById(null);
            employee.setOperatRecordsById_0(null);
            employee.setSysGroupByGroupId(null);

            jpaEmployee.delete(employee);
            map.put("ok","删除成功");
        }
        return map;
    }

    @PostMapping("/user/checkAdinfo")
    public Map<String,String> check(String dc,String dc_ip,String uname,String pwd)  {
        Map<String,String> map=new HashMap<>();
        Adinfo adinfo =jpaAdinfo.findAll().get(0);
        try {
            if(dc==null){
                NamingEnumeration<SearchResult> res= UpdateUserDB.getNamingEnumeration( adinfo.getAdip(), adinfo.getDc(), adinfo.getDomainadminname(),adinfo.getDomainadminpwd());
                if(!res.hasMoreElements()){
                    throw new RuntimeException("加载ad资源出错");
                }
            }else if(dc!=null){
                NamingEnumeration<SearchResult> res= UpdateUserDB.getNamingEnumeration( dc_ip, dc, uname,pwd);
                if(!res.hasMoreElements()){
                    throw new RuntimeException("加载ad资源出错");
                }else {
                    adinfo.setAdip(dc_ip);
                    adinfo.setDc(dc);
                    adinfo.setDomainadminname(uname);
                    adinfo.setDomainadminpwd(pwd);
                    jpaAdinfo.save(adinfo);
                }
            }

        }catch (RuntimeException | NamingException e){
            map.put("error","ad域信息验证失败！");
            return map;
        }
        map.put("ok","ad域信息验证成功！");
        return map;
    }

    @PostMapping("/user/getAdinfo")
    public Map<String,Adinfo> get()  {
        Map<String,Adinfo> map=new HashMap<>();
        Adinfo adinfo =jpaAdinfo.findAll().get(0);
        map.put("ok",adinfo);
        return map;
    }

    /**
     *
     * upload user excel
     *
     * */

    @PostMapping("user/input")
    public Map<String,String> upload(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> json = new HashMap<>();
        request.setCharacterEncoding("UTF-8");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        /** 页面控件的文件流* */
        MultipartFile multipartFile = null;
        Map map = multipartRequest.getFileMap();
        Set set = map.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            multipartFile = (MultipartFile) map.get(iterator.next());
        }
        String filename = multipartFile.getOriginalFilename();
        if (!filename.contains("xlsx")) {
            json.put("error", "文件类型错误");
            return json;
        }
        InputStream inputStream;
        ExcelReader reader = null;
        try {

            inputStream = multipartFile.getInputStream();
            /**
             *
             * 使用easyExcel读上传的表格
             *
             * */


            try {
                reader = EasyExcel.read(inputStream, UserModel.class, new ReadUserEventListener( jpaGroup,  jpaEmployee, jpaOperatRecord)).excelType(ExcelTypeEnum.XLSX).build();
                ReadSheet readSheet = EasyExcel.readSheet(0).build();
                reader.read(readSheet);
            } catch (Exception e) {
                json.put("error", e.toString());
                return json;
            }

            /**
             *
             * */

            inputStream.close();
        } catch (Exception e) {
            json.put("error", e.toString());
            return json;
        } finally {
            if (reader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                reader.finish();
            }

        }
        json.put("ok", "上传成功");
        return json;
    }


    @PostMapping("/user/reset_password")
    public Map<String,String> turn(String oldPwd,String newPwd,String pwdConf){
        Map<String,String> map=new HashMap<>();
        String usname= (String) SecurityUtils.getSubject().getPrincipal();
        Employee employee= (Employee) SecurityUtils.getSubject().getSession().getAttribute(usname);
        String dbPwd=employee.getPwd();
        String encryptPwd=ShiroUtils.encryption(oldPwd,ByteSource.Util.bytes(employee.getLoginName()).toHex());

        if(!dbPwd.equals(encryptPwd)){
            map.put("error","旧密码不匹配");
            return map;
        }
        if(newPwd.length()>=8){
            if(newPwd.equals(pwdConf)){
                String newp=ShiroUtils.encryption(newPwd,ByteSource.Util.bytes(employee.getPingyin()).toHex());
                employee.setPwd(newp);
                jpaEmployee.save(employee);
                map.put("success","修改成功");
                return map;
            }
        }else {
            map.put("error","修改失败");
            return map;
        }
        return map;
    }
}
