package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduQuestionType;
import com.edu.common.dao.model.EduQuestionTypeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduQuestionTypeMapper {
    long countByExample(EduQuestionTypeExample example);

    int deleteByExample(EduQuestionTypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduQuestionType record);

    int insertSelective(EduQuestionType record);

    List<EduQuestionType> selectByExample(EduQuestionTypeExample example);

    EduQuestionType selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduQuestionType record, @Param("example") EduQuestionTypeExample example);

    int updateByExample(@Param("record") EduQuestionType record, @Param("example") EduQuestionTypeExample example);

    int updateByPrimaryKeySelective(EduQuestionType record);

    int updateByPrimaryKey(EduQuestionType record);
}