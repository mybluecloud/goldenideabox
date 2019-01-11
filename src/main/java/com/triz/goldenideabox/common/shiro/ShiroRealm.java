package com.triz.goldenideabox.common.shiro;

import com.triz.goldenideabox.model.Resource;
import com.triz.goldenideabox.model.Role;
import com.triz.goldenideabox.model.User;
import com.triz.goldenideabox.service.UserService;
import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * Created by yangqj on 2017/4/21.
 */
public class ShiroRealm extends AuthorizingRealm {



    @javax.annotation.Resource
    private UserService userService;


    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user= (User) SecurityUtils.getSubject().getPrincipal();

        List<Resource> resourcesList = userService.loadUserResources(user.getId());
        List<Role> roles = userService.loadUserRole(user.getId());
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for(Resource resources: resourcesList){
            info.addStringPermission(resources.getResUrl());

        }
        for (Role role: roles) {
            info.addRole(role.getName());
        }
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户的输入的账号.
        String account = (String)token.getPrincipal();
        User user = userService.selectByAccount(account);
        if(user==null) throw new UnknownAccountException();
        if (0 !=user.getState()) {
            throw new LockedAccountException(); // 帐号锁定
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user, //用户
                user.getPassword(), //密码
                ByteSource.Util.bytes(account),
                getName()  //realm name
        );

        // 当验证都通过后，把用户信息放在session里
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("userSession", user);
        session.setAttribute("userSessionId", user.getId());
        session.setTimeout(-1000l);
        return authenticationInfo;
    }



}
