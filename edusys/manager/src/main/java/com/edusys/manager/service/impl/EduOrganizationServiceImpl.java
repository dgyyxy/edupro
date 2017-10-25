package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduOrganizationMapper;
import com.edu.common.dao.model.EduOrganization;
import com.edu.common.dao.model.EduOrganizationExample;
import com.edusys.manager.service.EduOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduOrganizationService实现
* Created by Gary on 2017/4/1.
*/
@Service
@Transactional
@BaseService
public class EduOrganizationServiceImpl extends BaseServiceImpl<EduOrganizationMapper, EduOrganization, EduOrganizationExample> implements EduOrganizationService {

    private static Logger _log = LoggerFactory.getLogger(EduOrganizationServiceImpl.class);

    @Autowired
    EduOrganizationMapper eduOrganizationMapper;

}