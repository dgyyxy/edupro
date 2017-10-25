package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduRoleMapper;
import com.edu.common.dao.model.EduRole;
import com.edu.common.dao.model.EduRoleExample;
import com.edusys.manager.service.EduRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduRoleService实现
* Created by Gary on 2017/4/1.
*/
@Service
@Transactional
@BaseService
public class EduRoleServiceImpl extends BaseServiceImpl<EduRoleMapper, EduRole, EduRoleExample> implements EduRoleService {

    private static Logger _log = LoggerFactory.getLogger(EduRoleServiceImpl.class);

    @Autowired
    EduRoleMapper eduRoleMapper;

}