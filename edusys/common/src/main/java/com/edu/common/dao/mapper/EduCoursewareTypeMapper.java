package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduCoursewareType;
import com.edu.common.dao.model.EduCoursewareTypeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduCoursewareTypeMapper {
    long countByExample(EduCoursewareTypeExample example);

    int deleteByExample(EduCoursewareTypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduCoursewareType record);

    int insertSelective(EduCoursewareType record);

    List<EduCoursewareType> selectByExample(EduCoursewareTypeExample example);

    EduCoursewareType selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduCoursewareType record, @Param("example") EduCoursewareTypeExample example);

    int updateByExample(@Param("record") EduCoursewareType record, @Param("example") EduCoursewareTypeExample example);

    int updateByPrimaryKeySelective(EduCoursewareType record);

    int updateByPrimaryKey(EduCoursewareType record);
}