package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduJobsMapper;
import com.edu.common.dao.model.EduJobs;
import com.edu.common.dao.model.EduJobsExample;
import com.edusys.manager.service.EduJobsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduJobsService实现
* Created by Gary on 2017/4/25.
*/
@Service
@Transactional
@BaseService
public class EduJobsServiceImpl extends BaseServiceImpl<EduJobsMapper, EduJobs, EduJobsExample> implements EduJobsService {

    private static Logger _log = LoggerFactory.getLogger(EduJobsServiceImpl.class);

    @Autowired
    EduJobsMapper eduJobsMapper;

}