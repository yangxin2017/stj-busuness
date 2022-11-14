package com.bjd.smartanalysis.config.sys;

import com.bjd.smartanalysis.relm.CustomRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    //将自己的验证方式加入容器
    @Bean
    public CustomRealm myShiroRealm() {
        CustomRealm customRealm = new CustomRealm();
        return customRealm;
    }

    /**
     * 禁用session, 不保存用户登录状态。保证每次请求都重新认证
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    //权限管理，配置主要是Realm的管理认证
    @Bean
    public DefaultWebSecurityManager securityManager(@Qualifier("myShiroRealm") CustomRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(realm);

        // 关闭 ShiroDAO 功能
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        // 不需要将 Shiro Session 中的东西存到任何地方（包括 Http Session 中）
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

        factoryBean.setLoginUrl("/login");
        factoryBean.setSuccessUrl("/authorized");
        factoryBean.setUnauthorizedUrl("/user/401");

        // 添加自己的过滤器并且取名为jwt
        // 添加 jwt 专用过滤器，拦截除 /login 和 /logout 外的请求
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("jwtFilter", new JWTFilter());
        factoryBean.setFilters(filterMap);

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
//        filterChainDefinitionMap.put("/*", "anon");
        filterChainDefinitionMap.put("/user/login", "anon"); // 可匿名访问
        filterChainDefinitionMap.put("/user/getcode.jpg", "anon"); // 可匿名访问
        //filterChainDefinitionMap.put("/user/logout", "logout"); // 退出登录
        filterChainDefinitionMap.put("/user/logout", "anon"); // 退出登录
        filterChainDefinitionMap.put("/role/*", "anon"); // 可匿名访问
        filterChainDefinitionMap.put("/log/*", "anon"); // 可匿名访问
        filterChainDefinitionMap.put("/user/geturls", "anon"); // 可匿名访问
        filterChainDefinitionMap.put("/user/refresh_info", "anon"); // 可匿名访问
        filterChainDefinitionMap.put("/filemanager/**", "anon"); // 可匿名访问
        filterChainDefinitionMap.put("/check/**", "anon"); //
        filterChainDefinitionMap.put("/fh/**", "anon"); //
        filterChainDefinitionMap.put("/monitor/**", "anon"); //
//        filterChainDefinitionMap.put("/sso/*", "anon"); // 可匿名访问
        filterChainDefinitionMap.put("/user/401", "noSessionCreation");
//        filterChainDefinitionMap.put("/user/**", "anon"); // 需登录才能访问
        filterChainDefinitionMap.put("/**", "jwtFilter"); // 需登录才能访问
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return factoryBean;
    }
}
