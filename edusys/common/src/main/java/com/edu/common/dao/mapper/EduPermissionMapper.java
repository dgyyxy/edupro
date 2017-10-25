package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduPermission;
import com.edu.common.dao.model.EduPermissionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduPermissionMapper {
    long countByExample(EduPermissionExample example);

    int deleteByExample(EduPermissionExample example);

    int deleteByPrimaryKey(Integer permissionId);

    int insert(EduPermission record);

    int insertSelective(EduPermission record);

    List<EduPermission> selectByExample(EduPermissionExample example);

    EduPermission selectByPrimaryKey(Integer permissionId);

    int updateByExampleSelective(@Param("record") EduPermission record, @Param("example") EduPermissionExample example);

    int updateByExample(@Param("record") EduPermission record, @Param("example") EduPermissionExample example);

    int updateByPrimaryKeySelective(EduPermission record);

    int updateByPrimaryKey(EduPermission record);
}