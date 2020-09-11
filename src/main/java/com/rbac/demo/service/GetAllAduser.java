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
import java.util.Properties;

/**
 *
 * 通过ladp获得所有ad域账号信息并加入数据库
 *
 * */

@Component
public class GetAllAduser {
	private String result;
	private String[] results;
	public void getUsers(JpaEmployee jpaEmployee) {
		Properties env = new Properties();
		String adminName = "yehangcheng@hsaecd.com";//username@domain
		String adminPassword = "Yhc14253666";//password
		String ldapURL = "LDAP://192.168.100.10:389";//ip:port
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
			String searchBase = "DC=hsaecd,DC=com";
			String returnedAtts[] = {"*"};  //获得所有信息
			searchCtls.setReturningAttributes(returnedAtts);
			NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter,searchCtls);
			while (answer.hasMoreElements()) {
				SearchResult sr = answer.next();
				result=sr.getName()+","+sr.getAttributes().get("mail");
				results=result.split(",");
				String adUserName=results[0].split("=")[1];
				String depart=results[1].split("=")[1];
				String mail="";
				if(results.length>3) {
					if(results[3].length()>12) {
						mail=results[3].split(": ")[1];
					}
				}
				if(adUserName.length()>15){
					continue;
				}
				Employee employee;
				if(jpaEmployee.findEmployeesByEname(adUserName).size()>0){
					employee=jpaEmployee.findEmployeesByEname(adUserName).get(0);

				}else{
					employee=new Employee();
					employee.setEname(adUserName);
					employee.setPingyin(Chinese2Eng.convertHanzi2Pinyin(adUserName,true));
				}
				employee.setEmail(mail);
				employee.setEdepart(depart);
				jpaEmployee.save(employee);
			}
			ctx.close();
		}catch (NamingException e) {
			e.printStackTrace();
			System.err.println("Problem searching directory: " + e);
		}
	}
}
