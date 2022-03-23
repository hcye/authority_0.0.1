package com.rbac.hcye_admin.service;

import com.rbac.hcye_admin.entity.Employee;
import com.rbac.hcye_admin.entity.SysGroup;
import com.rbac.hcye_admin.jpa.JpaEmployee;
import com.rbac.hcye_admin.jpa.JpaGroup;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 通过ad域同步用户表
 */


@Component
public class UpdateUserDB {
    public static void updateUserTable(JpaEmployee jpaEmployee, JpaGroup jpaGroup, String adip, String adname, String username, String userpwd) throws NamingException {
        NamingEnumeration<SearchResult> answer=getNamingEnumeration( adip,  adname,  username,  userpwd);
        List<String> allLADPUserList = new ArrayList<String>();

        List<Employee> allUser = jpaEmployee.findAll();

        int flag = 0;
        //遍历AD域
        while (answer.hasMoreElements()) {
            String[] results;
            String adUserName = "";
            String depart = "";
            String result = "";
            String loginName = "";
            String mail = "";
            SearchResult sr = answer.next();

            if(sr.getAttributes().get("mail")!=null){
                mail=sr.getAttributes().get("mail").toString().split(": ")[1];
            }
            result = sr.getName() + "," + mail + "," + sr.getAttributes().get("userPrincipalName");
            results = result.split(",");
            depart = results[1].split("=")[1];
            adUserName = result.split("=")[1].split(",")[0];
            if (results.length > 4) {
                loginName = sr.getAttributes().get("sAMAccountName").toString().split(": ")[1];;
            } else {
                continue;
            }


            if (results.length > 2) {
                if (results[3].length() > 12) {
                    if(sr.getAttributes().get("mail")!=null){
                        mail=sr.getAttributes().get("mail").toString().split(": ")[1];
                    }
                }
            }
            if (adUserName.length() > 15) {
                continue;
            }
            allLADPUserList.add(loginName);
            //------------
            for (int i = 0; i < allUser.size(); i++) {
                flag = 0;
                Employee employee=allUser.get(i);
                if (employee.getLoginName()!= null && employee.getLoginName().trim().equals(loginName.trim())) {
                    /**
                     * 判断用户是否已存在，更新用户信息！
                     *         String adUserName = "";
                     *         String depart = "";
                     *         String mail = "";
                     *         String result = "";
                     * */
                    /**
                     * 更新显示用户名
                     * */
                    if(!adUserName.equals(employee.getEname())){
                        employee.setEname(adUserName);
                    }
                    /**
                     * 更新部门
                     * */
                    if(employee.getSysGroupByGroupId()==null){
                        continue;
                    }
                    if(!depart.equals(employee.getSysGroupByGroupId().getGname())){
                        List<SysGroup> groups=jpaGroup.findAll();
                        for (SysGroup group:groups){
                            if(depart.equals(group.getGname())){
                                employee.setSysGroupByGroupId(group);
                                break;
                            }
                        }
               /*         if(!fla){
                            throw new RuntimeException("ad域部门名与系统不匹配，如果是新部门请在系统内新增部门，部门名称要求和ad域分组名相同！");
                        }*/

                    }
                    /**
                     * 更新邮箱地址
                     * */

                    if(employee.getEmail()!=null&&!mail.equals(employee.getEmail())){
                        employee.setEmail(mail);
                    }

                    jpaEmployee.save(employee);
                    flag = 1;
                    break;
                }
            }
            SysGroup sysGroup=jpaGroup.findSysGroupByGname(depart);
            if (flag == 0&&sysGroup!=null) {
                Employee employee = new Employee();
                String pingyin = Chinese2Eng.convertHanzi2Pinyin(adUserName, true);
                employee.setEname(adUserName);
                employee.setEmail(mail);
                employee.setEdepart(depart);
                employee.setSysGroupByGroupId(sysGroup);
                employee.setPingyin(pingyin);
                employee.setLoginName(loginName);
                employee.setOnjob("0");
                jpaEmployee.saveAndFlush(employee);
            }
        }


        int flag1 = 0;
        for (int i = 0; i < allUser.size(); i++) {
            for (int j = 0; j < allLADPUserList.size(); j++) {
                flag1 = 0;
                if (allUser.get(i).getLoginName()!= null && allUser.get(i).getLoginName().trim().equals(allLADPUserList.get(j))) {
                    flag1 = 1;
                    break;
                }
            }
            try {
                if (flag1 == 1) {
                    Employee employees = jpaEmployee.findEmployeeByLoginName(allUser.get(i).getLoginName());
                    employees.setOnjob("0");
                    jpaEmployee.saveAndFlush(employees);
                }
            }catch (Exception e){
                throw new RuntimeException("系统中发现登录名重复的用户："+allUser.get(i).getLoginName()+",删除后重新刷新！");
            }




        /*    if (flag1 == 0) {
                if(allUser.get(i).getLoginName()!=null){
                    Employee employee = jpaEmployee.findEmployeeByLoginName(allUser.get(i).getLoginName().trim());
                    employee.setOnjob("1");
                    jpaEmployee.saveAndFlush(employee);
                }
            }*/
        }

    }
    public static NamingEnumeration<SearchResult> getNamingEnumeration( String adip, String adname, String username, String userpwd) throws NamingException {
        Properties env = new Properties();
        String adminName = username;//username@domain
        String adminPassword = userpwd;//password
        String ldapURL = "LDAP://" + adip + ":389";//ip:port
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//"none","simple","strong"
        env.put(Context.SECURITY_PRINCIPAL, adminName);
        env.put(Context.SECURITY_CREDENTIALS, adminPassword);
        env.put(Context.PROVIDER_URL, ldapURL);

        LdapContext ctx = new InitialLdapContext(env, null);
        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(objectCategory=person)(objectClass=user)(name=*))";
        String searchBase = "DC=" + adname + ",DC=com";
        String returnedAtts[] = {"*"};
        searchCtls.setReturningAttributes(returnedAtts);
        NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter, searchCtls);
        ctx.close();
        return  answer;
    }
}
