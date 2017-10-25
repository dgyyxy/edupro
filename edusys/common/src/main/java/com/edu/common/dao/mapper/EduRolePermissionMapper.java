package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduRolePermission;
import com.edu.common.dao.model.EduRolePermissionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduRolePermissionMapper {
    long countByExample(EduRolePermissionExample example);

    int deleteByExample(EduRolePermissionExample example);

    int deleteByPrimaryKey(Integer rolePermissionId);

    int insert(EduRolePermission record);

    int insertSelective(EduRolePermission record);

    List<EduRolePermission> selectByExample(EduRolePermissionExample example);

    EduRolePermission selectByPrimaryKey(Integer rolePermissionId);

    int updateByExampleSelective(@Param("record") EduRolePermission record, @Param("example") EduRolePermissionExample example);

    int updateByExample(@Param("record") EduRolePermission record, @Param("example") EduRolePermissionExample example);

    int updateByPrimaryKeySelective(EduRolePermission record);

    int updateByPrimaryKey(EduRolePermission record);
}