package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduExamMapper;
import com.edu.common.dao.model.EduExam;
import com.edu.common.dao.model.EduExamExample;
import com.edu.common.dao.pojo.ExamPassRate;
import com.edusys.manager.service.EduExamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* EduExamService实现
* Created by Gary on 2017/5/6.
*/
@Service
@Transactional
@BaseService
public class EduExamServiceImpl extends BaseServiceImpl<EduExamMapper, EduExam, EduExamExample> implements EduExamService {

    private static Logger _log = LoggerFactory.getLogger(EduExamServiceImpl.class);

    @Autowired
    EduExamMapper eduExamMapper;

    @Override
    public void batchUpdateStatus(List<Integer> idList) {
        eduExamMapper.batchUpdateStatus(idList);
    }

    @Override
    public void batchUpdateUnPublishStatus(List<Integer> idList) {
        eduExamMapper.batchUpdateUnPublishStatus(idList);
    }

    @Override
    public void batchUpdateEndExamStatus(List<Integer> idList) {
        eduExamMapper.batchUpdateEndExamStatus(idList);
    }

    @Override
    public void updateStatus() {
        eduExamMapper.updateExamStatus();
        eduExamMapper.updateExamingStatus();
    }

    @Override
    public List<EduExam> selectExamingByExample(EduExamExample example) {
        return eduExamMapper.selectExamingByExample(example);
    }

    @Override
    public List<ExamPassRate> selectPassRate() {
        return eduExamMapper.selectPassRate();
    }
}