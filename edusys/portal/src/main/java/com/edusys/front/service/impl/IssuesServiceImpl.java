package com.edusys.front.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduStudentAnswerMapper;
import com.edu.common.dao.model.EduStudentAnswer;
import com.edu.common.dao.model.EduStudentAnswerExample;
import com.edusys.front.service.IssuesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Gary on 2017/11/4.
 */
@Service
@Transactional
@BaseService
public class IssuesServiceImpl extends BaseServiceImpl<EduStudentAnswerMapper, EduStudentAnswer, EduStudentAnswerExample> implements IssuesService {
}
