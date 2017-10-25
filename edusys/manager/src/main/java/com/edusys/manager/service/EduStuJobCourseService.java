package com.edusys.manager.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.model.EduStuJobCourse;
import com.edu.common.dao.model.EduStuJobCourseExample;

import java.util.List;

/**
* EduStuJobCourseService接口
* Created by Gary on 2017/4/25.
*/
public interface EduStuJobCourseService extends BaseService<EduStuJobCourse, EduStuJobCourseExample> {

    //根据学员ID获取所学习任务列表
    public List<EduStuJobCourse> selectJobsByStuIdPage(Integer stuId, Integer limit, Integer offset, String search);

    //根据学员ID和任务ID获取课件列表
    public List<EduStuJobCourse> selectCoursesByStuIdPage(Integer stuId, Integer jobId, Integer limit, Integer offset, String search);

    //根据学员ID获取所学习任务数
    public long jobsCountByStuId(Integer stuId, String search);

    //根据学员ID和任务ID获取课件数
    public long courseCountByStuId(Integer stuId, Integer jobId, String search);
}