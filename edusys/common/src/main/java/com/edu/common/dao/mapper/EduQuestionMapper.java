package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduQuestion;
import com.edu.common.dao.model.EduQuestionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduQuestionMapper {
    long countByExample(EduQuestionExample example);

    int deleteByExample(EduQuestionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduQuestion record);

    int insertSelective(EduQuestion record);

    List<EduQuestion> selectByExampleWithBLOBs(EduQuestionExample example);

    List<EduQuestion> selectByExample(EduQuestionExample example);

    EduQuestion selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduQuestion record, @Param("example") EduQuestionExample example);

    int updateByExampleWithBLOBs(@Param("record") EduQuestion record, @Param("example") EduQuestionExample example);

    int updateByExample(@Param("record") EduQuestion record, @Param("example") EduQuestionExample example);

    int updateByPrimaryKeySelective(EduQuestion record);

    int updateByPrimaryKeyWithBLOBs(EduQuestion record);

    int updateByPrimaryKey(EduQuestion record);

    int insertBatch(List<EduQuestion> questionList);
}