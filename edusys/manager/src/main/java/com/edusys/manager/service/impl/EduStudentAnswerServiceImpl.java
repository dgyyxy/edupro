package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduStudentAnswerMapper;
import com.edu.common.dao.model.EduStudentAnswer;
import com.edu.common.dao.model.EduStudentAnswerExample;
import com.edusys.manager.service.EduStudentAnswerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduStudentAnswerService实现
* Created by Gary on 2017/10/30.
*/
@Service
@Transactional
@BaseService
public class EduStudentAnswerServiceImpl extends BaseServiceImpl<EduStudentAnswerMapper, EduStudentAnswer, EduStudentAnswerExample> implements EduStudentAnswerService {

    private static Logger _log = LoggerFactory.getLogger(EduStudentAnswerServiceImpl.class);

    @Autowired
    EduStudentAnswerMapper eduStudentAnswerMapper;

}