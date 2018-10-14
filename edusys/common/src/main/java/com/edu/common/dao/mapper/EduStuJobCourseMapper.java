package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduStuJobCourse;
import com.edu.common.dao.model.EduStuJobCourseExample;
import com.edu.common.dao.pojo.ExportStudyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduStuJobCourseMapper {
    long countByExample(EduStuJobCourseExample example);

    int deleteByExample(EduStuJobCourseExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduStuJobCourse record);

    int insertSelective(EduStuJobCourse record);

    List<EduStuJobCourse> selectByExample(EduStuJobCourseExample example);

    EduStuJobCourse selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduStuJobCourse record, @Param("example") EduStuJobCourseExample example);

    int updateByExample(@Param("record") EduStuJobCourse record, @Param("example") EduStuJobCourseExample example);

    int updateByPrimaryKeySelective(EduStuJobCourse record);

    int updateByPrimaryKey(EduStuJobCourse record);

    List<EduStuJobCourse> selectJobsByStuId(@Param("stuId") Integer stuId);

    List<EduStuJobCourse> selectJobsByStuIdPage(@Param("stuId") Integer stuId, @Param("limit") int limit, @Param("offset") int offset, @Param("search") String search);

    List<EduStuJobCourse> selectCoursesByStuId(@Param("stuId") Integer stuId, @Param("jobId") Integer jobId);

    List<EduStuJobCourse> selectCoursesByStuIdPage(@Param("stuId") Integer stuId, @Param("jobId") Integer jobId, @Param("limit") int limit, @Param("offset") int offset, @Param("search") String search);

    List<EduStuJobCourse> selectFavoriteList(@Param("stuId") Integer stuId, @Param("courseName") String courseName, @Param("limit") int limit, @Param("offset") int offset);

    long favoriteCountBy(@Param("stuId") Integer stuId, @Param("courseName") String courseName);

    long jobsCountByStuId(@Param("stuId") Integer stuId, @Param("search") String search);

    long courseCountByStuId(@Param("stuId") Integer stuId, @Param("jobId") Integer jobId, @Param("search") String search);

    List<ExportStudyVo> exportStudyCourseListBy(@Param("organId") Integer organId, @Param("jobId") Integer jobId);
}