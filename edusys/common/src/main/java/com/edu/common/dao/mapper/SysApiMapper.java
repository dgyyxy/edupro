package com.edu.common.dao.mapper;


import com.edu.common.dao.model.EduPermission;
import com.edu.common.dao.model.EduRole;

import java.util.List;

/**
 * Created by Gary on 2017/4/1.
 */
public interface SysApiMapper {
    // 根据用户id获取所拥有的权限
    List<EduPermission> selectPermissionByUserId(Integer userId);

    // 根据用户id获取所属的角色
    List<EduRole> selectRoleByUserId(Integer userId);
}
