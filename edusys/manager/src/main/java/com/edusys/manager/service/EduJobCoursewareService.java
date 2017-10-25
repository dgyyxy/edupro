package com.edusys.manager.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.model.EduJobCourseware;
import com.edu.common.dao.model.EduJobCoursewareExample;

import java.util.List;

/**
* EduJobCoursewareService接口
* Created by Gary on 2017/4/25.
*/
public interface EduJobCoursewareService extends BaseService<EduJobCourseware, EduJobCoursewareExample> {

    int jobCoursewareSave(List<EduJobCourseware> list);

    int maxByExample(Integer jobId);

    EduJobCourseware selectBySortNum(Integer sortNum, Integer jobId);

    int deleteBatch(List<Integer> courseIds, Integer jobId);
}