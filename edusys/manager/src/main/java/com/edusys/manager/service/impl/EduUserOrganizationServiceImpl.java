package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduUserOrganizationMapper;
import com.edu.common.dao.model.EduUserOrganization;
import com.edu.common.dao.model.EduUserOrganizationExample;
import com.edusys.manager.service.EduUserOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduUserOrganizationService实现
* Created by Gary on 2017/4/1.
*/
@Service
@Transactional
@BaseService
public class EduUserOrganizationServiceImpl extends BaseServiceImpl<EduUserOrganizationMapper, EduUserOrganization, EduUserOrganizationExample> implements EduUserOrganizationService {

    private static Logger _log = LoggerFactory.getLogger(EduUserOrganizationServiceImpl.class);

    @Autowired
    EduUserOrganizationMapper eduUserOrganizationMapper;

}