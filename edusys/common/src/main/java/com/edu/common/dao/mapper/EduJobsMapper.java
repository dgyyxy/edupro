package com.edu.common.dao.mapper;

import com.edu.common.dao.model.EduJobs;
import com.edu.common.dao.model.EduJobsExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EduJobsMapper {
    long countByExample(EduJobsExample example);

    int deleteByExample(EduJobsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EduJobs record);

    int insertSelective(EduJobs record);

    List<EduJobs> selectByExample(EduJobsExample example);

    EduJobs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EduJobs record, @Param("example") EduJobsExample example);

    int updateByExample(@Param("record") EduJobs record, @Param("example") EduJobsExample example);

    int updateByPrimaryKeySelective(EduJobs record);

    int updateByPrimaryKey(EduJobs record);
}