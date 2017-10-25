package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduUserRoleMapper;
import com.edu.common.dao.model.EduUserRole;
import com.edu.common.dao.model.EduUserRoleExample;
import com.edusys.manager.service.EduUserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduUserRoleService实现
* Created by Gary on 2017/4/1.
*/
@Service
@Transactional
@BaseService
public class EduUserRoleServiceImpl extends BaseServiceImpl<EduUserRoleMapper, EduUserRole, EduUserRoleExample> implements EduUserRoleService {

    private static Logger _log = LoggerFactory.getLogger(EduUserRoleServiceImpl.class);

    @Autowired
    EduUserRoleMapper eduUserRoleMapper;

}