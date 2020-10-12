package com.rbac.demo.shiro;


import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.rbac.demo.realm.UserRealm;
import net.sf.ehcache.CacheManager;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean(name="SecurityManager")    //以securityManager注入
    public DefaultWebSecurityManager getDefaultSecurityManager(@Qualifier("userRealm")UserRealm userRealm){
        DefaultWebSecurityManager defaultSecurityManager=new DefaultWebSecurityManager();
        EhCacheManager manager=new EhCacheManager();
        manager.setCacheManagerConfigFile("classpath:ehcache.xml");
        defaultSecurityManager.setCacheManager(manager);
        defaultSecurityManager.setSessionManager(sessionManager());
        userRealm.setCredentialsMatcher(myCredentialsMatcher());
        defaultSecurityManager.setRealm(userRealm);
        return defaultSecurityManager;
    }



    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(60*1*1000);
        //设置sessionDao对session查询，在查询在线用户service中用到了
        sessionManager.setSessionDAO(sessionDAO());
        //配置session的监听
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        listeners.add(new BDSessionListener());
        sessionManager.setSessionListeners(listeners);
        //设置在cookie中的sessionId名称
        sessionManager.setSessionIdCookie(simpleCookie());
        return sessionManager;
    }
    @Bean
    public SessionDAO sessionDAO(){
        return new MemorySessionDAO();
    }


    @Bean
    public MyCredentialsMatcher myCredentialsMatcher(){
        return new MyCredentialsMatcher();
    }
    @Bean
    public SimpleCookie simpleCookie(){

        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("session.id");

        return simpleCookie;
    }
    @Bean   //spring把该对象实例化进内存，可以通过 @Autowire 直接获得实例
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("userRealm")UserRealm userRealm){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        DefaultWebSecurityManager defaultSecurityManager=new DefaultWebSecurityManager();
        defaultSecurityManager.setRealm(userRealm);
        shiroFilterFactoryBean.setSecurityManager(defaultSecurityManager);

        // 身份认证失败，则跳转到登录页面的配置
        shiroFilterFactoryBean.setLoginUrl("/");
        //添加Shiro内置过滤器
        /**
         * Shiro内置过滤器，可以实现权限相关的拦截器
         *    常用的过滤器：
         *       anon: 无需认证（登录）可以访问
         *       authc: 必须认证才可以访问
         *       user: 如果使用rememberMe的功能可以直接访问
         *       perms： 该资源必须得到资源权限才可以访问
         *       role: 该资源必须得到角色权限才可以访问
         */
        Map<String,String> filter=new HashMap<>();
        filter.put("/logout", "logout");
        filter.put("/img/**","anon");
        filter.put("/css/**","anon");
        filter.put("/font/**","anon");
        filter.put("/js/**","anon");
        filter.put("/index","anon");
        filter.put("/layui/**","anon");
        filter.put("/layui_ext/**","anon");
        filter.put("/login","anon");  //登录界面不需要认证即可访问
        filter.put("/","anon");  //登录界面不需要认证即可访问
        filter.put("/user/**","authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filter);
        return shiroFilterFactoryBean;
    }


    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启aop注解支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    //获得Realm
    @Bean
    public UserRealm getUserRealm(EhCacheManager ehCacheManager) {
        UserRealm userRealm=new UserRealm();


        userRealm.setCacheManager(ehCacheManager);   //添加缓存管理器

        userRealm.setAuthenticationCachingEnabled(true);
        userRealm.setAuthorizationCachingEnabled(true);
        userRealm.setCachingEnabled(true);   //开启缓存
        userRealm.setAuthenticationCacheName("authenticationCache");   //添加认证缓存
        userRealm.setAuthorizationCacheName("authorizationCache");     //添加授权缓存
        return userRealm;
    }

    /**
     * 页面上使用shiro标签
     * @return
     */
    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }


    @Bean
    public EhCacheManager ehCacheManager() {



        net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.getCacheManager("hcye");

        EhCacheManager em = new EhCacheManager();
        if (cacheManager==null)
        {
            em.setCacheManager(new net.sf.ehcache.CacheManager(getCacheManagerConfigFileInputStream()));
            return em;
        }
        else
        {

            em.setCacheManager(cacheManager);
            return em;
        }
    }

    protected InputStream getCacheManagerConfigFileInputStream()
    {
        String configFile = "classpath:ehcache.xml";
        InputStream inputStream = null;
        try
        {
            inputStream = ResourceUtils.getInputStreamForPath(configFile);
            byte[] b = IOUtils.toByteArray(inputStream);
            InputStream in = new ByteArrayInputStream(b);
            return in;
        }
        catch (IOException e)
        {
            throw new ConfigurationException(
                    "Unable to obtain input stream for cacheManagerConfigFile [" + configFile + "]", e);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }



}
