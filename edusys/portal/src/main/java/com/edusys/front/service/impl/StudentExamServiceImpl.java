package com.edusys.front.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduStudentExamMapper;
import com.edu.common.dao.model.EduStudentExam;
import com.edu.common.dao.model.EduStudentExamExample;
import com.edusys.front.service.StudentExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Gary on 2017/7/8.
 */
@Service
@Transactional
@BaseService
public class StudentExamServiceImpl extends BaseServiceImpl<EduStudentExamMapper, EduStudentExam, EduStudentExamExample> implements StudentExamService {

    @Autowired
    private EduStudentExamMapper studentExamMapper;

    @Override
    public List<EduStudentExam> selectStuExamHistoryList(int stuId) {
        return studentExamMapper.selectStuExamHistoryList(stuId);
    }
}
