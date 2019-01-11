package com.triz.goldenideabox.common.shiro;

import com.triz.goldenideabox.model.Resource;
import com.triz.goldenideabox.service.UserService;
import freemarker.template.utility.StringUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yangqj on 2017/4/30.
 */
@Service("shiroService")
public class ShiroService {
    @Autowired
    private ShiroFilterFactoryBean shiroFilterFactoryBean;
    @Autowired
    private UserService userService;

    /**
     * 初始化权限
     */
    public Map<String, String> loadFilterChainDefinitions() {
        // 权限控制map.从数据库获取
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        filterChainDefinitionMap.put("/logout", "logout");

        filterChainDefinitionMap.put("/global/**","anon");
        filterChainDefinitionMap.put("/layouts/**","anon");
        filterChainDefinitionMap.put("/pages/**","anon");
        filterChainDefinitionMap.put("/static/**","anon");
        filterChainDefinitionMap.put("/templates/**","anon");
        filterChainDefinitionMap.put("/regedit","anon");
        List<Resource> resourcesList = userService.loadAllResources();
        for(Resource resources:resourcesList){

            if (StringUtil.emptyToNull(resources.getResUrl()) != null) {
                String permission = "perms[" + resources.getResUrl()+ "]";
                filterChainDefinitionMap.put(resources.getResUrl(),permission);
            }
        }
        filterChainDefinitionMap.put("/**", "user");
        return filterChainDefinitionMap;
    }

    /**
     * 重新加载权限
     */
    public void updatePermission() {

        synchronized (shiroFilterFactoryBean) {

            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean
                        .getObject();
            } catch (Exception e) {
                throw new RuntimeException(
                        "get ShiroFilter from shiroFilterFactoryBean error!");
            }

            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                    .getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
                    .getFilterChainManager();

            // 清空老的权限控制
            manager.getFilterChains().clear();

            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean
                    .setFilterChainDefinitionMap(loadFilterChainDefinitions());
            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean
                    .getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim()
                        .replace(" ", "");
                manager.createChain(url, chainDefinition);
            }


        }
    }


}
