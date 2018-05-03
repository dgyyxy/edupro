package com.edusys.front.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.model.EduExam;
import com.edu.common.dao.model.EduExamExample;
import com.edu.common.dao.model.EduPaper;
import com.edu.common.dao.model.EduStudentExam;
import com.edu.common.dao.pojo.AnswerSheet;

import java.util.List;

/**
 * Created by Gary on 2017/5/17.
 */
public interface ExamService extends BaseService<EduExam, EduExamExample>{
    //验证考试密码
    public int exampwd(int examId, int stuId);

    //获取学员对应的考试
    public EduStudentExam getStudentExamById(int id);

    //获取当前学员对应的考试
    public List<EduExam> selectExamListByStu(Integer stuId, Integer limit, Integer offset);

    //获取当前学员对应的考试总数
    public long countByStu(Integer stuId);

    //更新已经结束的考试状态
    public void updateExamStatus();

    //更新正在进行中的考试
    public void updateExaming();

    //提交每一道题
    public void updateExamQuestion(AnswerSheet answerSheet, String answerSheetStr, int approved);

    // 创建临时试卷
    public void createPaper(EduPaper paper, EduExam exam, EduStudentExam studentExam) throws Exception;

    // 创建临时试卷-题库分类
    public void createPaperRule(EduPaper paper, EduExam exam, EduStudentExam studentExam) throws Exception;

    // 获取试卷
    public EduPaper getPaperById(Integer paperId);
}
