package com.edusys.front.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduExamMapper;
import com.edu.common.dao.mapper.EduStudentExamMapper;
import com.edu.common.dao.model.EduExam;
import com.edu.common.dao.model.EduExamExample;
import com.edu.common.dao.model.EduStudentExam;
import com.edu.common.dao.model.EduStudentExamExample;
import com.edu.common.dao.pojo.AnswerSheet;
import com.edusys.front.service.ExamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Gary on 2017/5/17.
 * 考试服务层
 */
@Service
@Transactional
@BaseService
public class ExamServiceImpl extends BaseServiceImpl<EduExamMapper, EduExam, EduExamExample> implements ExamService{

    private static Logger _log = LoggerFactory.getLogger(ExamServiceImpl.class);

    @Autowired
    private EduExamMapper eduExamMapper;

    @Autowired
    private EduStudentExamMapper eduStudentExamMapper;

    /**
     * 验证考试密码
     * @param examId
     * @param stuId
     * @return
     */
    @Override
    public int exampwd(int examId, int stuId) {
        EduStudentExamExample esee = new EduStudentExamExample();
        EduStudentExamExample.Criteria criteria = esee.createCriteria();

        criteria.andExamIdEqualTo(examId);
        criteria.andStuIdEqualTo(stuId);

        List<EduStudentExam> list = eduStudentExamMapper.selectByExample(esee);
        int stuexamId = 0;
        if(list!=null && list.size()>0){
            stuexamId = list.get(0).getId();
        }

        return stuexamId;
    }

    /**
     * 获取学员对应的考试信息
     * @param id
     * @return
     */
    @Override
    public EduStudentExam getStudentExamById(int id) {
        EduStudentExam eduStudentExam = eduStudentExamMapper.selectByPrimaryKey(id);
        return eduStudentExam;
    }

    @Override
    public List<EduExam> selectExamListByStu(Integer stuId, Integer limit, Integer offset) {
        return eduExamMapper.selectExamListByStu(stuId, limit, offset);
    }

    @Override
    public long countByStu(Integer stuId) {
        return eduExamMapper.countByStu(stuId);
    }

    @Override
    public void updateExamStatus() {
        eduExamMapper.updateExamStatus();
    }

    @Override
    public void updateExaming() {
        eduExamMapper.updateExamingStatus();
    }

    @Override
    public void updateExamQuestion(AnswerSheet answerSheet, String answerSheetStr, int approved) {
        eduStudentExamMapper.updateExamQuestion(answerSheet, answerSheetStr, approved);
    }
}
