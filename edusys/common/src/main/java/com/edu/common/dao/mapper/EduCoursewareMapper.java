package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduCourseware;
import com.edu.common.dao.model.EduCoursewareExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduCoursewareMapper {
    long countByExample(EduCoursewareExample example);

    int deleteByExample(EduCoursewareExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduCourseware record);

    int insertSelective(EduCourseware record);

    List<EduCourseware> selectByExample(EduCoursewareExample example);

    EduCourseware selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduCourseware record, @Param("example") EduCoursewareExample example);

    int updateByExample(@Param("record") EduCourseware record, @Param("example") EduCoursewareExample example);

    int updateByPrimaryKeySelective(EduCourseware record);

    int updateByPrimaryKey(EduCourseware record);

    long sumTimeByExample(EduCoursewareExample example);

    List<Integer> getIdsByNameStr(@Param("namestr") String namestr);
}