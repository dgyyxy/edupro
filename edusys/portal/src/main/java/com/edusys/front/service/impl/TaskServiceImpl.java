package com.edusys.front.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduJobsMapper;
import com.edu.common.dao.model.EduJobs;
import com.edu.common.dao.model.EduJobsExample;
import com.edusys.front.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Gary on 2017/5/17.
 * 学习任务服务层
 */
@Service
@Transactional
@BaseService
public class TaskServiceImpl extends BaseServiceImpl<EduJobsMapper, EduJobs, EduJobsExample> implements TaskService {

    private static Logger _log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private EduJobsMapper eduJobsMapper;
}
