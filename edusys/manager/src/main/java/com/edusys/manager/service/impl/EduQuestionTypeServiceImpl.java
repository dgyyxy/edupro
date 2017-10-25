package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduQuestionTypeMapper;
import com.edu.common.dao.model.EduQuestionType;
import com.edu.common.dao.model.EduQuestionTypeExample;
import com.edusys.manager.service.EduQuestionTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduQuestionTypeService实现
* Created by Gary on 2017/5/6.
*/
@Service
@Transactional
@BaseService
public class EduQuestionTypeServiceImpl extends BaseServiceImpl<EduQuestionTypeMapper, EduQuestionType, EduQuestionTypeExample> implements EduQuestionTypeService {

    private static Logger _log = LoggerFactory.getLogger(EduQuestionTypeServiceImpl.class);

    @Autowired
    EduQuestionTypeMapper eduQuestionTypeMapper;

}