package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduJobCoursewareMapper;
import com.edu.common.dao.model.EduJobCourseware;
import com.edu.common.dao.model.EduJobCoursewareExample;
import com.edusys.manager.service.EduJobCoursewareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* EduJobCoursewareService实现
* Created by Gary on 2017/4/25.
*/
@Service
@Transactional
@BaseService
public class EduJobCoursewareServiceImpl extends BaseServiceImpl<EduJobCoursewareMapper, EduJobCourseware, EduJobCoursewareExample> implements EduJobCoursewareService {

    private static Logger _log = LoggerFactory.getLogger(EduJobCoursewareServiceImpl.class);

    @Autowired
    EduJobCoursewareMapper eduJobCoursewareMapper;

    @Override
    public int jobCoursewareSave(List<EduJobCourseware> list) {
        return eduJobCoursewareMapper.insertBatch(list);
    }

    @Override
    public int maxByExample(Integer jobId) {
        Integer max = eduJobCoursewareMapper.maxByExample(jobId);
        if(max == null){
            max = 0;
        }
        return max;
    }

    @Override
    public EduJobCourseware selectBySortNum(Integer sortNum, Integer jobId) {
        return eduJobCoursewareMapper.selectBySortNum(sortNum, jobId);
    }

    @Override
    public int deleteBatch(List<Integer> courseIds, Integer jobId) {
        return eduJobCoursewareMapper.deleteBatch(courseIds, jobId);
    }
}