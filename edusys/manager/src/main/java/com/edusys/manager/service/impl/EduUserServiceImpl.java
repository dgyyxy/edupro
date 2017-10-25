package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduUserMapper;
import com.edu.common.dao.model.EduUser;
import com.edu.common.dao.model.EduUserExample;
import com.edusys.manager.service.EduUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* EduUserService实现
* Created by Gary on 2017/4/1.
*/
@Service
@Transactional
@BaseService
public class EduUserServiceImpl extends BaseServiceImpl<EduUserMapper, EduUser, EduUserExample> implements EduUserService {

    private static Logger _log = LoggerFactory.getLogger(EduUserServiceImpl.class);

    @Autowired
    EduUserMapper eduUserMapper;

    @Override
    public List<String> getRoleNameByUserId(Integer userId) {
        return eduUserMapper.selectRoleNameByUserId(userId);
    }
}