package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduPaperCategory;
import com.edu.common.dao.model.EduPaperCategoryExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduPaperCategoryMapper {
    long countByExample(EduPaperCategoryExample example);

    int deleteByExample(EduPaperCategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduPaperCategory record);

    int insertSelective(EduPaperCategory record);

    List<EduPaperCategory> selectByExample(EduPaperCategoryExample example);

    EduPaperCategory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduPaperCategory record, @Param("example") EduPaperCategoryExample example);

    int updateByExample(@Param("record") EduPaperCategory record, @Param("example") EduPaperCategoryExample example);

    int updateByPrimaryKeySelective(EduPaperCategory record);

    int updateByPrimaryKey(EduPaperCategory record);
}