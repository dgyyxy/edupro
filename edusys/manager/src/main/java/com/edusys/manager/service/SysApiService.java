package com.edusys.manager.service;


import com.edu.common.dao.model.*;

import java.util.List;

/**
 * 系统接口
 * Created by Gary on 2017/3/28.
 */
public interface SysApiService {
    /**
     * 根据用户id获取所拥有的权限（用户和角色权限合集）
     * @param userId
     * @return
     */
    List<EduPermission> selectPermissionByUserId(Integer userId);

    /**
     * 根据用户id获取所属的角色
     * @param userId
     * @return
     */
    List<EduRole> selectRoleByUserId(Integer userId);

    /**
     * 根据角色id获取所拥有的权限
     * @param roleId
     * @return
     */
    List<EduRolePermission> selectRolePermisstionByRoleId(Integer roleId);

    /**
     * 根据条件获取机构数据
     * @param eduOrganizationExample
     * @return
     */
    List<EduOrganization> selectOrganizationByExample(EduOrganizationExample eduOrganizationExample);

    /**
     * 根据username获取用户
     * @param username
     * @return
     */
    EduUser selectUserByUsername(String username);
}
