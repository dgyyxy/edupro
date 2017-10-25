package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduCoursewareMapper;
import com.edu.common.dao.model.EduCourseware;
import com.edu.common.dao.model.EduCoursewareExample;
import com.edusys.manager.service.EduCoursewareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduCoursewareService实现
* Created by Gary on 2017/4/25.
*/
@Service
@Transactional
@BaseService
public class EduCoursewareServiceImpl extends BaseServiceImpl<EduCoursewareMapper, EduCourseware, EduCoursewareExample> implements EduCoursewareService {

    private static Logger _log = LoggerFactory.getLogger(EduCoursewareServiceImpl.class);

    @Autowired
    EduCoursewareMapper eduCoursewareMapper;

    @Override
    public long sumTimeByExample(EduCoursewareExample example) {
        return eduCoursewareMapper.sumTimeByExample(example);
    }
}