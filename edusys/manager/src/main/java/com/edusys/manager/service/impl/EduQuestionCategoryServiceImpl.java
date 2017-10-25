package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduQuestionCategoryMapper;
import com.edu.common.dao.model.EduQuestionCategory;
import com.edu.common.dao.model.EduQuestionCategoryExample;
import com.edusys.manager.service.EduQuestionCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduQuestionCategoryService实现
* Created by Gary on 2017/5/6.
*/
@Service
@Transactional
@BaseService
public class EduQuestionCategoryServiceImpl extends BaseServiceImpl<EduQuestionCategoryMapper, EduQuestionCategory, EduQuestionCategoryExample> implements EduQuestionCategoryService {

    private static Logger _log = LoggerFactory.getLogger(EduQuestionCategoryServiceImpl.class);

    @Autowired
    EduQuestionCategoryMapper eduQuestionCategoryMapper;

}