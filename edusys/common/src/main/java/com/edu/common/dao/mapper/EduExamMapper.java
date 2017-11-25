package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduExamExample;
import com.edu.common.dao.model.EduExam;
import com.edu.common.dao.pojo.ExamPassRate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduExamMapper {
    long countByExample(EduExamExample example);

    int deleteByExample(EduExamExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduExam record);

    int insertSelective(EduExam record);

    List<EduExam> selectByExample(EduExamExample example);

    List<EduExam> selectExamingByExample(EduExamExample example);

    EduExam selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduExam record, @Param("example") EduExamExample example);

    int updateByExample(@Param("record") EduExam record, @Param("example") EduExamExample example);

    int updateByPrimaryKeySelective(EduExam record);

    int updateByPrimaryKey(EduExam record);

    void batchUpdateStatus(List<Integer> idList);

    void batchUpdateUnPublishStatus(List<Integer> idList);

    List<EduExam> selectExamListByStu(@Param("stuId") Integer stuId, @Param("limit") Integer limit, @Param("offset") Integer offset);

    long countByStu(@Param("stuId") Integer stuId);

    void updateExamStatus();

    void updateExamingStatus();

    //获取考试及格率
    List<ExamPassRate> selectPassRate();
}