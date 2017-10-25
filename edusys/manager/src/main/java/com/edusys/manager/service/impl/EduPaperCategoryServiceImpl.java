package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduPaperCategoryMapper;
import com.edu.common.dao.model.EduPaperCategory;
import com.edu.common.dao.model.EduPaperCategoryExample;
import com.edusys.manager.service.EduPaperCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduPaperCategoryService实现
* Created by Gary on 2017/5/6.
*/
@Service
@Transactional
@BaseService
public class EduPaperCategoryServiceImpl extends BaseServiceImpl<EduPaperCategoryMapper, EduPaperCategory, EduPaperCategoryExample> implements EduPaperCategoryService {

    private static Logger _log = LoggerFactory.getLogger(EduPaperCategoryServiceImpl.class);

    @Autowired
    EduPaperCategoryMapper eduPaperCategoryMapper;

}