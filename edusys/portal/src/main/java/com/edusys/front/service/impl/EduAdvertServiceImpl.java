package com.edusys.front.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduAdvertMapper;
import com.edu.common.dao.model.EduAdvert;
import com.edu.common.dao.model.EduAdvertExample;
import com.edusys.front.service.EduAdvertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduAdvertService实现
* Created by Gary on 2017/8/17.
*/
@Service
@Transactional
@BaseService
public class EduAdvertServiceImpl extends BaseServiceImpl<EduAdvertMapper, EduAdvert, EduAdvertExample> implements EduAdvertService {

    private static Logger _log = LoggerFactory.getLogger(EduAdvertServiceImpl.class);

    @Autowired
    EduAdvertMapper eduAdvertMapper;

}