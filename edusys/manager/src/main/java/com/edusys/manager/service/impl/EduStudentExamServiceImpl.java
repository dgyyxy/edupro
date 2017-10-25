package com.edusys.manager.service.impl;

import com.edu.common.annotation.BaseService;
import com.edu.common.base.BaseServiceImpl;
import com.edu.common.dao.mapper.EduStudentExamMapper;
import com.edu.common.dao.mapper.EduStudentMapper;
import com.edu.common.dao.model.EduExam;
import com.edu.common.dao.model.EduPaper;
import com.edu.common.dao.model.EduStudentExam;
import com.edu.common.dao.model.EduStudentExamExample;
import com.edu.common.dao.pojo.AnswerSheet;
import com.edu.common.util.RandomUtil;
import com.edusys.manager.service.EduStudentExamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* EduStudentExamService实现
* Created by Gary on 2017/5/10.
*/
@Service
@Transactional
@BaseService
public class EduStudentExamServiceImpl extends BaseServiceImpl<EduStudentExamMapper, EduStudentExam, EduStudentExamExample> implements EduStudentExamService {

    private static Logger _log = LoggerFactory.getLogger(EduStudentExamServiceImpl.class);

    @Autowired
    EduStudentExamMapper eduStudentExamMapper;

    @Autowired
    EduStudentMapper eduStudentMapper;


    /**
     * 选取开考学员
     * @param stuIds
     * @param paper
     * @param exam
     */
    @Override
    public void examByStudents(List<Integer> stuIds, EduPaper paper, EduExam exam) {
        if(stuIds.size()>0){
            List<EduStudentExam> studentExams = new ArrayList<EduStudentExam>();
            for (Integer id : stuIds){
                EduStudentExam studentExam = new EduStudentExam();
                studentExam.setStuId(id);
                studentExam.setIslook(exam.getIslook());
                studentExam.setExamId(exam.getId());
                studentExam.setPaperId(paper.getId());
                studentExam.setContent(paper.getContent());
                studentExam.setDuration(exam.getDuration());
                studentExam.setApproved(0);//审核通过
                studentExam.setExamPassword(RandomUtil.getFourRandNum());
                studentExam.setDisorganize(exam.getDisorganize());//是否打乱题目显示顺序
                studentExam.setPoint(exam.getTotalPoint());
                long time = System.currentTimeMillis();
                studentExam.setCreateTime(time);

                studentExams.add(studentExam);
            }
            //批量新增
            eduStudentExamMapper.insertBatch(studentExams);
        }
    }

    /**
     * 通过获取参考学员列表
     * @param examId
     * @return
     */
    @Override
    public List<Integer> getStuIdList(Integer examId) {
        return eduStudentExamMapper.getStuIdList(examId);
    }

    @Override
    public void updateUserExamHist(AnswerSheet answerSheet, String answerSheetStr, int approved) {
        eduStudentExamMapper.updateUserExamHist(answerSheet, answerSheetStr, approved);
    }

    @Override
    public void updateStudentExamByExamId(EduExam record) {
        eduStudentExamMapper.updateStudentExamByExamId(record);
    }

    @Override
    public void stopExamOperate(List<Integer> idList) {
        eduStudentExamMapper.stopExamOperate(idList);
    }

}