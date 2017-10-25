package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduJobCourseware;
import com.edu.common.dao.model.EduJobCoursewareExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduJobCoursewareMapper {
    long countByExample(EduJobCoursewareExample example);

    int deleteByExample(EduJobCoursewareExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduJobCourseware record);

    int insertSelective(EduJobCourseware record);

    List<EduJobCourseware> selectByExample(EduJobCoursewareExample example);

    EduJobCourseware selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduJobCourseware record, @Param("example") EduJobCoursewareExample example);

    int updateByExample(@Param("record") EduJobCourseware record, @Param("example") EduJobCoursewareExample example);

    int updateByPrimaryKeySelective(EduJobCourseware record);

    int updateByPrimaryKey(EduJobCourseware record);

    Integer maxByExample(Integer jobId);

    EduJobCourseware selectBySortNum(@Param("sortNum") Integer sortNum, @Param("jobId") Integer jobId);

    int insertBatch(List<EduJobCourseware> jobCoursewareList);

    int deleteBatch(@Param("courseIds") List<Integer> courseIds, @Param("jobId") Integer jobId);
}