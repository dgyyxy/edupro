package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduUserRole;
import com.edu.common.dao.model.EduUserRoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduUserRoleMapper {
    long countByExample(EduUserRoleExample example);

    int deleteByExample(EduUserRoleExample example);

    int deleteByPrimaryKey(Integer userRoleId);

    int insert(EduUserRole record);

    int insertSelective(EduUserRole record);

    List<EduUserRole> selectByExample(EduUserRoleExample example);

    EduUserRole selectByPrimaryKey(Integer userRoleId);

    int updateByExampleSelective(@Param("record") EduUserRole record, @Param("example") EduUserRoleExample example);

    int updateByExample(@Param("record") EduUserRole record, @Param("example") EduUserRoleExample example);

    int updateByPrimaryKeySelective(EduUserRole record);

    int updateByPrimaryKey(EduUserRole record);
}