package com.edusys.front.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduOrganizationMapper;
import com.edu.common.dao.model.EduOrganization;
import com.edu.common.dao.model.EduOrganizationExample;
import com.edusys.front.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Gary on 2017/5/17.
 * 组织机构服务层
 */
@Service
@Transactional
@BaseService
public class OrganizationServiceImpl extends BaseServiceImpl<EduOrganizationMapper, EduOrganization, EduOrganizationExample> implements OrganizationService {

    private static Logger _log = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private EduOrganizationMapper eduOrganizationMapper;
}
