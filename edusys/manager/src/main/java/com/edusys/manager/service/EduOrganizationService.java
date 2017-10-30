package com.edusys.manager.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.model.EduOrganization;
import com.edu.common.dao.model.EduOrganizationExample;

import java.util.List;

/**
* EduOrganizationService接口
* Created by Gary on 2017/4/1.
*/
public interface EduOrganizationService extends BaseService<EduOrganization, EduOrganizationExample> {

    int insertBatch(List<EduOrganization> organizationList);

    List<String> selectOrganNameList(Integer parentId);
}