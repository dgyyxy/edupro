package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduUserOrganization;
import com.edu.common.dao.model.EduUserOrganizationExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduUserOrganizationMapper {
    long countByExample(EduUserOrganizationExample example);

    int deleteByExample(EduUserOrganizationExample example);

    int deleteByPrimaryKey(Integer userOrganization);

    int insert(EduUserOrganization record);

    int insertSelective(EduUserOrganization record);

    List<EduUserOrganization> selectByExample(EduUserOrganizationExample example);

    EduUserOrganization selectByPrimaryKey(Integer userOrganization);

    int updateByExampleSelective(@Param("record") EduUserOrganization record, @Param("example") EduUserOrganizationExample example);

    int updateByExample(@Param("record") EduUserOrganization record, @Param("example") EduUserOrganizationExample example);

    int updateByPrimaryKeySelective(EduUserOrganization record);

    int updateByPrimaryKey(EduUserOrganization record);
}