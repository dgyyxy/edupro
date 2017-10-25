package com.edusys.front.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.model.EduCourseware;
import com.edu.common.dao.model.EduCoursewareExample;

import java.util.List;

/**
 * Created by Gary on 2017/5/17.
 */
public interface CourseService extends BaseService<EduCourseware, EduCoursewareExample>{

    public List<Integer> getIdsByNameStr(String namestr);

}
