package com.rbac.demo.service;

import com.rbac.demo.entity.Employee;
import com.rbac.demo.jpa.JpaEmployee;
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

    public static void updateUserTable(JpaEmployee jpaEmployee, String adip, String adname, String username, String userpwd) throws NamingException {
        NamingEnumeration<SearchResult> answer=getNamingEnumeration( adip,  adname,  username,  userpwd);
        List<String> allLADPUserList = new ArrayList<String>();
        List<Employee> allUser = jpaEmployee.findAll();
        String[] results;
        int flag = 0;
        String adUserName = "";
        String depart = "";
        String mail = "";
        String result = "";
        String loginName = "";
        //遍历AD域
        while (answer.hasMoreElements()) {
            SearchResult sr = answer.next();
            result = sr.getName() + "," + sr.getAttributes().get("mail") + "," + sr.getAttributes().get("userPrincipalName");
            results = result.split(",");


            depart = results[1].split("=")[1];
            adUserName = result.split("=")[1].split(",")[0];
            if (results.length > 4) {
                loginName = results[4].split(": ")[1].split("@")[0];
            } else {
                continue;
            }


            if (results.length > 3) {
                if (results[3].length() > 12) {
                    mail = results[3].split(": ")[1];
                }
            }
            if (adUserName.length() > 15) {
                continue;
            }
            allLADPUserList.add(loginName);
            //------------
            for (int i = 0; i < allUser.size(); i++) {
                flag = 0;
                if (allUser.get(i).getLoginName()!= null && allUser.get(i).getLoginName().trim().equals(loginName.trim())) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                Employee employee = new Employee();
                String pingyin = Chinese2Eng.convertHanzi2Pinyin(adUserName, true);
                employee.setEname(adUserName);
                employee.setEmail(mail);
                employee.setEdepart(depart);
                employee.setPingyin(pingyin);
                employee.setLoginName(loginName);
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
            if (flag1 == 1) {
                Employee employees = jpaEmployee.findEmployeeByLoginName(allUser.get(i).getLoginName());
                employees.setOnjob("0");
                jpaEmployee.save(employees);



            }
            if (flag1 == 0) {
                if(allUser.get(i).getLoginName()!=null){
                    Employee employee = jpaEmployee.findEmployeeByLoginName(allUser.get(i).getLoginName().trim());
                    employee.setOnjob("1");
                    jpaEmployee.save(employee);
                }
            }
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
