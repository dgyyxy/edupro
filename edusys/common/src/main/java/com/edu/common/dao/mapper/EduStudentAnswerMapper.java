package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduStudentAnswer;
import com.edu.common.dao.model.EduStudentAnswerExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduStudentAnswerMapper {
    long countByExample(EduStudentAnswerExample example);

    int deleteByExample(EduStudentAnswerExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduStudentAnswer record);

    int insertSelective(EduStudentAnswer record);

    List<EduStudentAnswer> selectByExample(EduStudentAnswerExample example);

    EduStudentAnswer selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduStudentAnswer record, @Param("example") EduStudentAnswerExample example);

    int updateByExample(@Param("record") EduStudentAnswer record, @Param("example") EduStudentAnswerExample example);

    int updateByPrimaryKeySelective(EduStudentAnswer record);

    int updateByPrimaryKey(EduStudentAnswer record);
}