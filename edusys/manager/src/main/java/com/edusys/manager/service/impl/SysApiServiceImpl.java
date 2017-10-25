package com.edusys.manager.service.impl;

import com.edu.common.dao.mapper.EduOrganizationMapper;
import com.edu.common.dao.mapper.EduRolePermissionMapper;
import com.edu.common.dao.mapper.EduUserMapper;
import com.edu.common.dao.mapper.SysApiMapper;
import com.edu.common.dao.model.*;
import com.edusys.manager.service.SysApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户service实现
 * Created by Gary on 2017/3/28.
 */
@Service
@Transactional
public class SysApiServiceImpl implements SysApiService {

    private static Logger _log = LoggerFactory.getLogger(SysApiServiceImpl.class);

    @Autowired
    EduUserMapper userMapper;

    @Autowired
    SysApiMapper apiMapper;

    @Autowired
    EduRolePermissionMapper rolePermissionMapper;

    @Autowired
    EduOrganizationMapper organizationMapper;


    /**
     * 根据用户id获取所拥有的权限
     * @param userId
     * @return
     */
    @Override
    public List<EduPermission> selectPermissionByUserId(Integer userId) {
        // 用户不存在或锁定状态
        EduUser user = userMapper.selectByPrimaryKey(userId);
        if(null == user){
            _log.info("selectPermissionByUserId : userId = {}", userId);
            return null;
        }
        List<EduPermission> permissions = apiMapper.selectPermissionByUserId(userId);
        return permissions;
    }

    /**
     * 根据角色id获取所拥有的权限
     * @param userId
     * @return
     */
    @Override
    public List<EduRole> selectRoleByUserId(Integer userId) {
        // 用户不存在或禁用状态
        EduUser user = userMapper.selectByPrimaryKey(userId);
        if(null == user){
            _log.info("selectRoleByUserId : userId = {}", userId);
            return null;
        }
        List<EduRole> roles = apiMapper.selectRoleByUserId(userId);
        return roles;
    }

    /**
     * 根据角色id获取所拥有的权限
     * @param roleId
     * @return
     */
    @Override
    public List<EduRolePermission> selectRolePermisstionByRoleId(Integer roleId) {
        EduRolePermissionExample rolePermissionExample = new EduRolePermissionExample();
        rolePermissionExample.createCriteria().andRoleIdEqualTo(roleId);
        List<EduRolePermission> rolePermissions = rolePermissionMapper.selectByExample(rolePermissionExample);
        return rolePermissions;
    }

    /**
     * 根据条件获取组织数据
     * @param eduOrganizationExample
     * @return
     */
    @Override
    public List<EduOrganization> selectOrganizationByExample(EduOrganizationExample eduOrganizationExample) {
        return organizationMapper.selectByExample(eduOrganizationExample);
    }

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @Override
    public EduUser selectUserByUsername(String username) {
        EduUserExample userExample = new EduUserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<EduUser> users = userMapper.selectByExample(userExample);
        if(null != users && users.size()>0){
            return users.get(0);
        }
        return null;
    }
}
