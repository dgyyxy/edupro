package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduExam;
import com.edu.common.dao.model.EduStudentExam;
import com.edu.common.dao.model.EduStudentExamExample;
import com.edu.common.dao.pojo.AnswerSheet;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduStudentExamMapper {
    long countByExample(EduStudentExamExample example);

    int deleteByExample(EduStudentExamExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduStudentExam record);

    int insertSelective(EduStudentExam record);

    List<EduStudentExam> selectByExampleWithBLOBs(EduStudentExamExample example);

    List<EduStudentExam> selectByExample(EduStudentExamExample example);

    EduStudentExam selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduStudentExam record, @Param("example") EduStudentExamExample example);

    int updateByExampleWithBLOBs(@Param("record") EduStudentExam record, @Param("example") EduStudentExamExample example);

    int updateByExample(@Param("record") EduStudentExam record, @Param("example") EduStudentExamExample example);

    int updateByPrimaryKeySelective(EduStudentExam record);

    int updateByPrimaryKeyWithBLOBs(EduStudentExam record);

    int updateByPrimaryKey(EduStudentExam record);

    int insertBatch(List<EduStudentExam> studentExams);

    List<Integer> getStuIdList(Integer examId);

    /**
     * 更新答题卡及得分
     * @param answerSheet
     * @param answerSheetStr
     */
    public void updateUserExamHist(@Param("answerSheet") AnswerSheet answerSheet, @Param("answerSheetStr") String answerSheetStr, @Param("approved") int approved);

    public void updateExamQuestion(@Param("answerSheet") AnswerSheet answerSheet, @Param("answerSheetStr") String answerSheetStr, @Param("approved") int approved);

    //考试历史
    public List<EduStudentExam> selectStuExamHistoryList(@Param("stuId") int stuId);

    //更新数据
    public void updateStudentExamByExamId(@Param("record") EduExam record);

    public void stopExamOperate(List<Integer> idList);
}