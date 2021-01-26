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
	public void getUsers() {
		Properties env = new Properties();
		String adminName = "yehangcheng@hsaecd.com";//username@domain
		String adminPassword = "Yhc142536...";//password
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
//				System.out.println(sr);
				String res="";
				if(sr.getAttributes().get("mail")!=null){
					res=sr.getAttributes().get("mail").toString();

				}
				result=sr.getAttributes().get("sAMAccountName").toString().split(": ")[1];
				System.out.println(result+"-"+res);
			}
			ctx.close();
		}catch (NamingException e) {
			e.printStackTrace();
			System.err.println("Problem searching directory: " + e);
		}
	}
}
