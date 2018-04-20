package com.edusys.manager.service;

import com.edu.common.base.BaseService;
import com.edu.common.dao.model.EduExam;
import com.edu.common.dao.model.EduPaper;
import com.edu.common.dao.model.EduStudentExam;
import com.edu.common.dao.model.EduStudentExamExample;
import com.edu.common.dao.pojo.AnswerSheet;

import javax.servlet.ServletOutputStream;
import java.util.List;

/**
* EduStudentExamService接口
* Created by Gary on 2017/5/10.
*/
public interface EduStudentExamService extends BaseService<EduStudentExam, EduStudentExamExample> {

    //选取开考学员
    public void examByStudents(List<Integer> stuIds, EduPaper paper, EduExam exam);

    public List<Integer> getStuIdList(Integer examId);

    /**
     * 更新答题卡及得分
     * @param answerSheet
     * @param answerSheetStr
     */
    public void updateUserExamHist(AnswerSheet answerSheet, String answerSheetStr, int approved);

    public void updateStudentExamByExamId(EduExam record);

    public void stopExamOperate(List<Integer> idList);

    public int exportExcel(String[] titles, ServletOutputStream outputStream, List<EduStudentExam> studentExamList, String examName, String className, String companyName, String passRate);
}