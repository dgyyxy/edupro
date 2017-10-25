package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduQuestionCategory;
import com.edu.common.dao.model.EduQuestionCategoryExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduQuestionCategoryMapper {
    long countByExample(EduQuestionCategoryExample example);

    int deleteByExample(EduQuestionCategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduQuestionCategory record);

    int insertSelective(EduQuestionCategory record);

    List<EduQuestionCategory> selectByExample(EduQuestionCategoryExample example);

    EduQuestionCategory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduQuestionCategory record, @Param("example") EduQuestionCategoryExample example);

    int updateByExample(@Param("record") EduQuestionCategory record, @Param("example") EduQuestionCategoryExample example);

    int updateByPrimaryKeySelective(EduQuestionCategory record);

    int updateByPrimaryKey(EduQuestionCategory record);
}