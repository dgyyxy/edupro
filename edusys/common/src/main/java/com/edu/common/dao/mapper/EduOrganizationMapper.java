package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduOrganization;
import com.edu.common.dao.model.EduOrganizationExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduOrganizationMapper {
    long countByExample(EduOrganizationExample example);

    int deleteByExample(EduOrganizationExample example);

    int deleteByPrimaryKey(Integer organizationId);

    int insert(EduOrganization record);

    int insertSelective(EduOrganization record);

    List<EduOrganization> selectByExample(EduOrganizationExample example);

    EduOrganization selectByPrimaryKey(Integer organizationId);

    int updateByExampleSelective(@Param("record") EduOrganization record, @Param("example") EduOrganizationExample example);

    int updateByExample(@Param("record") EduOrganization record, @Param("example") EduOrganizationExample example);

    int updateByPrimaryKeySelective(EduOrganization record);

    int updateByPrimaryKey(EduOrganization record);

    int insertBatch(List<EduOrganization> organizationList);

    List<String> selectOrganNameList(@Param("parentId") Integer parentId);
}