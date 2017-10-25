package com.edusys.manager.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.model.EduCourseware;
import com.edu.common.dao.model.EduCoursewareExample;

/**
* EduCoursewareService接口
* Created by Gary on 2017/4/25.
*/
public interface EduCoursewareService extends BaseService<EduCourseware, EduCoursewareExample> {

    long sumTimeByExample(EduCoursewareExample example);
}