package com.edusys.front.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduCoursewareMapper;
import com.edu.common.dao.model.EduCourseware;
import com.edu.common.dao.model.EduCoursewareExample;
import com.edusys.front.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Gary on 2017/5/17.
 * 课件服务层
 */
@Service
@Transactional
@BaseService
public class CourseServiceImpl extends BaseServiceImpl<EduCoursewareMapper, EduCourseware, EduCoursewareExample> implements CourseService {

    private static Logger _log = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    private EduCoursewareMapper eduCoursewareMapper;

    @Override
    public List<Integer> getIdsByNameStr(String namestr) {
        return eduCoursewareMapper.getIdsByNameStr(namestr);
    }
}
