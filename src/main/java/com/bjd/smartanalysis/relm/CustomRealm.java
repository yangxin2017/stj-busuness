package com.bjd.smartanalysis.relm;

import com.bjd.smartanalysis.config.sys.JWTToken;
import com.bjd.smartanalysis.config.sys.JWTUtil;
import com.bjd.smartanalysis.entity.sys.SysPermission;
import com.bjd.smartanalysis.entity.sys.SysRole;
import com.bjd.smartanalysis.entity.sys.SysUser;
import com.bjd.smartanalysis.service.sys.SysPermissionService;
import com.bjd.smartanalysis.service.sys.SysRoleService;
import com.bjd.smartanalysis.service.sys.SysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CustomRealm extends AuthorizingRealm {
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysPermissionService permissionService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SysUser user = (SysUser) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        SysRole role = roleService.getById(user.getRoleId());
        List<SysPermission> perms = permissionService.getPermsByRoleId(user.getRoleId());

        if(role != null) {
            simpleAuthorizationInfo.addRole(role.getName());
        }
        if(perms != null) {
            for(SysPermission p: perms) {
                simpleAuthorizationInfo.addStringPermission(p.getPerm());
            }
        }
        return simpleAuthorizationInfo;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token invalid");
        }

        SysUser userBean = userService.getUserByName(username);
        if (userBean == null) {
            throw new AuthenticationException("User didn't existed!");
        }

        if (! JWTUtil.verify(token, username, userBean.getPassword())) {
            throw new AuthenticationException("Username or password error");
        }

        return new SimpleAuthenticationInfo(userBean, token, getName());
    }
}
