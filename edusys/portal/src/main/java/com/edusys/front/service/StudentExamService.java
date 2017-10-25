package com.edusys.front.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.mapper.EduStudentExamMapper;
import com.edu.common.dao.model.EduStudentExam;
import com.edu.common.dao.model.EduStudentExamExample;

import java.util.List;

/**
 * Created by Gary on 2017/7/8.
 */
public interface StudentExamService extends BaseService<EduStudentExam, EduStudentExamExample> {

    //考试历史
    public List<EduStudentExam> selectStuExamHistoryList(int stuId);
}
