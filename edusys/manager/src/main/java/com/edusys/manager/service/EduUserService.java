package com.edusys.manager.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.model.EduUser;
import com.edu.common.dao.model.EduUserExample;

import java.util.List;

/**
* EduUserService接口
* Created by Gary on 2017/4/1.
*/
public interface EduUserService extends BaseService<EduUser, EduUserExample> {

    public List<String> getRoleNameByUserId(Integer userId);
}