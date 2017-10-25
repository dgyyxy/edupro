package com.edusys.front.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduNoticeMapper;
import com.edu.common.dao.model.EduNotice;
import com.edu.common.dao.model.EduNoticeExample;
import com.edusys.front.service.EduNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* EduNoticeService实现
* Created by Gary on 2017/8/17.
*/
@Service
@Transactional
@BaseService
public class EduNoticeServiceImpl extends BaseServiceImpl<EduNoticeMapper, EduNotice, EduNoticeExample> implements EduNoticeService {

    private static Logger _log = LoggerFactory.getLogger(EduNoticeServiceImpl.class);

    @Autowired
    EduNoticeMapper eduNoticeMapper;

}