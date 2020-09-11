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
 *
 * 通过ad域同步用户表
 *
 * */


@Component
public class UpdateUserDB {
private String result;
	public static void  updateUserTable(JpaEmployee jpaEmployee,String adip,String adname,String username,String userpwd) {
		Properties env = new Properties();
		String adminName = username;//username@domain
		String adminPassword = userpwd;//password
		String ldapURL = "LDAP://"+adip+":389";//ip:port
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");//"none","simple","strong"
		env.put(Context.SECURITY_PRINCIPAL, adminName);
		env.put(Context.SECURITY_CREDENTIALS, adminPassword);
		env.put(Context.PROVIDER_URL, ldapURL);
		try {
			LdapContext ctx = new InitialLdapContext(env, null);
			SearchControls searchCtls = new SearchControls();
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String searchFilter = "(&(objectCategory=person)(objectClass=user)(name=*))";
			String searchBase = "DC="+adname+",DC=com";
			String returnedAtts[] = {"*"};
			//临时变量j

			searchCtls.setReturningAttributes(returnedAtts);
			NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter,searchCtls);
			List<String> allLADPUserList=new ArrayList<String>();
			List<Employee> allUser=jpaEmployee.findAll();
			String[] results;
			int flag=0;
			String adUserName="";
			String depart="";
			String mail="";
			String result="";
			//遍历AD域
			while (answer.hasMoreElements()) {
				SearchResult sr = answer.next();
				result = sr.getName() + "," + sr.getAttributes().get("mail");
				results = result.split(",");
				depart = results[1].split("=")[1];
				adUserName = result.split("=")[1].split(",")[0];
				if (results.length > 3) {
					if (results[3].length() > 12) {
						mail = results[3].split(": ")[1];
					}
				}
				if (adUserName.length() > 15) {
					continue;
				}
				allLADPUserList.add(adUserName);
				//------------
				for (int i = 0; i < allUser.size(); i++) {
					flag = 0;
					if (allUser.get(i).getEname().trim().equals(adUserName)) {
						flag = 1;
						break;
					}
				}
				if(flag==0)
				{
					Employee employee=new Employee();
					String pingyin=Chinese2Eng.convertHanzi2Pinyin(adUserName,true);
					employee.setEname(adUserName);
					employee.setEmail(mail);
					employee.setEdepart(depart);
					employee.setPingyin(pingyin);
					jpaEmployee.saveAndFlush(employee);
				}
			}


			int flag1=0;
			for (int i = 0; i < allUser.size(); i++) {
				for(int j=0;j<allLADPUserList.size();j++)
				{
					flag1=0;
					if(allUser.get(i).getEname().trim().equals(allLADPUserList.get(j)))
					{
						flag1=1;
						break;
					}
				}
				if(flag1==1)
				{
					List<Employee> employees= jpaEmployee.findEmployeesByEname(allUser.get(i).getEname().trim());
					for(Employee employe:employees){
						employe.setOnjob("0");
						jpaEmployee.save(employe);
					}


				}
				if(flag1==0)
				{
					List<Employee> employees= jpaEmployee.findEmployeesByEname(allUser.get(i).getEname().trim());
					for(Employee employe:employees){
						employe.setOnjob("1");
						jpaEmployee.saveAndFlush(employe);
					}
				}
			}
			ctx.close();
		}catch (NamingException e) {
			e.printStackTrace();
			System.err.println("Problem searching directory: " + e);
		}
	}
}
