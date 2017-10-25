package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduStuJobCourseMapper;
import com.edu.common.dao.model.EduStuJobCourse;
import com.edu.common.dao.model.EduStuJobCourseExample;
import com.edusys.manager.service.EduStuJobCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * EduStuJobCourseService实现
 * Created by Gary on 2017/4/25.
 */
@Service
@Transactional
@BaseService
public class EduStuJobCourseServiceImpl extends BaseServiceImpl<EduStuJobCourseMapper, EduStuJobCourse, EduStuJobCourseExample> implements EduStuJobCourseService {

    private static Logger _log = LoggerFactory.getLogger(EduStuJobCourseServiceImpl.class);

    @Autowired
    EduStuJobCourseMapper eduStuJobCourseMapper;

    @Override
    public List<EduStuJobCourse> selectJobsByStuIdPage(Integer stuId, Integer limit, Integer offset, String search) {
        return eduStuJobCourseMapper.selectJobsByStuIdPage(stuId, limit, offset, search);
    }

    @Override
    public List<EduStuJobCourse> selectCoursesByStuIdPage(Integer stuId, Integer jobId, Integer limit, Integer offset, String search) {
        return eduStuJobCourseMapper.selectCoursesByStuIdPage(stuId, jobId, limit, offset, search);
    }

    @Override
    public long jobsCountByStuId(Integer stuId, String search) {
        return eduStuJobCourseMapper.jobsCountByStuId(stuId, search);
    }

    @Override
    public long courseCountByStuId(Integer stuId, Integer jobId, String search) {
        return eduStuJobCourseMapper.courseCountByStuId(stuId, jobId, search);
    }
}