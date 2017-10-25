package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduPaper;
import com.edu.common.dao.model.EduPaperExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduPaperMapper {
    long countByExample(EduPaperExample example);

    int deleteByExample(EduPaperExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduPaper record);

    int insertSelective(EduPaper record);

    List<EduPaper> selectByExampleWithBLOBs(EduPaperExample example);

    List<EduPaper> selectByExample(EduPaperExample example);

    EduPaper selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduPaper record, @Param("example") EduPaperExample example);

    int updateByExampleWithBLOBs(@Param("record") EduPaper record, @Param("example") EduPaperExample example);

    int updateByExample(@Param("record") EduPaper record, @Param("example") EduPaperExample example);

    int updateByPrimaryKeySelective(EduPaper record);

    int updateByPrimaryKeyWithBLOBs(EduPaper record);

    int updateByPrimaryKey(EduPaper record);
}