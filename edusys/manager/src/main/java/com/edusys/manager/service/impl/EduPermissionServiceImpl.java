package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduPermissionMapper;
import com.edu.common.dao.model.EduPermission;
import com.edu.common.dao.model.EduPermissionExample;
import com.edusys.manager.service.EduPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduPermissionService实现
* Created by Gary on 2017/4/1.
*/
@Service
@Transactional
@BaseService
public class EduPermissionServiceImpl extends BaseServiceImpl<EduPermissionMapper, EduPermission, EduPermissionExample> implements EduPermissionService {

    private static Logger _log = LoggerFactory.getLogger(EduPermissionServiceImpl.class);

    @Autowired
    EduPermissionMapper eduPermissionMapper;

}