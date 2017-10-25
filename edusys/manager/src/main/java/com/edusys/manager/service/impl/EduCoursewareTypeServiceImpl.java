package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduCoursewareTypeMapper;
import com.edu.common.dao.model.EduCoursewareType;
import com.edu.common.dao.model.EduCoursewareTypeExample;
import com.edusys.manager.service.EduCoursewareTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduCoursewareTypeService实现
* Created by Gary on 2017/4/25.
*/
@Service
@Transactional
@BaseService
public class EduCoursewareTypeServiceImpl extends BaseServiceImpl<EduCoursewareTypeMapper, EduCoursewareType, EduCoursewareTypeExample> implements EduCoursewareTypeService {

    private static Logger _log = LoggerFactory.getLogger(EduCoursewareTypeServiceImpl.class);

    @Autowired
    EduCoursewareTypeMapper eduCoursewareTypeMapper;

}