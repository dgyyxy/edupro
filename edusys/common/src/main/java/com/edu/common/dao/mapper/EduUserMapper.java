package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduUser;
import com.edu.common.dao.model.EduUserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduUserMapper {
    long countByExample(EduUserExample example);

    int deleteByExample(EduUserExample example);

    int deleteByPrimaryKey(Integer userId);

    int insert(EduUser record);

    int insertSelective(EduUser record);

    List<EduUser> selectByExample(EduUserExample example);

    EduUser selectByPrimaryKey(Integer userId);

    int updateByExampleSelective(@Param("record") EduUser record, @Param("example") EduUserExample example);

    int updateByExample(@Param("record") EduUser record, @Param("example") EduUserExample example);

    int updateByPrimaryKeySelective(EduUser record);

    int updateByPrimaryKey(EduUser record);

    List<String> selectRoleNameByUserId(@Param("userId") Integer userId);
}