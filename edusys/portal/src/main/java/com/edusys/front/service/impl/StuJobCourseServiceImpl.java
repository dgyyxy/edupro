package com.edusys.front.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduStuJobCourseMapper;
import com.edu.common.dao.model.EduStuJobCourse;
import com.edu.common.dao.model.EduStuJobCourseExample;
import com.edusys.front.service.StuJobCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Gary on 2017/6/6.
 */
@Service
@Transactional
@BaseService
public class StuJobCourseServiceImpl extends BaseServiceImpl<EduStuJobCourseMapper, EduStuJobCourse, EduStuJobCourseExample> implements StuJobCourseService {

    @Autowired
    private EduStuJobCourseMapper eduStuJobCourseMapper;


    @Override
    public List<EduStuJobCourse> selectJobsByStuId(Integer stuId) {
        return eduStuJobCourseMapper.selectJobsByStuId(stuId);
    }

    @Override
    public List<EduStuJobCourse> selectCoursesByStuId(Integer stuId, Integer jobId) {
        return eduStuJobCourseMapper.selectCoursesByStuId(stuId, jobId);
    }

    @Override
    public List<EduStuJobCourse> selectFavoriteList(Integer stuId, String courseName, int limit, int offset) {
        return eduStuJobCourseMapper.selectFavoriteList(stuId, courseName, limit, offset);
    }

    @Override
    public Long favoriteCountBy(Integer stuId, String courseName) {
        return eduStuJobCourseMapper.favoriteCountBy(stuId, courseName);
    }
}
