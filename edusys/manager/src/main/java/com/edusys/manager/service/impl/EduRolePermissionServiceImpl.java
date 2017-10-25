package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduRolePermissionMapper;
import com.edu.common.dao.model.EduRolePermission;
import com.edu.common.dao.model.EduRolePermissionExample;
import com.edusys.manager.service.EduRolePermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduRolePermissionService实现
* Created by Gary on 2017/4/1.
*/
@Service
@Transactional
@BaseService
public class EduRolePermissionServiceImpl extends BaseServiceImpl<EduRolePermissionMapper, EduRolePermission, EduRolePermissionExample> implements EduRolePermissionService {

    private static Logger _log = LoggerFactory.getLogger(EduRolePermissionServiceImpl.class);

    @Autowired
    EduRolePermissionMapper eduRolePermissionMapper;

}