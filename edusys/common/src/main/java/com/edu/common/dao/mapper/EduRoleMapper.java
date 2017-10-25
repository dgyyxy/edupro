package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduRole;
import com.edu.common.dao.model.EduRoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduRoleMapper {
    long countByExample(EduRoleExample example);

    int deleteByExample(EduRoleExample example);

    int deleteByPrimaryKey(Integer roleId);

    int insert(EduRole record);

    int insertSelective(EduRole record);

    List<EduRole> selectByExample(EduRoleExample example);

    EduRole selectByPrimaryKey(Integer roleId);

    int updateByExampleSelective(@Param("record") EduRole record, @Param("example") EduRoleExample example);

    int updateByExample(@Param("record") EduRole record, @Param("example") EduRoleExample example);

    int updateByPrimaryKeySelective(EduRole record);

    int updateByPrimaryKey(EduRole record);
}