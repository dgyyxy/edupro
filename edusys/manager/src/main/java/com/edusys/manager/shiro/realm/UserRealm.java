package com.edusys.manager.shiro.realm;

import com.edu.common.dao.model.EduPermission;
import com.edu.common.dao.model.EduRole;
import com.edu.common.dao.model.EduUser;
import com.edu.common.util.MD5Util;
import com.edusys.manager.service.SysApiService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户认证和授权
 * Created by Gary on 2017/3/30.
 */
public class UserRealm extends AuthorizingRealm{

    private static Logger _log = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private SysApiService apiService;

    /**
     * 授权：验证权限时调用
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取用户名
        String username = (String) principalCollection.getPrimaryPrincipal();
        //根据用户名获取用户信息
        EduUser user = apiService.selectUserByUsername(username);

        // 获取当前用户所有角色
        List<EduRole> roleList = apiService.selectRoleByUserId(user.getUserId());
        Set<String> roles = new HashSet<>();
        for (EduRole role : roleList){
            if(StringUtils.isNotBlank(role.getName())){
                roles.add(role.getName());
            }
        }

        //获取当前用户所有权限
        List<EduPermission> permissions = apiService.selectPermissionByUserId(user.getUserId());
        Set<String> permissionSet = new HashSet<>();
        for (EduPermission permission : permissions){
            if (StringUtils.isNotBlank(permission.getPermissionValue())){
                permissionSet.add(permission.getPermissionValue());
            }
        }

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证：登录时调用
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());

        //查询用户信息
        EduUser user = apiService.selectUserByUsername(username);
        if(null == user){
            throw new UnknownAccountException();
        }
        /*if(!user.getPassword().equals(MD5Util.MD5(password + user.getSalt()))) {
            throw new IncorrectCredentialsException();
        }*/
        if(!user.getPassword().equals(password)){
            throw new IncorrectCredentialsException();
        }
        /*if(user.getStatus() == 1){
            throw new LockedAccountException();
        }*/
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}
