package com.triz.goldenideabox.config;


import com.triz.goldenideabox.common.kaptcha.KaptchaFilter;
import com.triz.goldenideabox.common.shiro.ShiroRealm;
import com.triz.goldenideabox.model.Resource;
import com.triz.goldenideabox.service.UserService;
import freemarker.template.utility.StringUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yangqj on 2017/4/23.
 */
@Configuration
public class ShiroConfig {
    @Autowired(required = false)
    private UserService userService;



    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
     Filter Chain定义说明
     1、一个URL可以配置多个Filter，使用逗号分隔
     2、当设置多个过滤器时，全部验证通过，才视为通过
     3、部分过滤器可指定参数，如perms，roles
     *
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager){

        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");

        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        //验证码过滤器
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("kaptchaFilter", new KaptchaFilter());



        //拦截器.
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();

        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/apps/**","anon");
        filterChainDefinitionMap.put("/global/**","anon");
        filterChainDefinitionMap.put("/layouts/**","anon");
        filterChainDefinitionMap.put("/pages/**","anon");
        filterChainDefinitionMap.put("/static/**","anon");
        filterChainDefinitionMap.put("/templates/**","anon");
        filterChainDefinitionMap.put("/regedit","anon");
        filterChainDefinitionMap.put("/captcha/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        //<!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        //自定义加载权限资源关系
        List<Resource> resourcesList = userService.loadAllResources();
         for(Resource resources:resourcesList){

            if (StringUtil.emptyToNull(resources.getResUrl()) != null) {
                String permission = "perms[" + resources.getResUrl()+ "]";
                filterChainDefinitionMap.put(resources.getResUrl(),permission);
            }
        }
        filterChainDefinitionMap.put("/login", "kaptchaFilter");
        filterChainDefinitionMap.put("/**", "user");


        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        //设置realm.
        securityManager.setRealm(myShiroRealm());

        securityManager.setCacheManager(ehCacheManager());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    private CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    private SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(30*24*60*60);
        simpleCookie.setHttpOnly(true);
        return simpleCookie;
    }

    @Bean(name = "ehCacheManager")
  public EhCacheManager ehCacheManager(){
    EhCacheManager ehCacheManager = new EhCacheManager();
    return ehCacheManager;
  }

    @Bean
    public ShiroRealm myShiroRealm(){
      ShiroRealm myShiroRealm = new ShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     *  所以我们需要修改下doGetAuthenticationInfo中的代码;
     * ）
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));

        return hashedCredentialsMatcher;
    }


    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }



}
