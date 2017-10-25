package com.edusys.front.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.model.EduStuJobCourse;
import com.edu.common.dao.model.EduStuJobCourseExample;

import java.util.List;

/**
 * Created by Gary on 2017/6/6.
 */
public interface StuJobCourseService extends BaseService<EduStuJobCourse, EduStuJobCourseExample> {

    public List<EduStuJobCourse> selectJobsByStuId(Integer stuId);

    public List<EduStuJobCourse> selectCoursesByStuId(Integer stuId, Integer jobId);

    public List<EduStuJobCourse> selectFavoriteList(Integer stuId, String courseName, int limit, int offset);

    public Long favoriteCountBy(Integer stuId, String courseName);
}
